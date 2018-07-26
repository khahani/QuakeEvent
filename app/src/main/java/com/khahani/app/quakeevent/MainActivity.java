package com.khahani.app.quakeevent;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Event eventValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Event event = NetworkUtil.fetchData("https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2016-01-01&endtime=2016-05-02&minfelt=50&minmagnitude=5");

        new EventAsyncTask().execute("https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2016-01-01&endtime=2016-05-02&minfelt=50&minmagnitude=5");


        Button visit = findViewById(R.id.bt_visit);
        visit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWebPage(eventValue.getSiteUrl());
            }
        });

    }

    public void openWebPage(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public class EventAsyncTask extends AsyncTask<String, Void, Event> {

        @Override
        protected void onPreExecute() {
            TextView magTextView = findViewById(R.id.tv_mag);
            TextView feltTextView = findViewById(R.id.tv_felt);

            magTextView.setText("Loading...");
            feltTextView.setText("Loading...");
        }

        @Override
        protected Event doInBackground(String... urls) {

            String url = urls[0];

            return NetworkUtil.fetchData(url);
        }

        @Override
        protected void onPostExecute(Event event) {

            eventValue = event;

            TextView magTextView = findViewById(R.id.tv_mag);
            TextView feltTextView = findViewById(R.id.tv_felt);

            magTextView.setText(Double.toString(event.getMag()));
            feltTextView.setText(Integer.toString(event.getFelt()));
        }
    }
}
