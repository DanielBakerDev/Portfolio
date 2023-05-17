package com.example.dbaker.project1;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;

public class Settings extends AppCompatActivity {

    View background;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        background = findViewById(R.id.background);
        setBackgroundColor();
    }


    public void setBackgroundColor(){
        background.setBackgroundColor(Color.parseColor("#edd11f"));
    }

    public void onButtonClose(View view) {
        finish();
    }


    public void Cleartext(View view) {
        try{
            FileOutputStream ofile = openFileOutput("test.txt",MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(ofile);
            osw.write("");
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
    }
}
