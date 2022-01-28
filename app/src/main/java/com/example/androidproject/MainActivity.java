package com.example.androidproject;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.androidproject.Registeration.LoginActivity;
import com.example.androidproject.data.Data;
import com.example.androidproject.reciever.AlarmReceiver;
import com.example.androidproject.ui.ui.cancel.CancelFragment;
import com.example.androidproject.ui.ui.upcoming.HomeFragment;
import com.example.androidproject.ui.ui.history.HistoryFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    public ArrayList<Calendar> calendars = new ArrayList<>();


    final String TAG="MainActivity";
    FloatingActionButton  ftLogOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ftLogOut=findViewById(R.id.floatingActionButton2);
        Data.USER= Data.FIREBASEAUTH.getCurrentUser();
        SharedPreferences preferences = getSharedPreferences("mytokennn", Context.MODE_PRIVATE);

        String storedPreference = preferences.getString("x", "null");
        Log.i(TAG, "onCreate: token= "+storedPreference);

        if(Data.USER==null&&storedPreference.equals("null") )
        {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));

            finish();
            return;

        }

        //add alarm
        for ( int i =0;i<5;i++){
            calendars.add(Calendar.getInstance());
        }

        //Calendar calendar= Calendar.getInstance();
        calendars.get(0).set(
                calendars.get(0).get(Calendar.YEAR)
                ,calendars.get(0).get(Calendar.MONTH)
                ,calendars.get(0).get(Calendar.DAY_OF_MONTH)
                ,20,8,0);

        setAlarm(calendars.get(0).getTimeInMillis(),0);

        calendars.get(1).set(
                calendars.get(1).get(Calendar.YEAR)
                ,calendars.get(1).get(Calendar.MONTH)
                ,calendars.get(1).get(Calendar.DAY_OF_MONTH)
                ,20,05,0);
        /*calendar.set(
                calendar.get(Calendar.YEAR)
                ,calendar.get(Calendar.MONTH)
                ,calendar.get(Calendar.DAY_OF_MONTH)
                ,17,25,0);*/

        setAlarm(calendars.get(1).getTimeInMillis(),1);

        //Toast.makeText(this,"Alarm is set",Toast.LENGTH_SHORT).show();



        ftLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Data.FIREBASEAUTH.signOut();
        startActivity(new Intent(MainActivity.this,LoginActivity.class));
            }
        });

        //Log.i(TAG, "onCreate: uid= "+user.getUid());// for email data user

       // Toast.makeText(this, "user is "+firebaseAuth.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
//        Log.i(TAG, "onCreate: "+firebaseAuth.getCurrentUser().getEmail());

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        
        // as soon as the application opens the first
        // fragment should be shown to the user
        // in this case it is algorithm fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            // By using switch we can easily get
            // the selected fragment
            // by using there id.
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.navigation_cancel:
                    selectedFragment = new CancelFragment();
                    break;
                case R.id.navigation_history:
                    selectedFragment = new HistoryFragment();
                    break;
            }
            // It will help to replace the
            // one fragment to other.
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, selectedFragment)
                    .commit();
            return true;
        }
    };


    private void setAlarm(long timeInMillis , int i) {
        Calendar calendarNow = Calendar.getInstance();
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, i, intent, 0);

        if (calendarNow.getTimeInMillis() <= timeInMillis) {

            // finish();
            alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);

/*
        Intent myIntent = new Intent(getBaseContext(), AlarmReceiver.class);
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(getBaseContext(), 0,
                myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent);*/

            Toast.makeText(this, "Alarm is set", Toast.LENGTH_SHORT).show();
        }
        else
            alarmManager.cancel(pendingIntent);

    }


}

