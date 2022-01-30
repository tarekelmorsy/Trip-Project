package com.example.androidproject.data;

import com.google.android.gms.maps.model.LatLng;

public class Trip {
    private String tripName;
    private String startPoint;
    private String endPoint;
    private String date;
    private String alarm;
    private String repeat;
    private String way;
    private String notes;
    private String status;
    private String latLogEnd;
    private double endLat;
    private double startLat;
    private double endLong;
    private double startLong;

    public double getEndLat() {
        return endLat;
    }

    public void setEndLat(double endLat) {
        this.endLat = endLat;
    }

    public double getStartLat() {
        return startLat;
    }

    public void setStartLat(double startLat) {
        startLat = startLat;
    }

    public double getEndLong() {
        return endLong;
    }

    public void setEndLong(double endLong) {
        this.endLong = endLong;
    }

    public double getStartLong() {
        return startLong;
    }

    public void setStartLong(double startLong) {
        startLong = startLong;
    }

    public String getLatLogEnd() {
        return latLogEnd;
    }

    public void setLatLogEnd(String latLogEnd) {
        this.latLogEnd = latLogEnd;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    Trip (){}
    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAlarm() {
        return alarm;
    }

    public void setAlarm(String alarm) {
        this.alarm = alarm;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public String getWay() {
        return way;
    }

    public void setWay(String way) {
        this.way = way;
    }

    public Trip(String name, String start, String end, String dat, String alarm, String repeat, String way) {
        this.tripName = name;
        this.startPoint = start;
        this.endPoint = end;
        this.date = dat;
        this.alarm = alarm;
        this.repeat = repeat;
        this.way = way;
    }


}
