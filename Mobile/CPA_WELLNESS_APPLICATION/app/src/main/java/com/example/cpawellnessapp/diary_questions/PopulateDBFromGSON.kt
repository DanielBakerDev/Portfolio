package com.example.cpawellnessapp.diary_questions

import android.content.Context
import com.example.cpawellnessapp.Diary.Diary
import com.example.cpawellnessapp.Diary.DiaryDbAdapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class PopulateDBFromGSON {

    fun getJSONDataFromAsset(context: Context, filename:String):String? {
        val jsonString:String
        try {
            // Use bufferedReader
            // Closable.use will automatically close the input at the end of execution
            // it.reader.readText()  is automatically
            jsonString = context.assets.open(filename).bufferedReader().use {
                it.readText() }
        } catch(ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }

    fun ok(context: Context){
        var people:List<Questions>
        val jsonFileString = getJSONDataFromAsset(context,"questions.json")
        //TypeToken to get the type of the object
        val listQuestionType = object: TypeToken<List<Questions>>() {}.type
        people = Gson().fromJson(jsonFileString, listQuestionType)

        var db: QuestionsDbAdapter? = null
        db = QuestionsDbAdapter(context)
        db?.open()

        people.forEach {
            val questions: Questions = Questions();
            questions.question = it.question
            db?.insertQuestionEntry(questions);
        }


        db.close()
    }
}