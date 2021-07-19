package com.example.quakereport;

public class Quake {
    private double magnitude;
    private String country,date,time,direction,url;
    public Quake(double magnitude,String country,String date, String time, String direction, String url)
    {
       this.country=country;
       this.date=date;
       this.magnitude=magnitude;
       this.time=time;
       this.direction=direction;
       this.url=url;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public String getCountry() {
        return country;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getDirection() {
        return direction;
    }

    public String getUrl() {
        return url;
    }
}
