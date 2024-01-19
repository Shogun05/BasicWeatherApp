package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class SecondActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        //Getting the data from the first page of app
        Bundle extras = getIntent().getExtras();
        String main = extras.getString("main");
        //Setting the parameters to display in the app

        int temp = (int) Double.parseDouble(extras.getString("temp"));
        int min = (int) Double.parseDouble(extras.getString("min"));
        int max = (int) Double.parseDouble(extras.getString("max"));
        int feels_like = (int) Double.parseDouble(extras.getString("feels_like"));

        TextView textView1 = (TextView) findViewById(R.id.textView1);
        TextView textView2 = (TextView) findViewById(R.id.textView2);
        TextView textView3 = (TextView) findViewById(R.id.textView3);
        TextView textView4 = (TextView) findViewById(R.id.textView4);
        TextView textView5 = (TextView) findViewById(R.id.textView5);

        textView1.setText(main); //Since main is going to be altered later, setting it as text early on

        ImageView imageView = (ImageView) findViewById(R.id.imageView2);

        main = main.toLowerCase();
        int resID;

        Calendar calendar = Calendar.getInstance();
        int currentHourIn24Format = calendar.get(Calendar.HOUR_OF_DAY);

        Log.d("DEBUG",String.valueOf(currentHourIn24Format));

        switch(main) {
            case "thunderstorm":resID = R.drawable.thunderstorm;break;
            case "clear": {
                if(currentHourIn24Format>6 && currentHourIn24Format<18) {
                    resID = R.drawable.clear_day;
                }
                else
                    resID = R.drawable.clear_night;
            };break;
            case "clouds": {
                if(currentHourIn24Format>6 && currentHourIn24Format<18){
                    resID = R.drawable.partly_cloudy_day;
                }
                else
                    resID = R.drawable.partly_cloudy_night;
            };break;
            case "rain":resID=R.drawable.rainy;break;
            case "haze":
            case "mist":
            case "smoke":
            case "dust":
            case "fog":
            case "sand":
            case "ash":resID = R.drawable.foggy;break;
            default:resID = R.drawable.clear_day;
        }
        imageView.setImageResource(resID);
        //Displaying the parameters

        textView2.setText("Current Temperature:"+temp);
        textView3.setText("feels like: "+feels_like);
        textView4.setText("min:"+min);
        textView5.setText("max:"+max);


    }
}