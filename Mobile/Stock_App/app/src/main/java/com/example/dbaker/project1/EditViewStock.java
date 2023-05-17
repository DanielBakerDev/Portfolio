package com.example.dbaker.project1;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class EditViewStock extends AppCompatActivity {

    View background;
    TextView multiLine, editPrice, editDate;
    Spinner spinnerNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_view_stock);
        multiLine = findViewById(R.id.ShowAllStocks);
        spinnerNum = findViewById(R.id.spinner2);
        editPrice = findViewById(R.id.AddClosePrice);
        editDate = findViewById(R.id.EditCloseDate);
        background = findViewById(R.id.background);
        setBackgroundColor();
        ReadIN();
    }

    String[][] twoD_arr = new String[0][0];
    int ReadInCounter = 0;
    ArrayList<Integer> numArray = new ArrayList<Integer>();

    public void setBackgroundColor(){
        background.setBackgroundColor(Color.parseColor("#edd11f"));
    }

    public void ReadIN() {
        String [] wordsArray = {"Name: ", "Ticker: ", "Shares: ", "Price Bought: ", "Listing: ", "Date Opened: ", "Price Closed: ", "Date Closed: "};
        try{
            String line = "";
            FileInputStream fin = openFileInput("test.txt");
            Scanner input = new Scanner(fin);

            int i = 0;

            while(input.hasNextLine()){
                line += wordsArray[i] + input.nextLine() + "\n";
                i++;
                if(i == 8 ){
                    i =0;
                    line += "\n";
                    ReadInCounter++;
                }
            }

            multiLine.setText(line);
            fin.close();
            populateNumArray();

        } catch(IOException ioe){
            ioe.printStackTrace();
        }
    }

    public void DeleteLastStock(View view) {
            //Read in
        String line = "";
        String Other ="";
        try{
            FileInputStream fins = openFileInput("test.txt");
            Scanner input = new Scanner(fins);

            int i = 0;
            int CountAdded = 0;

            while(input.hasNextLine()){
                if(CountAdded != ReadInCounter-1){
                    line +=  input.nextLine() + "\n";
                    i++;
                    if(i == 8 ){
                        i =0;
                        CountAdded++;
                    }
                }else{
                    Other +=  input.nextLine() + "\n";                }
            }

            fins.close();

        } catch(IOException ioe){
            ioe.printStackTrace();
        }

        //Read Out
        try{
            FileOutputStream ofile = openFileOutput("test.txt",MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(ofile);

            osw.write(line);

            osw.flush();
            osw.close();


            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("Stock has been Deleted");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Close",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });


            AlertDialog alert11 = builder1.create();
            alert11.show();
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
        ReadInCounter = 0;
        ReadIN();
    }

    public void populateNumArray(){
        numArray.clear();
            for(int i = 0; i < ReadInCounter; i++){
                numArray.add(i+1);
            }

        if(ReadInCounter != 0){
            ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item, numArray);
            spinnerNum.setAdapter(adapter);
        }

    }

    public void UpdateStock(View view) {
        String text = spinnerNum.getSelectedItem().toString();
        Integer result = Integer.valueOf(text);
        result -= 1;
        String line ="";
        String linenotUsed ="";

        int i =0;
        int countTimes =0;

        try{
            FileInputStream fins = openFileInput("test.txt");
            Scanner input = new Scanner(fins);

            while(input.hasNextLine()){
                if(countTimes == result && i == 6){
                    linenotUsed = input.nextLine();
                    line += editPrice.getText().toString() + "\n";
                }else if(countTimes == result && i ==7){
                    linenotUsed = input.nextLine();
                    line += editDate.getText().toString() + "\n";
                }else{
                    line +=  input.nextLine() + "\n";
                }
                i++;
                if(i == 8 ){
                    i =0;
                    countTimes++;
                }

            }

        }catch(IOException ioe){
        ioe.printStackTrace();
        }

        //Read Out
        try{
            FileOutputStream ofile = openFileOutput("test.txt",MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(ofile);

            osw.write(line);

            osw.flush();
            osw.close();


            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("Stock has been Updated!");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Close",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });


            AlertDialog alert11 = builder1.create();
            alert11.show();
        }catch(IOException ioe){
            ioe.printStackTrace();
        }

        editPrice.setText("");
        editDate.setText("");
        ReadInCounter = 0;
        ReadIN();

    }


    public void CloseAddStock(View view) {
        finish();
    }


}
