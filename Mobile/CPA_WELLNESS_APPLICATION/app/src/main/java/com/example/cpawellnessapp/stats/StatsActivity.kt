package com.example.cpawellnessapp.stats

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cpawellnessapp.Diary.Diary
import com.example.cpawellnessapp.Diary.DiaryDbAdapter
import com.example.cpawellnessapp.R
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class StatsActivity : AppCompatActivity() {

    private var days_in_a_row:String = ""
    private var average_happiness:String = ""
    private var up_or_down:String = ""
    private var db: DiaryDbAdapter? = null
    private var allEntries = ArrayList<Diary>();
    private val TAG = "OnCreate"

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)

        val diary:Diary = Diary();


        try {
            db = DiaryDbAdapter(this)
            db?.open()


            //allEntries = db!!.getAllDiaryEntries()
            allEntries = db!!.getAllDiaryEntries();
        }
        catch (e: IOException) {
            Log.e(TAG, "Saving received message failed with", e);
        }
        finally {
            db?.close()
        }

        viewManager = LinearLayoutManager(this)
        viewAdapter = MyStatsAdapter(allEntries)

        recyclerView = findViewById<RecyclerView>(R.id.statsRecycler).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter
        }


        printStats()
        getAverage()


    }

    //Get all diary entries and shit saved from them
    //    -Days in a row
    //    -Up or down per week

    //Return
    fun onButtonReturn(view: View) {
        finish()
    }


    //-Average happiness
    fun getAverage(){
        var average:Double = 0.0
        allEntries.forEach {
            average += it.smileScore
        }

        var sometext = allEntries.size.toString() + " entries in the diary.";
        val entry = findViewById<TextView>(R.id.NumberofEntries)
        entry.setText(sometext)

        if(allEntries.size == 0){
            val text = "Average happiness score is 0, please record diary to get data! "
            val edit = findViewById<TextView>(R.id.textAverage)
            edit.setText(text)
            return
        }
        average = average/allEntries.size


        if(average === 0.0 || average.isNaN() == true){
            findViewById<TextView>(R.id.textAverage).apply { text= "Average happiness score is 0, please record diary to get data! " }
        }
        else{
            val score = (average * 100)
            val realAverage:Double = Math.round(score * 1000.0) / 1000.0
            findViewById<TextView>(R.id.textAverage).apply { text= "Average happiness score: " + realAverage.toString() }
        }
    }


    fun printStats(){
        println("Did this work?")
        println(allEntries.size)

        allEntries.forEach {
            println("ID " + it.Id)
            println("Entry Number " + it.entryNumber.toString())
            println("dateRecorded " + it.dateRecorded.toString())
            println("File URI " + it.fileURI.toString())
            println("Smile Score " + it.smileScore.toString())
        }

        //daysInARow();
    }

    fun daysInARow(){

        var daysInROW = 0
        val allDates = ArrayList<Date>()
//        for (i in 0..allEntries.size) {
//            allDates.add(LocalDate.parse(allEntries[i].dateRecorded, DateTimeFormatter.ofPattern("dd-MM-yyyy")))
//        }
//        val list = allEntries.distinctB

        allDates.forEach {
            print(it)
        }
//        val diaries: ArrayList<IDiary>?
//
//        val from = LocalDate.parse("04112005", DateTimeFormatter.ofPattern("ddMMyyyy"))
//        // get today's date
//        val today = LocalDate.now()
//        // calculate the period between those two
//        var period = Period.between(from, today)
//        println("The difference between " + from.format(DateTimeFormatter.ISO_LOCAL_DATE)
//                + " and " + today.format(DateTimeFormatter.ISO_LOCAL_DATE) + " is "
//                + period.getYears() + " years, " + period.getMonths() + " months and "
//                + period.getDays() + " days")
//        println("DURATION:  " + Duration.between(otherDay, today).toDays())

//        val days: Long = diff.toDays()
//        diff = diff.minusDays(days)
//
//        val diff: Duration = Duration.between(today, today)
//        System.out.println(diff)
    }

    fun IsDateBigger(){

    }

    fun populateData(){
        db = DiaryDbAdapter(this)
        db?.open()

        val diary:Diary = Diary();
        diary.dateRecorded = SimpleDateFormat("yyyy/MM/dd").format(incrementDateByOne(Date()));
        diary.entryNumber = 1;
        diary.fileURI = "NA";
        diary.smileScore = 0.058F
        db?.insertDiaryEntry(diary);

        val diary2:Diary = Diary();
        diary2.dateRecorded = SimpleDateFormat("yyyy/MM/dd").format(incrementDateByTwo(Date()));
        diary2.entryNumber = 1;
        diary2.fileURI = "NA";
        diary2.smileScore = 0.078F
        db?.insertDiaryEntry(diary2);

//        val diary3:Diary = Diary();
//        diary3.dateRecorded = SimpleDateFormat("yyyy/MM/dd").format(incrementDateByThree(Date()));
//        diary3.entryNumber = 1;
//        diary3.fileURI = "NA";
//        diary3.smileScore = 0.098F
//        db?.insertDiaryEntry(diary3);

        db?.close()
    }

    fun incrementDateByOne(date: Date?): Date? {
        val c = Calendar.getInstance()
        c.time = date
        c.add(Calendar.DATE, 1)
        return c.time
    }
    fun incrementDateByTwo(date: Date?): Date? {
        val c = Calendar.getInstance()
        c.time = date
        c.add(Calendar.DATE, 2)
        return c.time
    }
    fun incrementDateByThree(date: Date?): Date? {
        val c = Calendar.getInstance()
        c.time = date
        c.add(Calendar.DATE, 3)
        return c.time
    }
}