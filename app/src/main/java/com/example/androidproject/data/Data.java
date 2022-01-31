package com.example.androidproject.data;

import android.widget.ArrayAdapter;

import com.example.androidproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class Data {
    public static final String UPCOMING = "1";
    public static final String CANCEL = "2";

    public static final String DONE = "3";
    public static final String UPCOMINGR1 = "4";
    public static final String UPCOMINGR2 = "5";
    public static String alarm = "";
    public static String date = "";
    public static String endPoint = "";
    public static String startPoint = "";
    public static String tripName = "";
    public static String repeat = "";
    public static String way = "";
    public static double endLat = 0.0;
    public static String latLogEnd = "";
    public static double endLong = 0.0;
    public static double startLat = 0.0;
    public static double startLong = 0.0;

    public static final String KEYMAP = "AIzaSyAtaH8N_lSkKtae12pgySJsPbhBHolTSlY";
    public static FirebaseAuth FIREBASEAUTH = FirebaseAuth.getInstance();

    public static FirebaseUser USER = FIREBASEAUTH.getCurrentUser();


    //public static final String TWOWAY="2";

    //public static final String NOREPEAT="1";
    //public static final String REPEATDAILY="2";
    //public static final String REPEATWEEKLY="3";
    //public static final String REPEATMONTHLY="4";
    // edit text choice (way,repeat)


}
