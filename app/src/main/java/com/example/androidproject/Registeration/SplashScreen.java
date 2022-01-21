package com.example.androidproject.Registeration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.androidproject.R;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Thread background = new Thread() {
            public void run() {
                try {
                    sleep(2000);
                    Intent i=new Intent(getBaseContext(), LoginActivity.class);
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