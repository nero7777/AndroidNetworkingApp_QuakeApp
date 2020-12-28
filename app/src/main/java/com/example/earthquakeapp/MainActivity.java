package com.example.earthquakeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";

    /** Adapter for the list of earthquakes */
    private QuakeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            EarthquakeAsyncTask task = new EarthquakeAsyncTask();

            task.execute(USGS_REQUEST_URL);

            ListView earthquakeListView = (ListView) findViewById(R.id.list);

            mAdapter = new QuakeAdapter(this, new ArrayList<EarthQuake>());

            earthquakeListView.setAdapter(mAdapter);


            earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // Find the current earthquake that was clicked on
                    EarthQuake currentEarthquake = mAdapter.getItem(position);

                    // Convert the String URL into a URI object (to pass into the Intent constructor)
                    Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());

                    // Create a new intent to view the earthquake URI
                    Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                    // Send the intent to launch a new activity
                    startActivity(websiteIntent);
                }

            });
        }




        private class EarthquakeAsyncTask extends AsyncTask<String,Void, List<EarthQuake>> {

            @Override
            protected List<EarthQuake> doInBackground(String... urls) {
                // Don't perform the request if there are no URLs, or the first URL is null.
                if (urls.length < 1 || urls[0] == null) {
                    return null;
                }

                List<EarthQuake> result = null;
                try {
                    result = QueryUtils.fetchEarthquakeData(urls[0]);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                return result;
            }
            @Override
            protected void onPostExecute(List<EarthQuake> data) {
                mAdapter.clear();

                if (data != null && !data.isEmpty()) {
                    mAdapter.addAll(data);
                }
            }
        }
    }
