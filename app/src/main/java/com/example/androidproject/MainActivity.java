package com.example.androidproject;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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
import com.example.androidproject.ui.ui.cancel.CancelFragment;
import com.example.androidproject.ui.ui.upcoming.HomeFragment;
import com.example.androidproject.ui.ui.history.HistoryFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {


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


}

