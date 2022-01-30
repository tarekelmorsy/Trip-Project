package com.example.androidproject.Registeration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.androidproject.MainActivity;
import com.example.androidproject.R;
import com.google.firebase.database.FirebaseDatabase;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);


        Thread background = new Thread() {
            public void run() {
                try {
                    sleep(2000);
                    Intent i=new Intent(getBaseContext(), MainActivity.class);
                    startActivity(i);

                    finish();
                } catch (Exception e) {
                    System.out.println(e.getLocalizedMessage());
                }
            }
        };
        background.start();
    }


}