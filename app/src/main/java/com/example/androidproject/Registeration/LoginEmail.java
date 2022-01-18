package com.example.androidproject.Registeration;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginEmail extends AppCompatActivity {
   Button btLoginEmail;
   TextInputEditText et_email,et_password;


    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_email);
        init();

        btLoginEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=et_email.getText().toString();
                String password=et_password.getText().toString();

                register(email,password);
            }
        });





    }
    public void init(){

        btLoginEmail=findViewById(R.id.bt_login_email);
        et_email=findViewById(R.id.et_login_password_email);
        et_password=findViewById(R.id.et_login_email);


    }


    public void register(String email,String password){
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(LoginEmail.this,"registered: "+email+" "+password,Toast.LENGTH_LONG).show();



                }
                else{
                    Toast.makeText(LoginEmail.this,"account not created",Toast.LENGTH_LONG).show();


                }
            }
        });

    }

}
