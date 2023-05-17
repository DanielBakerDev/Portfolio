package com.example.dbaker.project1;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NewStock extends AppCompatActivity {

    View background;
    EditText addTextName, addTextTicker, addTextPrice, addTextNumShares;
    RadioButton radbtn1, radbtn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_stock);
        //editTextName = findViewById(R.id.editTextName);
        addTextName = findViewById(R.id.addTextName);
        addTextTicker = findViewById(R.id.addTextTicker);
        addTextPrice = findViewById(R.id.addTextPrice);
        addTextNumShares = findViewById(R.id.addTextNumShares);
        radbtn1 = findViewById(R.id.Nasdaq);
        radbtn2 = findViewById(R.id.Nyse);
        background = findViewById(R.id.background);
        setBackgroundColor();
    }

    private String pattern = "dd-MM-yyyy";
    String dateInString =new SimpleDateFormat(pattern).format(new Date());

    public void setBackgroundColor(){
        background.setBackgroundColor(Color.parseColor("#edd11f"));
    }

    public void AddNewStock(View view) {
        try
        {
            // to save to file "test.txt" in data/data/packagename/File
            FileOutputStream ofile = openFileOutput("test.txt",MODE_APPEND);
            OutputStreamWriter osw = new OutputStreamWriter(ofile);

            String value= addTextPrice.getText().toString();
            double sharePrice=Integer.parseInt(value);

            String value2 = addTextNumShares.getText().toString();
            int numShares=Integer.parseInt(value);

            if(addTextName.getText().toString().matches("") && addTextTicker.getText().toString().matches("") && sharePrice <= 0 && numShares <= 0 && (!radbtn1.isChecked() || !radbtn2.isChecked())) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setMessage("One of the required inputs is Null, please enter data!");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }else{
                osw.write(addTextName.getText().toString());
                osw.append("\n");
                osw.write(addTextTicker.getText().toString());
                osw.append("\n");
                osw.write(addTextPrice.getText().toString());
                osw.append("\n");
                osw.write(addTextNumShares.getText().toString());
                osw.append("\n");
                if(radbtn1.isChecked() ){
                    osw.write("NYSE");
                    osw.append("\n");
                }else{
                    osw.write("NASDAQ");
                    osw.append("\n");
                }
                osw.write(dateInString.toString());
                osw.append("\n");
                osw.write("0");
                osw.append("\n");
                osw.write("0");
                osw.append("\n");
            }

            osw.flush();
            osw.close();

            reset();
        }
        catch(IOException ioe)
        {
            ioe.printStackTrace();
        }
    }

    public void reset(){
        addTextName.setText("");
        addTextTicker.setText("");
        addTextPrice.setText("");
        addTextNumShares.setText("");
        radbtn1.setChecked(false);
        radbtn2.setChecked(false);

        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Stock has been added!");
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
    }

    public void switchRadio(View view) {
        if(radbtn1.isChecked() ) {
            radbtn2.setChecked(false);
        }else{
            radbtn1.setChecked(false);
        }
    }

    public void CloseAddStock(View view) {
        finish();
    }
}
