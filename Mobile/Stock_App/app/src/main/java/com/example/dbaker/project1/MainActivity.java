package com.example.dbaker.project1;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    View background;
    LinearLayout mainLayout;
    TextView TotalGainInDollar, PercentGain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TotalGainInDollar = findViewById(R.id.MoneyGain);
        PercentGain = findViewById(R.id.PercentGain);
        mainLayout= findViewById(R.id.linearLayout);
        background  = findViewById(R.id.background);
        doCalculations();
    }

    public void doCalculations(){

        try{
            FileInputStream fins = openFileInput("test.txt");
            Scanner input = new Scanner(fins);

            int i =0;
            int countTimes = 0;

            double totalGainOrLoss =0;
            double totalMoney = 0;
            double totalPercent = 0;
            double tempBuy = 0;
            double tempSell = 0;
            double NumShares = 0;
            String temp = "";

            while(input.hasNextLine()){



                i++;
                if(i == 3){
                    String text = input.nextLine();
                    NumShares = Double.parseDouble(text);
                }
                else if(i == 4){
                    String text = input.nextLine();
                    tempBuy = Double.parseDouble(text);
                }
                else if(i == 7){
                    String text = input.nextLine();
                    tempSell = Double.parseDouble(text);
                }else{
                    temp = input.nextLine();
                }

                if(i == 8 ){
                    i =0;
                    countTimes++;
                    if(NumShares > 0 && tempBuy > 0 && tempSell > 0){
                        totalGainOrLoss += (tempSell - tempBuy) * (double)NumShares;
                        totalMoney+= tempBuy * NumShares;
                    }
                }

            }

            String gains= String.valueOf("$"+totalGainOrLoss);
            TotalGainInDollar.setText(gains);

            double tempGainNegative = 0;
            if(totalGainOrLoss < 0){
                tempGainNegative = totalGainOrLoss * -1;
            }else{
                tempGainNegative = totalGainOrLoss;
            }

            totalPercent = ((tempGainNegative - totalMoney) / totalMoney) *100 ;
            int test = (int)totalPercent;
            String percent= String.valueOf(test+ "%");
            PercentGain.setText(percent);

            if(totalGainOrLoss > 0){
                TotalGainInDollar.setTextColor(Color.parseColor("#15ad15"));
                PercentGain.setTextColor(Color.parseColor("#15ad15"));
                background.setBackgroundColor(Color.parseColor("#226110"));
                TotalGainInDollar.setText("r");
            }else if(totalGainOrLoss < 0){
                TotalGainInDollar.setTextColor(Color.parseColor("#f01e13"));
                PercentGain.setTextColor(Color.parseColor("#f01e13"));
                background.setBackgroundColor(Color.parseColor("#991412"));
            }else{

            }

        }catch(IOException ioe){
            ioe.printStackTrace();
        }
    }

    public void onButtonRead(View view) {
        Intent intent = new Intent(this,Settings.class);
        startActivity(intent);
        doCalculations();
    }

    public void onButtonAdd(View view) {
        Intent intent2 = new Intent(this,NewStock.class);
        startActivity(intent2);
        doCalculations();
    }

    public void onButtonView(View view) {
        Intent intent3 = new Intent(this,EditViewStock.class);
        startActivity(intent3);
        doCalculations();
    }
}
