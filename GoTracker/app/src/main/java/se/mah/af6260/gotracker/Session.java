package se.mah.af6260.gotracker;

import java.sql.Date;
import java.sql.Time;

/**
 * Created by Koffe on 2017-03-01.
 */

public class Session {

    private String activity;
    private int duration, distance, steps;
    private double avgSpeed, stepsPerSecond;
    private Date startDate;
    private Time startTime;

    public Session(String activity, Date startDate, Time startTime, int duration, int distance, int steps, double avgSpeed, double stepsPerSecond){
        this.activity = activity;
        this.startDate = startDate;
        this.startTime = startTime;
        this.duration = duration;
        this.distance = distance;
        this.steps = steps;
        this.avgSpeed = avgSpeed;
        this.stepsPerSecond = stepsPerSecond;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public double getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(double avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public double getStepsPerSecond() {
        return stepsPerSecond;
    }

    public void setStepsPerSecond(double stepsPerSecond) {
        this.stepsPerSecond = stepsPerSecond;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }
}
