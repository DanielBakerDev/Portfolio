package com.example.cpawellnessapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Switch
import android.widget.TextView
import com.example.cpawellnessapp.Diary.Diary
import com.example.cpawellnessapp.Diary.DiaryDbAdapter
import com.example.cpawellnessapp.diary_questions.Questions
import com.example.cpawellnessapp.diary_questions.QuestionsDbAdapter
import java.io.IOException
import java.util.ArrayList

class Settings : AppCompatActivity() {

    val context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
    }

    fun onButtonReturn(view: View) {
        finish()
    }

    fun onReset(view: View){

        var db = DiaryDbAdapter(context)
        //Just re create dbs
        try {
            db = DiaryDbAdapter(this)
            db?.open()

            var allEntries = ArrayList<Diary>();
            allEntries = db!!.getAllDiaryEntries()

            allEntries.forEach {
                db.deleteDiary(it.Id)
            }

        }
        catch (e: IOException) {
            println("Error")
        }
        finally {
            db?.close()
        }


        //Set switch to off
        val onOffSwitch = findViewById<Switch>(R.id.switch1)
        onOffSwitch.isChecked = false

        //Change text to reset
        val textViewReset = findViewById<TextView>(R.id.textNotification)
        textViewReset.text = "Data reset!"

        Animate(R.animator.translate)
    }

    private fun Animate(animationResourceID: Int) {
        val text: TextView = findViewById<TextView>(R.id.textNotification) as TextView

        // Load the appropriate animation
        val an: Animation = AnimationUtils.loadAnimation(this, animationResourceID)
        // set up listener, so that you know states of the animation
        an.setAnimationListener(MyAnimationListener())
        // start the animation
        text.startAnimation(an)
    }

    inner class MyAnimationListener : Animation.AnimationListener {
        override fun onAnimationEnd(animation: Animation) {

        }

        override fun onAnimationRepeat(animation: Animation) {}
        override fun onAnimationStart(animation: Animation) {}
    }
}