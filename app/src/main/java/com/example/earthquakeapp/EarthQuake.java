package com.example.earthquakeapp;

public class EarthQuake {
    private double mmagnitude;
    private String mlocation;
    private long mtime;
    private String murl;

    EarthQuake(double magnitude,String location,long time,String url){
        this.mmagnitude = magnitude;
        this.mlocation  = location;
        this.mtime = time;
        this.murl = url;
    }

    public double getMagnitude(){
        return mmagnitude;
    }

    public String getLocation(){
        return mlocation;
    }

    public Long getTime(){ return mtime; }
    public String getUrl(){ return murl; }

}
