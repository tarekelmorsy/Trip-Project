package com.example.androidproject.Registeration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidproject.TestActivity;
import com.example.androidproject.R;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity {
    Button btLogin;
    TextView tvSignUp;
    //ImageView gmailimg;
    TextInputEditText tiEmail, tiPassword;
    LoginButton loginButton;

    final String TAG = "LoginActivity";

    private CallbackManager callbackManager;
    private FirebaseAuth.AuthStateListener authStateListener;
    private AccessTokenTracker tracker;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        firebaseAuth = FirebaseAuth.getInstance();
        //
//        tvSignUp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
//                startActivity(intent);
//            }
//        });
//
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, LoginEmail.class);
                startActivity(intent);
            }
        });

//login email firestore
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = tiEmail.getText().toString();
                String password = tiPassword.getText().toString();
                login(email, password);
                tiEmail.setText("");
                tiPassword.setText("");
            }
        });
//facebook login


        //for git the hash code
        //  FacebookSdk.sdkInitialize(getApplicationContext());
        // Log.d("AppLog", "key:" + FacebookSdk.getApplicationSignature(this)+"=");
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        facebookLogin();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                if (firebaseUser != null) {

                    updateUI(firebaseUser);
                } else {

                    updateUI(null);


                }
            }
        };
        tracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if (currentAccessToken == null) {
                    Toast.makeText(LoginActivity.this, "there is  no user ", Toast.LENGTH_SHORT).show();
                    firebaseAuth.signOut();


                } else {

                    SharedPreferences preferences = getSharedPreferences("mytoken", Context.MODE_PRIVATE);
                    preferences.edit().putString("accessToken", String.valueOf(currentAccessToken.getToken())).apply();
                    Log.i(TAG, "onCurrentAccessTokenChanged: " + currentAccessToken);
                    Toast.makeText(LoginActivity.this, "there is user token = " + currentAccessToken.getToken(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, TestActivity.class);
                    startActivity(intent);


                }


            }
        };


    }


    public void init() {

        btLogin = findViewById(R.id.bt_login);
        tvSignUp = findViewById(R.id.tv_signup);
        //gmailimg = findViewById(R.id.img_gmail);
        tiEmail = findViewById(R.id.et_login_email);
        tiPassword = findViewById(R.id.et_login_password);
    }


    public void facebookLogin() {


        loginButton = (LoginButton) findViewById(R.id.login_button);
        // If using in a fragment
        //   loginButton.setFragment(this);
        loginButton.setReadPermissions("email", "public_profile");

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                SharedPreferences preferences = getSharedPreferences("mytoken2", Context.MODE_PRIVATE);

                preferences.edit().putString("accessToken2", String.valueOf((AccessToken)loginResult.getAccessToken())).apply();
              //  Toast.makeText(LoginActivity.this,  String.valueOf((AccessToken)loginResult.getAccessToken()), Toast.LENGTH_LONG).show();

//                   Log.i(TAG, "onCurrentAccessTokenChanged: " + currentAccessToken);
                handleFacebookAccessToken(loginResult.getAccessToken());
                Toast.makeText(LoginActivity.this, "successful login ", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(LoginActivity.this, TestActivity.class);
                startActivity(intent);

            }

            @Override
            public void onCancel() {
                // App code
                Log.d(TAG, "facebook:onCancel");
                Toast.makeText(LoginActivity.this, "canceled", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.d(TAG, "facebook:onError" + exception.toString());
                Toast.makeText(LoginActivity.this, exception.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();

            }
        });
// Initialize Facebook Login button


        ///

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        updateUI(currentUser);
        if((firebaseAuth.getCurrentUser())==null)
            Toast.makeText(LoginActivity.this, "not exist ", Toast.LENGTH_SHORT).show();

        else
            Toast.makeText(LoginActivity.this, "logined user"+currentUser.getEmail(), Toast.LENGTH_SHORT).show();

        firebaseAuth.addAuthStateListener(authStateListener);

        // Check if user is signed in (non-null) and update UI accordingly.

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);


        }
    }



    private void handleFacebookAccessToken(AccessToken token) {// for token
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, "user account is" + user, Toast.LENGTH_SHORT).show();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void updateUI(FirebaseUser user) {// for token


        if (user != null) {


            Toast.makeText(this, "user is " + user.getDisplayName(), Toast.LENGTH_SHORT).show();


            if (user.getPhotoUrl() != null) {

                String photoUrl = user.getPhotoUrl().toString();
                Toast.makeText(this, "image url= " + photoUrl, Toast.LENGTH_SHORT).show();
                // photoUrl=photoUrl+"?type=large";
                //Picasso.get.load(photoUrl).into(myimageview);
            }


      }
//        else {
////textview.setText("");
//            //myimgview.setImageResource(R.id.pictures);
//
//
//        }
    }


    public void login(String email, String password) {


        if (email.equals("") || password.equals("")) {
            Toast.makeText(LoginActivity.this, "please fill all data ", Toast.LENGTH_LONG).show();


        } else {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (!user.isEmailVerified()) {
                            Toast.makeText(LoginActivity.this, "please verify your account " + firebaseAuth.getCurrentUser().getEmail(), Toast.LENGTH_LONG).show();

                        } else {

                            Toast.makeText(LoginActivity.this, "verified account ", Toast.LENGTH_LONG).show();

                            //Toast.makeText(LoginActivity.this, "correct user", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(LoginActivity.this, TestActivity.class);
                            startActivity(intent);
                        }

                    } else {
                        String errorMessage = task.getException().getLocalizedMessage();
                        Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        Log.i(TAG, "onComplete: " + errorMessage);


                    }
                }
            });
        }

    }


    public void signOut() {// for email

        firebaseAuth.signOut();

    }


}