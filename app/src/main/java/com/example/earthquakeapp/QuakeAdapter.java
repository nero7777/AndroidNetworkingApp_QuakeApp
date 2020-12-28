package com.example.earthquakeapp;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class QuakeAdapter extends ArrayAdapter<EarthQuake> {
    public QuakeAdapter(Activity context, ArrayList<EarthQuake> earthquakes) {
        super(context, 0, earthquakes);
    }



    //Return the formatted magnitude string  from a (double) magnitude value.
    private String formatMagnitude(double magnitude) {
        DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
        return magnitudeFormat.format(magnitude);
    }

    //Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    //Return the formatted date string (i.e. "4:30 PM") from a Date object.
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }



    private int getMagnitudeColor(double magnitude) {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }





    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        // Get the {@link AndroidFlavor} object located at this position in the list
        EarthQuake currentearthquake = getItem(position);

        //creating object for textview of listitem for list view and setting current item magnitude
        TextView magnitude_textview = (TextView) listItemView.findViewById(R.id.magnitude_textview);
        String formattedMagnitude = formatMagnitude(currentearthquake.getMagnitude());
        magnitude_textview.setText(formattedMagnitude);

        GradientDrawable magnitudeCircle = (GradientDrawable) magnitude_textview.getBackground();
        int magnitudeColor = getMagnitudeColor(currentearthquake.getMagnitude());
        magnitudeCircle.setColor(magnitudeColor);



        //getting the original location to split it in two and display in the below textviews
        String originalLocation = currentearthquake.getLocation();
        String primaryLocation;
        String locationOffset;
        final String LOCATION_SEPARATOR = " of ";

        if (originalLocation.contains(LOCATION_SEPARATOR)) {
            String[] parts = originalLocation.split(LOCATION_SEPARATOR);
            locationOffset = parts[0] + LOCATION_SEPARATOR;
            primaryLocation = parts[1];
        } else {
            locationOffset = getContext().getString(R.string.near_the);
            primaryLocation = originalLocation;
        }




        //creating object for textview1 of listitem for list view and setting current item location1
        TextView offset_textview = (TextView) listItemView.findViewById(R.id.offset_textview);
        offset_textview.setText(locationOffset);

        //creating object for textview2 of listitem for list view and setting current item location2
        TextView primary_textview = (TextView) listItemView.findViewById(R.id.primary_textview);
        primary_textview.setText(primaryLocation);



        // Create a new Date object from the time in milliseconds of the earthquake
        Date dateObject = new Date(currentearthquake.getTime());



        //creating object for textview of listitem for list view and setting current item date
        TextView date_textview = (TextView) listItemView.findViewById(R.id.date_textview);
        String formattedDate = formatDate(dateObject);
        date_textview.setText(formattedDate);

        //creating object for textview of listitem for list view and setting current item time
        TextView time_textview = (TextView) listItemView.findViewById(R.id.time_textview);
        String formattedTime = formatTime(dateObject);
        time_textview.setText(formattedTime);


        return listItemView;
    }
}
