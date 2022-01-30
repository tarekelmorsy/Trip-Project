package com.example.androidproject.reciever;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
import androidx.work.Data.Builder;

import com.example.androidproject.data.Trip;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DataForAlarm {
    public static Trip trip;
    public static String[] tripTime, detailsOfMinute, tripDate,tripCalendar;
    public static String hourOfTime, minuteOfTime, amOrPm;
    public static int hours, minutes, day, month, year;
    public static ArrayList<Calendar> arrayOfCalendars = new ArrayList<>();
    private static WorkManager workManager;
    private static WorkRequest workRequest;


    public static void setDataForAlarm(ArrayList<Trip> Trips) {

        Log.i("Main", "onDataForAlarm: Array Size is " + Trips.size());

        for (int i = 0; i < Trips.size(); i++) {
            trip = Trips.get(i);

            tripTime = trip.getAlarm().split(":");
            hourOfTime = tripTime[0];

            detailsOfMinute = tripTime[1].split(" ");
            minuteOfTime = detailsOfMinute[0];
            minutes = Integer.parseInt(minuteOfTime);
            amOrPm = detailsOfMinute[1];
            if (amOrPm.equals("AM")) {
                if (hourOfTime.equals("12")) {
                    hours = 0;
                } else {
                    hours = Integer.parseInt(hourOfTime);
                }
            } else if (amOrPm.equals("PM")) {
                hours = Integer.parseInt(hourOfTime) + 12;
            }

            //Log.i("Main", "onCreate: here");

            tripDate = trip.getDate().split("/");
            day = Integer.parseInt(tripDate[0]);
            month = Integer.parseInt(tripDate[1]) - 1;
            year = Integer.parseInt(tripDate[2]);

            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day, hours, minutes, 0);
            arrayOfCalendars.add(calendar);

            String keyOfTrip = trip.getAlarm() + "&" + trip.getDate() + "&" + trip.getEndPoint() +
                    "&" + trip.getRepeat() + "&" + trip.getStartPoint() + "&" + trip.getStatus() + "&" + trip.getTripName() + "&" + trip.getWay();

            setWorkAlarm(keyOfTrip, arrayOfCalendars.get(arrayOfCalendars.size() - 1));
            Log.i("Main", "onCreate: " + year + " " + month + " " + day + " " + hours + " " + minutes + " ");
        }

    }

    public static void addAlarmForTrip(Map map) {

        String tripAlarm , tripDate,tripName,tripEndPoint,tripStartPoint,tripRepeat,tripStatus,tripWay;

        tripAlarm = map.get("alarm").toString();
        tripDate = map.get("date").toString();
        tripEndPoint = map.get("endPoint").toString();
        tripStartPoint = map.get("startPoint").toString();
        tripRepeat = map.get("repeat").toString();
        tripStatus = map.get("status").toString();
        tripWay = map.get("way").toString();
        tripName = map.get("tripName").toString();

        tripTime = tripAlarm.split(":");
            hourOfTime = tripTime[0];

            detailsOfMinute = tripTime[1].split(" ");
            minuteOfTime = detailsOfMinute[0];
            minutes = Integer.parseInt(minuteOfTime);
            amOrPm = detailsOfMinute[1];
            if (amOrPm.equals("AM")) {
                if (hourOfTime.equals("12")) {
                    hours = 0;
                } else {
                    hours = Integer.parseInt(hourOfTime);
                }
            } else if (amOrPm.equals("PM")) {
                hours = Integer.parseInt(hourOfTime) + 12;
            }

            tripCalendar = tripDate.split("/");
            day = Integer.parseInt(tripCalendar[0]);
            month = Integer.parseInt(tripCalendar[1]) - 1;
            year = Integer.parseInt(tripCalendar[2]);

            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day, hours, minutes, 0);

            String keyOfTrip = tripAlarm + "&" + tripDate + "&" + tripEndPoint +
                    "&" + tripRepeat + "&" + tripStartPoint + "&" + tripStatus + "&" + tripName + "&" + tripWay;
            setWorkAlarm(keyOfTrip, calendar);
            Log.i("Main", "onCreate: " + year + " " + month + " " + day + " " + hours + " " + minutes + " ");
        }


    private static void setWorkAlarm(String keyOfTrip, Calendar calendar) {

        Data dataInput = new Data.Builder().putString("KEY", keyOfTrip).build();
        workManager = WorkManager.getInstance();
        Calendar calendarNow = Calendar.getInstance();
        long nowMillis = calendarNow.getTimeInMillis();
        long diff = calendar.getTimeInMillis() - nowMillis;
        workRequest = new OneTimeWorkRequest.Builder(OpeningDialog.class).setInitialDelay(diff, TimeUnit.MILLISECONDS)
                .addTag(keyOfTrip).setInputData(dataInput)
                .build();
        Log.i("Main", "Tag: keyoftrip =" + keyOfTrip);
        workManager.enqueue(workRequest);
    }


    public static void DeleteAllAlarms() {
        workManager = WorkManager.getInstance();
        workManager.cancelAllWork();
        arrayOfCalendars.clear();
        Log.i("Main", "onCreate: DeleteAllAlarms");
    }

    public static void deleteAllAlarmsLogOut() {
        workManager.cancelAllWork();
    }

    public static void deleteAlarmForOneTrip ( Map map ){
        String tripAlarm , tripDate,tripName,tripEndPoint,tripStartPoint,tripRepeat,tripStatus,tripWay;

        tripAlarm = map.get("alarm").toString();
        tripDate = map.get("date").toString();
        tripEndPoint = map.get("endPoint").toString();
        tripStartPoint = map.get("startPoint").toString();
        tripRepeat = map.get("repeat").toString();
        tripStatus = map.get("status").toString();
        tripWay = map.get("way").toString();
        tripName = map.get("tripName").toString();

        String keyOfTrip = tripAlarm + "&" + tripDate + "&" + tripEndPoint +
                "&" + tripRepeat + "&" + tripStartPoint + "&" + tripStatus + "&" + tripName + "&" + tripWay;

        workManager.cancelAllWorkByTag(keyOfTrip);

    }
}
