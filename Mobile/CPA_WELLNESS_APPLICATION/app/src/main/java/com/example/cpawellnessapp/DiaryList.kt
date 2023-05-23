package com.example.cpawellnessapp

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cpawellnessapp.Diary.Diary
import com.example.cpawellnessapp.Diary.DiaryDbAdapter
import com.example.cpawellnessapp.Diary.DiaryRecyclerAdapter
import java.io.File
import java.lang.Exception

class DiaryList : AppCompatActivity() {
    companion object {
        var diaries = ArrayList<Diary>()
        var width: Int = 150
        lateinit var mediaDir: Array<File>
        lateinit var diaryListContext: Context
    }
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary_list)
        mediaDir = externalMediaDirs
        diaryListContext = this
        val db = DiaryDbAdapter(this)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        width = displayMetrics.widthPixels

        try{
            db.open()
            diaries = db.getAllDiaryEntries();
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
        finally {
            db.close()
        }
        viewManager = LinearLayoutManager(this)
        viewAdapter = DiaryRecyclerAdapter(diaries)

        recyclerView = findViewById<RecyclerView>(R.id.recycler_view).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter

        }
    }

    fun onButtonReturn(view: View) {
        finish()
    }
}