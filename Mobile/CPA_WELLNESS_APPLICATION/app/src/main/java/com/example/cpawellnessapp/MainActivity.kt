package com.example.cpawellnessapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.cpawellnessapp.Diary.DiaryDbAdapter
import com.example.cpawellnessapp.stats.StatsActivity
import java.io.*

class MainActivity : AppCompatActivity() {

    // Storage Permissions

    private var db: DiaryDbAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initializeDB()
        validatePermissions ()
        setContentView(R.layout.activity_main)
    }

    // copyDB to copy assets to phone
    @Throws(IOException::class)
    fun CopyDB(inputStream: InputStream, outputStream: OutputStream) {
        //Copy one byte at a time
        val buffer = ByteArray(1024)
        var length: Int
        while (inputStream.read(buffer).also { length = it } > 0) {
            outputStream.write(buffer, 0, length)
        }
        inputStream.close()
        outputStream.close()
    }


    fun validatePermissions () {
        val PERMISSIONS = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA
        );

        var permissionsNeeded = arrayOf<String>();
        //Find all permissions the app does not have
        for (permission in PERMISSIONS) {
            val allow = ContextCompat.checkSelfPermission(this, permission)
            if(allow != PackageManager.PERMISSION_GRANTED) {
                permissionsNeeded += permission

            }
        }
        if(permissionsNeeded.size > 0) {
            ActivityCompat.requestPermissions(
                this,
                permissionsNeeded,
                PackageManager.PERMISSION_GRANTED
            )
        }

    }

    fun onButtonActivity(view: View) {
        when (view.id) {
            R.id.buttonHelp -> {
                startActivity(Intent(this, HelpActivity::class.java))
            }
            R.id.buttonRecord -> {
                startActivity(Intent(this, DiaryMenu::class.java))
            }
            R.id.buttonSettings -> {
                val intent = Intent(this, Settings::class.java)
                startActivity(intent)
            }
            R.id.buttonStats -> {
                val intent = Intent(this, StatsActivity::class.java)
                startActivity(intent)
            }

        }
    }

    fun onButtonSettingsActivity(view: View) {
        when (view.id) {
            R.id.buttonSettings -> {
                val intent = Intent(this, Settings::class.java)
//                val editText = findViewById<EditText>(R.id.editTextNumber)
//                intent.putExtra("edit", editText.text.toString())
                startActivity(intent)
            }

        }
    }

    fun initializeDB() {
        db = DiaryDbAdapter(this)
       // names = ArrayList<String>()
        // get the existing database file or from the assets folder if doesn't exist
        // get the existing database file or from the assets folder if doesn't exist
        try {
            val destPath = "data/data/$packageName/databases"
            val f = File(destPath)
            if (!f.exists()) {
                f.mkdirs()
                f.createNewFile()
                //copy db from assets folder
                CopyDB(baseContext.assets.open("mydb"),
                        FileOutputStream("$destPath/MyDB"))
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}