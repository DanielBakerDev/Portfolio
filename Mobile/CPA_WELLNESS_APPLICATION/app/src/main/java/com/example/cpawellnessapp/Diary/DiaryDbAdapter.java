package com.example.cpawellnessapp.Diary;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Created by lwong on 9/28/2015.
 */
public class DiaryDbAdapter {
    static final String KEY_ROWID = "_id";
    //Columns
    static final String DATE_RECORDED = "dateRecorded";
    static final String ENTRY_NUMBER = "entryNumber";
    static final String FILE_URI = "fileURI";
    static final String SMILE_SCORE = "smileScore";

    static final String TAG = "DiaryDBAdapter";

    static final String DATABASE_NAME = "CPAWellness";
    //Tables
    static final String TABLE_NAME = "diaryEntries";
    static final int DATABASE_VERSION = 5;

    static final String DATABASE_CREATE =
            "create table "+TABLE_NAME+" (_id integer primary key autoincrement, "
                    + DATE_RECORDED + " datetime not null, "+ENTRY_NUMBER+" integer not null, "+FILE_URI+" string not null,"+ SMILE_SCORE+ " string not null);";

    static Context context;

    static DatabaseHelper DBHelper;
    static SQLiteDatabase db;

    public DiaryDbAdapter(Context ctx)
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    public int getNextEntryNumber(@NotNull String dateRecorded) {
        int lastEntry = 0;
        ArrayList<Diary> diaries = getAllDiaryEntries();
        for(Diary diary : diaries){
            if(dateRecorded == diary.dateRecorded) {
                if(lastEntry < diary.getEntryNumber()) {
                    lastEntry = diary.getEntryNumber();
                }
            }
        };
        return ++lastEntry;
    }

    public static class DatabaseHelper extends SQLiteOpenHelper
    {
        public DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            try {
                db.execSQL(DATABASE_CREATE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
            onCreate(db);
        }
    }

    //---opens the database---
    public DiaryDbAdapter open() throws SQLException
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //---closes the database---
    public void close()
    {
        DBHelper.close();
    }

    //---insert a contact into the database---
    public long insertDiaryEntry(Diary diary)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(DATE_RECORDED, diary.getDateRecorded());
        initialValues.put(ENTRY_NUMBER, diary.getEntryNumber());
        initialValues.put(FILE_URI, diary.getFileURI());
        initialValues.put(SMILE_SCORE, diary.getSmileScore());
        return db.insert(TABLE_NAME, null, initialValues);
    }

    //---deletes a particular contact---
    public boolean deleteDiary(int rowId)
    {
        return db.delete(TABLE_NAME, KEY_ROWID + "=" + rowId, null) > 0;
    }

    //---retrieves all the contacts---
    public ArrayList<Diary> getAllDiaryEntries()
    {
        ArrayList<Diary> diaries = new ArrayList<Diary>();

        Cursor c = db.query(TABLE_NAME, new String[] {KEY_ROWID, DATE_RECORDED,
                ENTRY_NUMBER, FILE_URI, SMILE_SCORE}, null, null, null, null, null);
        if(c.moveToFirst())
        {
            do {
                Diary diary = new Diary();
                diary.setId(c.getInt(0));
                diary.setDateRecorded(c.getString(1));
                diary.setEntryNumber(c.getInt(2));
                diary.setFileURI(c.getString(3));
                diary.setSmileScore(c.getFloat(4));
                diaries.add(diary);
            } while(c.moveToNext());
        }
        return diaries;
    }

    public Cursor getDiaryEntry(long rowId) throws SQLException
    {
        Cursor mCursor =
                db.query(true, TABLE_NAME, new String[] {KEY_ROWID,
                                DATE_RECORDED, ENTRY_NUMBER,FILE_URI}, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public boolean updateDiary(Diary diary)
    {
        ContentValues args = new ContentValues();
        args.put(DATE_RECORDED, diary.getDateRecorded().toString());
        args.put(ENTRY_NUMBER, diary.getEntryNumber());
        args.put(FILE_URI, diary.getFileURI());
        return db.update(TABLE_NAME, args, KEY_ROWID + "=" + diary.getId(), null) > 0;
    }

}
