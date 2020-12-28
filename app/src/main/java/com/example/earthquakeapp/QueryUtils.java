package com.example.earthquakeapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {
    //A default constructor for this class
    private QueryUtils() {
    }


    //Creating url with the request url ie a constant string above this methods converts the string into a url and returns it back
    //which in turn is parameter for makehttpreq method
    private static URL createUrl(String stringUrl){
        URL url = null;

        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {

        //Creating a empty string to store response from the api
        String  jsonResponse = "";

        //returning the empty string if url is null
        if(url == null){
            return jsonResponse;
        }

        //Creating a HTTPURLConnection object and inputstream object which will be used to set up a connection
        //and then read the response respectively
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try{

            //using url object to setup a url connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //if we get a correct status code start parsing the response using the input stream object
            // and calling the readfromStream method
            if(urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }else{
                //else we log the message error response code
                Log.e("LOG_TAG", "Error response code: " + urlConnection.getResponseCode());
            }
        }catch (IOException e){
            Log.e("LOG_TAG", "Problem retrieving the earthquake JSON results.", e);
        }finally {
            if (urlConnection != null) {
                //finally after getting response if its not null then we close the urlconnection
                // by calling the disconnect method on urlconnection object
                urlConnection.disconnect();
            }

            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }

        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException{
        //we create a stringBuilder object to hold the response as it gives
        // flexibility to gradually build up a string over diffrent pieces
        StringBuilder output = new StringBuilder();

        if(inputStream != null){
            //we create a inputStreamReader object to read from inputStream and
            // wrap it around a buffered reade which reade bit by bit
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();

            //running a while loop to read the whole response
            while(line != null){
                output.append(line);
                line = reader.readLine();
            }
        }

        //returning the output by convering it to string again
        return output.toString();
    }


    private static List<EarthQuake> extractFeatureFromJson(String earthquakeJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(earthquakeJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding earthquakes to
        List<EarthQuake> earthquakes = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(earthquakeJSON);

            // Extract the JSONArray associated with the key called "features",
            // which represents a list of features (or earthquakes).
            JSONArray earthquakeArray = baseJsonResponse.getJSONArray("features");

            // For each earthquake in the earthquakeArray, create an {@link Earthquake} object
            for (int i = 0; i < earthquakeArray.length(); i++) {

                // Get a single earthquake at position i within the list of earthquakes
                JSONObject currentEarthquake = earthquakeArray.getJSONObject(i);

                // For a given earthquake, extract the JSONObject associated with the
                // key called "properties", which represents a list of all properties
                // for that earthquake.
                JSONObject properties = currentEarthquake.getJSONObject("properties");

                // Extract the value for the key called "mag"
                double magnitude = properties.getDouble("mag");

                // Extract the value for the key called "place"
                String location = properties.getString("place");

                // Extract the value for the key called "time"
                long time = properties.getLong("time");

                // Extract the value for the key called "url"
                String url = properties.getString("url");

                // Create a new {@link Earthquake} object with the magnitude, location, time,
                // and url from the JSON response.
                EarthQuake earthquake = new EarthQuake(magnitude, location, time, url);

                // Add the new {@link Earthquake} to the list of earthquakes.
                earthquakes.add(earthquake);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }

    public static List<EarthQuake> fetchEarthquakeData(String requestUrl) throws MalformedURLException {
        // Create URL object
        URL url =  createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e("LOG_TAG", "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Earthquake}s
        List<EarthQuake> earthquakes = extractFeatureFromJson(jsonResponse);



        //finally returning the earthquakes list
        return earthquakes;
    }
}
