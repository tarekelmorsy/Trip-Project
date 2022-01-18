package com.example.androidproject.Registeration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.androidproject.R;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
Button btLogin;
TextView tvSignUp;
ImageView gmailimg;

FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        gmailimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(LoginActivity.this,LoginEmail.class);
                startActivity(intent);
            }
        });


    }

    public void init(){

        btLogin=findViewById(R.id.bt_login);
        tvSignUp=findViewById(R.id.tv_signup);
        gmailimg=findViewById(R.id.img_gmail);

    }

    public void login(){



    }



    public void signOut(){

        firebaseAuth.signOut();

    }




}