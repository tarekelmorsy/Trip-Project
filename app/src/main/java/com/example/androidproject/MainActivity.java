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
import com.example.androidproject.Registeration.SplashScreen;
import com.example.androidproject.data.Data;
import com.example.androidproject.ui.ui.cancel.CancelFragment;
import com.example.androidproject.ui.ui.upcoming.HomeFragment;
import com.example.androidproject.ui.ui.history.HistoryFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {


    final String TAG="MainActivity";
FloatingActionButton  ftLogOut;
   public static String storedPreference;
    public static String storedUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ftLogOut=findViewById(R.id.floatingActionButton2);
      //  FirebaseDatabase.getInstance().setPersistenceEnabled(true);//************8

        Data.USER= Data.FIREBASEAUTH.getCurrentUser();

        DatabaseReference scoresReft1 = FirebaseDatabase.getInstance().getReference().child("trips"+ MainActivity.storedPreference);
        scoresReft1.keepSynced(true);

        DatabaseReference scoresReft2 = FirebaseDatabase.getInstance().getReference().child("trips"+ MainActivity.storedUid);
        scoresReft2.keepSynced(true);
        SharedPreferences preferences = getSharedPreferences("mytokennn", Context.MODE_PRIVATE);

         storedPreference = preferences.getString("x", "null");

        SharedPreferences preferencesUid = getSharedPreferences("c", Context.MODE_PRIVATE);

        storedUid = preferencesUid.getString("id", "no id exist");


        Log.i(TAG, "onCreate: token= "+storedPreference);
        Log.i(TAG, "onCreate: uidxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx= "+storedUid);


        if(storedUid.equals("no id exist")&&storedPreference.equals("null") )
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
        finish();
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

