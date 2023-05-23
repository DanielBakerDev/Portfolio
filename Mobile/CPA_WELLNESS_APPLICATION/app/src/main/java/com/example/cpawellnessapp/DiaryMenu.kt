package com.example.cpawellnessapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class DiaryMenu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary_menu)
    }

    fun onButtonActivity(view: View) {
        when (view.id) {
            R.id.buttonRecordDiary -> {
                startActivity(Intent(this, DiaryRecord::class.java))
            }
            R.id.buttonViewDiaries -> {
                startActivity(Intent(this, DiaryList::class.java))
            }
        }
    }

    fun onButtonReturn(view: View) {
        finish()
    }
}