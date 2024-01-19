package com.example.myapplication.weather;

import android.util.Log;

import com.example.myapplication.MainActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import org.json.JSONArray;
import org.json.JSONObject;

public class Weather {

    private String city, state, country;
    private String API_key = "";

    public Weather(String city) {
        this.city = city;
        this.state = "Karnataka";
        this.country = "India";
    }

    public Weather(String city, String state) {
        this.city = city;
        this.state = state;
        this.country = "India";
    }

    public Weather(String city, String state, String country) {
        this.city = city;
        this.state = state;
        this.country = country;
    }


    public double[] process() {
        int limit = 1;
        try {
            URL url = new URL("http://api.openweathermap.org/geo/1.0/direct?q=" + city + "," + state + "," + country + "&appid=" + API_key);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.getInputStream();

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            in.close();
            con.disconnect();

            JSONArray json = new JSONArray(content.toString());
            try {
                double[] values = {json.getJSONObject(0).getDouble("lat"), json.getJSONObject(0).getDouble("lon")};
                return values;
            } catch (org.json.JSONException jsonException) {
                Log.d("DEBUG", "Key lat, long not found in the json" + json.toString());
            }
        } catch (Exception e) {
            Log.d("DEBUG", String.valueOf(e));
        }
        Log.d("DEBUG", "Something failed, returning default values");
        double[] values = {12.9768D, 77.5901D}; //latitude and longitude of bengaluru
        return values;
    }

    public List<String> getWeather(double values[]) {
        double lat = values[0];
        double longi = values[1];
        List<String> newValues = new ArrayList<String>(Arrays.asList("", "", "", "", ""));
        try {
            URL url = new URL("https://api.openweathermap.org/data/3.0/onecall?lat=" + lat + "&lon=" + longi + "&appid=" + API_key);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();

            JSONObject json = new JSONObject(content.toString());
            try {
                JSONObject weatherJSON = json.getJSONObject("current").getJSONArray("weather").getJSONObject(0);
                newValues.set(4, weatherJSON.getString("main"));

                JSONObject currentJSON = json.getJSONObject("current");

                newValues.set(0, roundOf(String.valueOf(currentJSON.getDouble("temp") - 273.15)));
                newValues.set(1, roundOf(String.valueOf(currentJSON.getDouble("feels_like") - 273.15)));

                JSONObject daily = json.getJSONArray("daily").getJSONObject(0).getJSONObject("temp");

                newValues.set(2, roundOf(String.valueOf(daily.getDouble("min") - 273.15)));
                newValues.set(3, roundOf(String.valueOf(daily.getDouble("max") - 273.15)));

                return newValues;

            } catch (org.json.JSONException jsonException) {
                Log.d("DEBUG", jsonException.toString());
            }
        } catch (Exception e) {
            Log.d("DEBUG", String.valueOf(e));
        }
        newValues.set(0, "0");
        newValues.set(1, "0");
        newValues.set(2, "0");
        newValues.set(3, "0");
        newValues.set(4, "0");
        //Set default values to prevent crashing of the app
        return newValues;
    }

    private String roundOf(String input){
        double doubleInput = Double.parseDouble(input);
        if(((int)doubleInput)+0.5 > doubleInput)
            return String.valueOf(doubleInput);
        return String.valueOf(doubleInput+1);
    }
}