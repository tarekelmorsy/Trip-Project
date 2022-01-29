package com.example.androidproject;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.ui.AppBarConfiguration;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
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
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {


final String TAG="MainActivity";
FloatingActionButton  ftLogOut;
     DrawerLayout drawer;
    private AppBarConfiguration mAppBarConfiguration;
    BottomNavigationView bottomNav;
    ActionBarDrawerToggle actionBarDrawerToggle;
    int lang_selected;
    Context context;
    Resources resources;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //ftLogOut=findViewById(R.id.floatingActionButton2);
       Data.USER= Data.FIREBASEAUTH.getCurrentUser();
        SharedPreferences preferences = getSharedPreferences("mytokennn", Context.MODE_PRIVATE);
        setUpToolbar();
        String storedPreference = preferences.getString("x", "null");
        Log.i(TAG, "onCreate: token= "+storedPreference);

        if(Data.USER==null&&storedPreference.equals("null") )
{
    startActivity(new Intent(MainActivity.this, LoginActivity.class));

    finish();
    return;


}
        if(LocaleHelper.getLanguage(MainActivity.this).equalsIgnoreCase("en"))
        {
            context = LocaleHelper.setLocale(MainActivity.this,"en");
            resources =context.getResources();
            Toast.makeText(MainActivity.this, "ENGLISH", Toast.LENGTH_SHORT).show();
            setTitle(resources.getString(R.string.app_name));
            lang_selected = 0;
        }else if(LocaleHelper.getLanguage(MainActivity.this).equalsIgnoreCase("ar")){
            context = LocaleHelper.setLocale(MainActivity.this,"ar");
            resources =context.getResources();
            Toast.makeText(MainActivity.this, "اللغة العربية", Toast.LENGTH_SHORT).show();
            setTitle(resources.getString(R.string.app_name));
            lang_selected =1;
        }
        else if(LocaleHelper.getLanguage(MainActivity.this).equalsIgnoreCase("ar")){
            context = LocaleHelper.setLocale(MainActivity.this,"ar");
            resources =context.getResources();
            Toast.makeText(MainActivity.this, "اللغة العربية", Toast.LENGTH_SHORT).show();
            setTitle(resources.getString(R.string.app_name));
            lang_selected =2;
        }

        mAppBarConfiguration = new AppBarConfiguration.Builder()
                .setOpenableLayout(drawer)
                .build();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.nav_home: {
                        Data.FIREBASEAUTH.signOut();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));

                        break;
                    }
                    case R.id.nav_ar: {
                    final String[] Language = {"ENGLISH","ar"};
                final int checkItem;
                Log.d("Clicked","Clicked");
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
                dialogBuilder.setTitle("Select a Language")
                        .setSingleChoiceItems(Language, lang_selected, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(MainActivity.this, Language[i], Toast.LENGTH_SHORT).show();

                                if(Language[i].equals("ENGLISH")){
                                    context = LocaleHelper.setLocale(MainActivity.this,"en");
                                    resources =context.getResources();
                                    lang_selected = 0;
                                    Toast.makeText(MainActivity.this, "ENGLISH", Toast.LENGTH_SHORT).show();
                                    setTitle(resources.getString(R.string.app_name));
                                }
                                if(Language[i].equals("ar"))
                                {
                                    context = LocaleHelper.setLocale(MainActivity.this,"hi");
                                    resources =context.getResources();
                                    lang_selected = 1;
                                    Toast.makeText(MainActivity.this, Language[i], Toast.LENGTH_SHORT).show();
                                    setTitle(resources.getString(R.string.app_name));
                                }

                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                dialogBuilder.create().show();
            }



                    case R.id.nav_En: {}


                }
                return false;
            }
        });


          bottomNav = findViewById(R.id.bottom_navigation);
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

    public void setUpToolbar() {
        drawer =findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.app_name, R.string.app_name);
        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.black));
        actionBarDrawerToggle.syncState();

    }
}

