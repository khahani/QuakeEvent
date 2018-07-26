package com.khahani.app.quakeevent;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Event event = NetworkUtil.fetchData("https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2016-01-01&endtime=2016-05-02&minfelt=50&minmagnitude=5");

        new EventAsyncTask().execute("https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2016-01-01&endtime=2016-05-02&minfelt=50&minmagnitude=5");


        loading = findViewById(R.id.pb_loading);
        loading.setVisibility(View.INVISIBLE);
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

            loading.setVisibility(View.INVISIBLE);

            TextView magTextView = findViewById(R.id.tv_mag);
            TextView feltTextView = findViewById(R.id.tv_felt);

            magTextView.setText(Double.toString(event.getMag()));
            feltTextView.setText(Integer.toString(event.getFelt()));
        }
    }
}
