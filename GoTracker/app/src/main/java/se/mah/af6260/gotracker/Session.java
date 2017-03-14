package se.mah.af6260.gotracker;

import com.google.android.gms.maps.model.LatLng;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

/**
 * Created by Koffe on 2017-03-01.
 */

public class Session {

    private String activityType;
    private int steps;
    private double avgSpeed;
    private int startYear, startMonth, startDay;
    private String startTime, duration, distance;
    private ArrayList<LatLng> routeArray;
    private int id;

    public Session(String activityType, int startYear, int startMonth, int startDay, String startTime, String duration, String distance, int steps, double avgSpeed, ArrayList<LatLng> routeArray) {
        this.activityType = activityType;
        this.startYear = startYear;
        this.startMonth = startMonth;
        this.startDay = startDay;
        this.startTime = startTime;
        this.duration = duration;
        this.distance = distance;
        this.steps = steps;
        this.avgSpeed = avgSpeed;
        this.routeArray = routeArray;
    }

    public Session(int id, String activityType, int startYear, int startMonth, int startDay, String startTime, String duration, String distance, int steps, double avgSpeed, ArrayList<LatLng> routeArray) {
        this.id = id;
        this.activityType = activityType;
        this.startYear = startYear;
        this.startMonth = startMonth;
        this.startDay = startDay;
        this.startTime = startTime;
        this.duration = duration;
        this.distance = distance;
        this.steps = steps;
        this.avgSpeed = avgSpeed;
        this.routeArray = routeArray;
    }

    public int getId(){
        return id;
    }

    public String getActivityType() {
        return activityType;
    }

    public String getDuration() {
        return duration;
    }

    public String getDistance() {
        return distance;
    }

    public int getSteps() {
        return steps;
    }

    public double getAvgSpeed() {
        return avgSpeed;
    }

    public int getStartYear() {
        return startYear;
    }

    public int getStartMonth() {
        return startMonth;
    }

    public int getStartDay() {
        return startDay;
    }

    public String getStartTime() {
        return startTime;
    }

    public ArrayList<LatLng> getRouteArray() {
        return routeArray;
    }
}
