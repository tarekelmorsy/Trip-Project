package com.example.androidproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.androidproject.Registeration.LoginActivity;
import com.facebook.AccessTokenTracker;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

public class TestActivity extends AppCompatActivity {
Button btTest;
    private AccessTokenTracker tracker;
    FirebaseAuth firebaseAuth;
final String TAG="MainActivity";
    private FirebaseAuth.AuthStateListener authStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_main);
        btTest = findViewById(R.id.bt_test);
        firebaseAuth = FirebaseAuth.getInstance();
        //handleFacebookAccessToken(firebaseUser.);

//        authStateListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//                if (user != null) {
//                    // User is signed in
//                    Toast.makeText(MainActivity.this, user.getDisplayName()+" is signed in", Toast.LENGTH_SHORT).show();
//                } else {
//                    // User is signed out
//                   // Toast.makeText(MainActivity.this, user.getDisplayName()+" is signed out", Toast.LENGTH_SHORT).show();
//
//                }
//                // ...
//            }
//        };
        //firebaseAuth.addAuthStateListener(authStateListener);


        btTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseAuth.signOut();
                LoginManager.getInstance().logOut();
                Toast.makeText(TestActivity.this, " sign out successfully ", Toast.LENGTH_SHORT).show();
                Intent intent= new Intent(TestActivity.this,LoginActivity.class);
                startActivity(intent);

            }


        });}




}