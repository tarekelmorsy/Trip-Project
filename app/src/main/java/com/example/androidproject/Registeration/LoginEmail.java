package com.example.androidproject.Registeration;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginEmail extends AppCompatActivity {
    Button btLoginEmail;
    TextInputEditText et_email, et_password, et_ConfirmPass;
    final String TAG = "LoginEmail";

    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_email);
        init();
        firebaseAuth = FirebaseAuth.getInstance();
        btLoginEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = et_email.getText().toString();
                String password = et_password.getText().toString();
                register(email, password);
                et_email.setText("");
                et_password.setText("");
                et_ConfirmPass.setText("");
            }
        });
    }




    public void init() {

        btLoginEmail = findViewById(R.id.bt_login_email);
        et_email = findViewById(R.id.et_login_email);
        et_password = findViewById(R.id.et_login_password_email);
        et_ConfirmPass = findViewById(R.id.et_login_conifirmpassword_email);
    }


    public void register(String email, String password) {
        String confirmPassword=et_ConfirmPass.getText().toString();

        if(email.equals("")||password.equals("")||confirmPassword.equals("")){

            Toast.makeText(LoginEmail.this, "please fill all data", Toast.LENGTH_LONG).show();


        }

       else{

        if (confirmPassword.equals(password)) {

            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user=firebaseAuth.getCurrentUser();
                        user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(LoginEmail.this, "go to your  mail to verify your account to login", Toast.LENGTH_SHORT).show();



                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: "+e.getMessage());
                                Toast.makeText(LoginEmail.this, "verification mail not sent  "+ e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });




                        Toast.makeText(LoginEmail.this, "account created", Toast.LENGTH_LONG).show();
//intent for main menue


                    } else {
                        String errorMessage = task.getException().getLocalizedMessage();
                        Toast.makeText(LoginEmail.this, errorMessage, Toast.LENGTH_LONG).show();
                        Log.i(TAG, "onComplete: " + errorMessage);
                    }
                }
            });

        }


else{
            Toast.makeText(LoginEmail.this, "confirm password not equal  password", Toast.LENGTH_LONG).show();


    }



}}
}
