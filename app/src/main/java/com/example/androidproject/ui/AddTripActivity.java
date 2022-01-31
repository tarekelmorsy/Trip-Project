package com.example.androidproject.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.androidproject.MainActivity;
import com.example.androidproject.R;
import com.example.androidproject.data.Data;
import com.example.androidproject.data.Trip;
import com.example.androidproject.reciever.DataForAlarm;
import com.example.androidproject.ui.ui.upcoming.AddAdapter;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddTripActivity extends AppCompatActivity {

    private EditText edStartPoint, edEndPoint, edTripName;
    private ImageView imvTime, imvDate, imvTimeBack, imvDateBack;
    private TextView tvDate, tvTime, tvTimeBack, tvDateBack, tvGoing,tvTitle;
    private int t1Hour, t1Minut;
    private Button btAdd;
    private AutoCompleteTextView repeat, way;
    private ArrayAdapter<String> arrayAdapterRepeat;
    private ArrayAdapter<String> arrayAdapterWay;
    public static ArrayList<String> repeatList;
    public static ArrayList<String> wayList;
    public static DatePickerDialog.OnDateSetListener listenerDate;
    DatabaseReference scoresRef2;
    DatabaseReference scoresRef1;
    Calendar calendar = Calendar.getInstance();
    final int year = calendar.get(Calendar.YEAR);
    final int month = calendar.get(Calendar.MONTH);
    final int day = calendar.get(Calendar.DAY_OF_MONTH);
    private ConstraintLayout containerBack;
    List<Place.Field> placeFields;
    int STARTKEY = 100;
    int ENDKEY = 101;

    private double endLat;
    private double startLat;
    private double endLong;
    private double startLong;
    private String endMapLatLong;

    ArrayList<Trip> arrayTrips;

    private static int AUTOCOMPLETE_REQUEST_CODE = 1;

    final String TAG = "AddTripActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        initialize();
        Data.USER = Data.FIREBASEAUTH.getCurrentUser();
        // FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        Log.i(TAG, "onCreate: uidddddddddddddddddddddddddddddddddd" + MainActivity.storedUid);
        AddAdapter.screen = 1;
        scoresRef2 = FirebaseDatabase.getInstance().getReference().child("trips" + MainActivity.storedUid);
        scoresRef2.keepSynced(true);


        scoresRef1 = FirebaseDatabase.getInstance().getReference().child("trips" + MainActivity.storedPreference);
        scoresRef1.keepSynced(true);
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), Data.KEYMAP);
        }

        edStartPoint.setFocusable(false);
        edEndPoint.setFocusable(false);

        edStartPoint.setOnClickListener(v -> {
            placeFields = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME);

            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, placeFields)
                    .build(AddTripActivity.this);
            startActivityForResult(intent, STARTKEY);


        });
        edEndPoint.setOnClickListener(v -> {
            placeFields = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME);

            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, placeFields)
                    .build(AddTripActivity.this);
            startActivityForResult(intent, ENDKEY);


        });
        way.setFocusable(false);
        repeat.setFocusable(false);

        wayList.add(getString(R.string.oneWay));
        wayList.add(getString(R.string.towWay));
        repeatList.add(getString(R.string.noRepeat));
        repeatList.add(getString(R.string.repeatDaily));
        repeatList.add(getString(R.string.repeatWeekly));
        repeatList.add(getString(R.string.repeatMonthly));

        arrayAdapterRepeat = new ArrayAdapter(getApplicationContext(), R.layout.tv_entity, repeatList);
        repeat.setAdapter(arrayAdapterRepeat);
        arrayAdapterWay = new ArrayAdapter(getApplicationContext(), R.layout.tv_entity, wayList);
        way.setAdapter(arrayAdapterWay);

        if (way.getText().toString().equals(getString(R.string.towWay))) {
            tvGoing.setVisibility(View.VISIBLE);
            containerBack.setVisibility(View.VISIBLE);
        } else {
            tvGoing.setVisibility(View.GONE);
            containerBack.setVisibility(View.GONE);

        }

        //show timeDialog
        imvTime.setOnClickListener(v -> setTime(tvTime));
        imvDate.setOnClickListener(v -> setDate(tvDate));
        imvTimeBack.setOnClickListener(v -> setTime(tvTimeBack));
        imvDateBack.setOnClickListener(v -> setDate(tvDateBack));
        way.setOnClickListener(v -> {
            if (way.getText().toString().equals(getString(R.string.towWay))) {
                tvGoing.setVisibility(View.VISIBLE);
                containerBack.setVisibility(View.VISIBLE);
            } else {
                tvGoing.setVisibility(View.GONE);
                containerBack.setVisibility(View.GONE);

            }
        });

        btAdd.setOnClickListener(v -> {
            insertData();
            clearAll();
            finish();
        });


    }

    private void insertData() {


        Data.USER = Data.FIREBASEAUTH.getCurrentUser();
        SharedPreferences preferences = getSharedPreferences("mytokennn", Context.MODE_PRIVATE);

        String storedPreference = preferences.getString("x", "null");
        Log.i(TAG, "onCreate: token= " + storedPreference);



        if (!MainActivity.storedPreference.equals("null")) {

            Map<String, Object> map = new HashMap<>();
            map.put("endPoint", edEndPoint.getText().toString());
            map.put("startPoint", edStartPoint.getText().toString());
            map.put("tripName", edTripName.getText().toString());
            map.put("repeat", repeat.getText().toString());
            map.put("way", way.getText().toString());
            map.put("status", Data.UPCOMING);
            map.put("latLogEnd", endMapLatLong);
            map.put("endLat", endLat);
            map.put("endLong", endLong);
            map.put("startLat", startLat);
            map.put("startLong", startLong);
            map.put("alarm", tvTime.getText().toString());
            map.put("date", tvDate.getText().toString());


            scoresRef1.push().setValue(map)

                    .addOnSuccessListener(unused -> Toast.makeText(AddTripActivity.this, "Data Insert is Successfully.", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> {
                        Toast.makeText(AddTripActivity.this, "Error while Insertion", Toast.LENGTH_SHORT).show();

                    });

            /*
            if(way.getText().toString().equals(getString(R.string.towWay))){
            tvGoing.setVisibility(View.VISIBLE);
            containerBack.setVisibility(View.VISIBLE);
            map.put("status", Data.UPCOMINGR1);
            map.put("alarm", tvTime.getText().toString());
            map.put("date", tvDate.getText().toString());
            map.put(" latLogEnd", "30°31'32.5, 30°57'10.5");


            FirebaseDatabase.getInstance().getReference().child("trips"+Data.USER.getUid()).push().setValue(map)

                });*/
            DataForAlarm.addAlarmForTrip(map);

        }
        else if(!MainActivity.storedUid.equals("no id exist")){
            Map<String, Object> map = new HashMap<>();
            map.put("endPoint", edEndPoint.getText().toString());
            map.put("startPoint", edStartPoint.getText().toString());
            map.put("tripName", edTripName.getText().toString());
            map.put("repeat", repeat.getText().toString());
            map.put("way", way.getText().toString());
            map.put("status", Data.UPCOMING);
            map.put("latLogEnd", endMapLatLong);
            map.put("endLat", endLat);
            map.put("endLong", endLong);
            map.put("startLat", startLat);
            map.put("startLong", startLong);
            map.put("alarm", tvTime.getText().toString());
            map.put("date", tvDate.getText().toString());

            scoresRef2.push().setValue(map)
                    .addOnSuccessListener(unused -> Toast.makeText(AddTripActivity.this, "Data Insert is Successfully.", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> {
                        Toast.makeText(AddTripActivity.this, "Error while Insertion", Toast.LENGTH_SHORT).show();
                    });

            DataForAlarm.addAlarmForTrip(map);

        }

    }


    private void handleError() {
        if (edEndPoint.getText().toString() == "") {
            edEndPoint.setError("please enter End Point");

        } else if (edTripName.getText().toString() == "") {
            edTripName.setError("please enter Trip Name");

        } else if (edStartPoint.getText().toString() == "") {
            edEndPoint.setError("please enter Start Point");

        } else if (tvDate.getText().toString() == "") {
            tvDate.setError("please enter Date");

        } else if (tvTime.getText().toString() == "") {
            tvTime.setError("please enter Date");

        }

    }

    private void clearAll() {

        edEndPoint.setText("");
        edTripName.setText("");
        edStartPoint.setText("");
        tvTime.setText("");
        tvDate.setText("");
        repeat.setText(R.string.noRepeat);
        way.setText(R.string.oneWay);
        tvTimeBack.setText("");
        tvDateBack.setText("");

    }

    public void initialize() {
        edEndPoint = findViewById(R.id.edEndPoint);
        edTripName = findViewById(R.id.edTripName);
        edStartPoint = findViewById(R.id.edStartPoint);
        imvDate = findViewById(R.id.imvDate);
        tvTitle=findViewById(R.id.tevTitle);
        imvTime = findViewById(R.id.imvTime);
        tvDate = findViewById(R.id.tvDate);
        tvTime = findViewById(R.id.tvTime);
        repeat = findViewById(R.id.repeat);
        way = findViewById(R.id.way);
        repeatList = new ArrayList<>();
        wayList = new ArrayList<>();
        btAdd = findViewById(R.id.btAdd);
        imvDateBack = findViewById(R.id.imvDateBack);
        imvTimeBack = findViewById(R.id.imvTimeBack);
        tvDateBack = findViewById(R.id.tvDateBack);
        tvTimeBack = findViewById(R.id.tvTimeBack);
        tvGoing = findViewById(R.id.tvGoing);
        containerBack = findViewById(R.id.containerBack);
    }

    public void setDate(TextView tvDate) {


        listenerDate = (view, year, month, dayOfMonth) -> {
            month = month + 1;
            String date = dayOfMonth + "/" + month + "/" + year;
            tvDate.setText(date);

        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(AddTripActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth
                , listenerDate, year, month, day);
        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        datePickerDialog.show();

    }

    public void setTime(TextView tvTime) {

        TimePickerDialog timePickerDialog = new TimePickerDialog(AddTripActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                t1Hour = hourOfDay;
                t1Minut = minute;

                calendar.set(0, 0, 0, t1Hour, t1Minut);
                tvTime.setText(DateFormat.format("hh:mm aa", calendar));

            }
        }, 12, 0, false);
        timePickerDialog.updateTime(t1Hour, t1Minut);
        timePickerDialog.show();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == STARTKEY && resultCode == RESULT_OK) {

            Place place = Autocomplete.getPlaceFromIntent(data);
            edStartPoint.setText(place.getAddress());
            startLat = place.getLatLng().latitude;
            startLong = place.getLatLng().longitude;


        } else if (requestCode == ENDKEY && resultCode == RESULT_OK) {

            Place place = Autocomplete.getPlaceFromIntent(data);
            edEndPoint.setText(place.getAddress());
            endLong = place.getLatLng().longitude;
            endLat = place.getLatLng().latitude;
            endMapLatLong = String.valueOf(place.getLatLng().latitude + ", " + place.getLatLng().longitude);


        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {

            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(AddTripActivity.this, status.getStatusMessage(), Toast.LENGTH_SHORT).show();
        }

    }
}