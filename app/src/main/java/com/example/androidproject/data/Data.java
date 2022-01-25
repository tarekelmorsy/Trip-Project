package com.example.androidproject.data;

import android.widget.ArrayAdapter;

import com.example.androidproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class Data {
    public static final String UPCOMING="1";
    public static final String CANCEL="2";
 
    public static final String DONE ="3";
 
    public static final String KEYMAP="AIzaSyAtaH8N_lSkKtae12pgySJsPbhBHolTSlY";
   public static FirebaseAuth FIREBASEAUTH=FirebaseAuth.getInstance();

    public static FirebaseUser USER;




    //public static final String TWOWAY="2";

    //public static final String NOREPEAT="1";
    //public static final String REPEATDAILY="2";
    //public static final String REPEATWEEKLY="3";
    //public static final String REPEATMONTHLY="4";
    // edit text choice (way,repeat)



}
