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

import com.example.androidproject.MainActivity;
import com.example.androidproject.R;
import com.example.androidproject.SimpleService;
import com.example.androidproject.data.Data;
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
import com.google.firebase.database.FirebaseDatabase;
import com.siddharthks.bubbles.FloatingBubblePermissions;


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
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);

//
//        FloatingBubblePermissions.startPermissionRequest(this);
//        startService(new Intent(getApplicationContext(), SimpleService.class));
        Data.FIREBASEAUTH = FirebaseAuth.getInstance();
         firebaseUser =  Data.FIREBASEAUTH.getCurrentUser();

     //Data.FIREBASEAUTH.Persistence.LOCAL;

       // firebase.auth.Auth.Persistence.LOCAL;
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, LoginEmail.class);
                startActivity(intent);
                finish();
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
              //  Log.i(TAG, "onClick: uid"+Data.USER.getUid());
            }
        });

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        facebookLogin();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                 firebaseUser = firebaseAuth.getCurrentUser();//&&&&&&&

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
                    Data.FIREBASEAUTH.signOut();


                } else {

                    SharedPreferences preferences = getSharedPreferences("mytokennn", Context.MODE_PRIVATE);
                    preferences.edit().putString("x", currentAccessToken.getToken()).apply();
                    Log.i(TAG, "onCurrentAccessTokenChanged: " + currentAccessToken.getToken());
                    Toast.makeText(LoginActivity.this, "there is user token = " + currentAccessToken.getToken(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);//****
                    startActivity(intent);
                    finish();

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
;
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);

               handleFacebookAccessToken(loginResult.getAccessToken());
                Toast.makeText(LoginActivity.this, "successful login ", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);//**
                startActivity(intent);
                finish();
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

    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseUser = Data.FIREBASEAUTH.getCurrentUser();//&&&
        updateUI(firebaseUser);
        if((Data.FIREBASEAUTH.getCurrentUser())==null)
            Toast.makeText(LoginActivity.this, "not exist ", Toast.LENGTH_SHORT).show();

        else
            Toast.makeText(LoginActivity.this, "logined user"+firebaseUser.getEmail(), Toast.LENGTH_SHORT).show();

        Data.FIREBASEAUTH.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authStateListener != null) {
            Data.FIREBASEAUTH.removeAuthStateListener(authStateListener);


        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    private void handleFacebookAccessToken(AccessToken token) {// for token
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        Data.FIREBASEAUTH.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                             firebaseUser = Data.FIREBASEAUTH.getCurrentUser();///&&&&&&&&&&&&&&&&&&&
                            Toast.makeText(LoginActivity.this, "user account is" + firebaseUser, Toast.LENGTH_SHORT).show();
                            updateUI(firebaseUser);
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


      }

    }


    public void login(String email, String password) {


        if (email.equals("") || password.equals("")) {
            Toast.makeText(LoginActivity.this, "please fill all data ", Toast.LENGTH_LONG).show();


        } else {
            Data.FIREBASEAUTH.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        firebaseUser = Data.FIREBASEAUTH.getCurrentUser();///&&&&&&&&&&&&&&&&&
                        if (!firebaseUser.isEmailVerified()) {
                            Toast.makeText(LoginActivity.this, "please verify your account " + Data.FIREBASEAUTH.getCurrentUser().getEmail(), Toast.LENGTH_LONG).show();
                            //Toast.makeText(LoginActivity.this, "uid="+user.getUid() + Data.FIREBASEAUTH.getCurrentUser().getEmail(), Toast.LENGTH_LONG).show();
                         //   Log.i(TAG, "onComplete: xxxxxxxxxxxxxxxx"+Data.FIREBASEAUTH.getCurrentUser().getUid());

                        } else {

                            Toast.makeText(LoginActivity.this, "verified account ", Toast.LENGTH_LONG).show();
                               Log.i(TAG, "onComplete: uidxx"+Data.FIREBASEAUTH.getCurrentUser().getUid());
                            Toast.makeText(LoginActivity.this, "verified account "+Data.FIREBASEAUTH.getCurrentUser().getUid(), Toast.LENGTH_LONG).show();

                            SharedPreferences preferences2 = getSharedPreferences("c", Context.MODE_PRIVATE);
                            preferences2.edit().putString("id", Data.FIREBASEAUTH.getCurrentUser().getUid()).apply();
                            //Toast.makeText(LoginActivity.this, "correct user", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }

                    } else {
                        String errorMessage = task.getException().getLocalizedMessage();
                        Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        Log.i(TAG, "onComplete:error " + errorMessage);


                    }
                }
            });
        }

    }


}