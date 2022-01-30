package com.example.androidproject.Registeration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.androidproject.R;

public class RegisterActivity extends AppCompatActivity {
ImageView logo;
TextView tvGOlogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        //logo.setImageBitmap(editePicture.decodeSampledBitmapFromResource(getResources(), R.id.img_logo, 50, 50));

        tvGOlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });


    }
     private void init(){
        logo=findViewById(R.id.img_logo);
        tvGOlogin=findViewById(R.id.et_gologin);

     }

}






