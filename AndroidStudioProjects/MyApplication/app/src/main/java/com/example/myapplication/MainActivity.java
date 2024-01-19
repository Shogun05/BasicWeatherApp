package com.example.myapplication;

//android packages that are needed
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

//Our own weather package
import com.example.myapplication.weather.Weather;

//Searchview package
import com.miguelcatalan.materialsearchview.MaterialSearchView;

//packages used for defining arrays
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //declaring searchview as private by using access modifier
    private MaterialSearchView searchview;
    private List<String> finalValues;
    @Override
    //This method is called whenever the app is opened
    protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);

                //Setting the main page with toolbar and search button
                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);

                MaterialSearchView searchview = (MaterialSearchView) findViewById(R.id.searchview);
                searchview.setVoiceSearch(false);
                searchview.setVoiceSearch(false);
                searchview.setCursorDrawable(R.drawable.custom_cursor);
                searchview.setEllipsize(true);
                searchview.setSuggestions(getResources().getStringArray(R.array.query_suggestions), true);
                searchview.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
                    @Override
                    //When you enter your city name and hit the search button
                    public boolean onQueryTextSubmit(String query) {
                        Log.d("DEBUG", "Query: " + query);

                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //Creating the weather object from the package that i made and imported
                                Weather w = new Weather(query);
                                //getting latitude and longitude data
                                double values[] = w.process();
                                List<String> finalValues = w.getWeather(values);
                                MainActivity.this.finalValues = finalValues;
                            }
                        });
                        thread.start();

                        try{
                            thread.join();
                        }catch(InterruptedException e){
                            Log.d("DEBUG","Interrupted main thread");
                        }
                        //Storing the required values like temp, min, max in a List


                        //Sending the data to the second page of app
                        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                        intent.putExtra("temp", finalValues.get(0));
                        intent.putExtra("feels_like", finalValues.get(1));
                        intent.putExtra("min", finalValues.get(2));
                        intent.putExtra("max", finalValues.get(3));
                        intent.putExtra("main", finalValues.get(4));
                        startActivity(intent);
                        return false;
                    }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });

        searchview.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MaterialSearchView searchview = (MaterialSearchView) findViewById(R.id.searchview);

        MenuItem item = (MenuItem) menu.findItem(R.id.action_search);
        searchview.setMenuItem(item);

        return true;
    }

    @Override
    public void onBackPressed() {
        MaterialSearchView searchview = (MaterialSearchView) findViewById(R.id.searchview);
        if (searchview.isSearchOpen()) {
            searchview.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    searchview.setQuery(searchWrd, false);
                }
            }

            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
