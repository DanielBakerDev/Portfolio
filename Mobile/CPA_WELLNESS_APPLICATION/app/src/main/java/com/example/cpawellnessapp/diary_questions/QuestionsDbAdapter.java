package com.example.cpawellnessapp.diary_questions;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class QuestionsDbAdapter {
    static final String KEY_ROWID = "_id";
    //Columns
    static final String QUESTION = "question";

    static final String TAG = "QuestionDBAdapter";

    static final String DATABASE_NAME = "CPAWellness2";
    //Tables
    static final String TABLE_NAME = "questionEntries";
    static final int DATABASE_VERSION = 5;

    static final String DATABASE_CREATE =
            "create table "+TABLE_NAME+" (_id integer primary key autoincrement, "
                    + QUESTION + " string not null);";

    static Context context;

    static QuestionsDbAdapter.DatabaseHelper DBHelper;
    static SQLiteDatabase db;

    public  QuestionsDbAdapter(Context ctx)
    {
        this.context = ctx;
        DBHelper = new QuestionsDbAdapter.DatabaseHelper(context);
    }

    public int getNextEntryNumber(@NotNull String dateRecorded) {
        int lastEntry = 0;
        ArrayList<Questions> diaries = getAllQuestionEntries();
        for(Questions question : diaries){
//            if(dateRecorded == diary.dateRecorded) {
//                if(lastEntry < diary.getEntryNumber()) {
//                    lastEntry = diary.getEntryNumber();
//                }
//            }
        };
        return ++lastEntry;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
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
    public QuestionsDbAdapter open() throws SQLException
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
    public long insertQuestionEntry(Questions question)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(QUESTION, question.getQuestion());
        return db.insert(TABLE_NAME, null, initialValues);
    }

    //---deletes a particular contact---
    public boolean deleteQuestion(int rowId)
    {
        return db.delete(TABLE_NAME, KEY_ROWID + "=" + rowId, null) > 0;
    }

    //---retrieves all the contacts---
    public ArrayList<Questions> getAllQuestionEntries()
    {
        ArrayList<Questions> diaries = new ArrayList<Questions>();

        Cursor c = db.query(TABLE_NAME, new String[] {KEY_ROWID, QUESTION}, null, null, null, null, null);
        if(c.moveToFirst())
        {
            do {
                Questions question = new Questions();
                question.setId(c.getInt(0));
                question.setQuestion(c.getString(1));
                diaries.add(question);
            } while(c.moveToNext());
        }
        return diaries;
    }


    public Cursor getQuestionEntry(long rowId) throws SQLException
    {
        Cursor mCursor =
                db.query(true, TABLE_NAME, new String[] {KEY_ROWID, QUESTION}, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public boolean updateQuestion(Questions question)
    {
        ContentValues args = new ContentValues();
        args.put(QUESTION, question.getQuestion());
        return db.update(TABLE_NAME, args, KEY_ROWID + "=" + question.getId(), null) > 0;
    }
}
