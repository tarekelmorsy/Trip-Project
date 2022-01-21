package com.example.androidproject.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.androidproject.R;
import com.example.androidproject.data.Data;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddTripActivity extends AppCompatActivity {

    private EditText edStartPoint, edEndPoint, edTripName;
    private ImageView imvTime, imvDate;
    private TextView tvTime, tvDate;
    private int t1Hour, t1Minut;
    private Button btAdd;
    private AutoCompleteTextView repeat, way;
    private ArrayAdapter<String> arrayAdapterRepeat;
    private ArrayAdapter<String> arrayAdapterWay;
    public static ArrayList<String> repeatList;
    public static ArrayList<String> wayList;
    public static DatePickerDialog.OnDateSetListener listenerDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        initialize();

        Places.initialize(getApplicationContext(), Data.KEYMAP);
        String placeId = "INSERT_PLACE_ID_HERE";
        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME);

        FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields)
                .build();

        way.setFocusable(false);
        repeat.setFocusable(false);
        edStartPoint.setFocusable(false);
        edStartPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //////////////////////////////
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ID, Place.Field.ADDRESS
                        , Place.Field.LAT_LNG, Place.Field.NAME);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY
                        , fieldList).build(AddTripActivity.this);
                startActivityForResult(intent, 100);
            }
        });
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

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        //show timeDialog
        imvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });
        imvDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(AddTripActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth
                        , listenerDate, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        listenerDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth + "/" + month + "/" + year;
                tvDate.setText(date);

            }
        };


        btAdd.setOnClickListener(v -> {
            insertData();
            clearAll();
        });

    }

    private void insertData() {
        Map<String, Object> map = new HashMap<>();
        map.put("alarm", tvTime.getText().toString());
        map.put("dat", tvDate.getText().toString());
        map.put("endPoint", edEndPoint.getText().toString());
        map.put("startPoint", edStartPoint.getText().toString());
        map.put("tripName", edTripName.getText().toString());
        map.put("repeat", repeat.getText().toString());
        map.put("way", way.getText().toString());
        map.put("status", Data.UPCOMING);
        FirebaseDatabase.getInstance().getReference().child("trips").push().setValue(map)
                .addOnSuccessListener(unused -> Toast.makeText(AddTripActivity.this, "Data Insert is Successfully.", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> {
                    Toast.makeText(AddTripActivity.this, "Error while Insertion", Toast.LENGTH_SHORT).show();

                });

    }

    private void clearAll() {

        edEndPoint.setText("");
        edTripName.setText("");
        edStartPoint.setText("");
        tvTime.setText("");
        tvDate.setText("");
        repeat.setText(R.string.noRepeat);
        way.setText(R.string.oneWay);

    }

    public void initialize() {
        edEndPoint = findViewById(R.id.edEndPoint);
        edTripName = findViewById(R.id.edTripName);
        edStartPoint = findViewById(R.id.edStartPoint);
        imvDate = findViewById(R.id.imvDate);
        imvTime = findViewById(R.id.imvTime);
        tvDate = findViewById(R.id.tvDate);
        tvTime = findViewById(R.id.tvTime);
        repeat = findViewById(R.id.repeat);
        way = findViewById(R.id.way);
        repeatList = new ArrayList<>();
        wayList = new ArrayList<>();
        btAdd = findViewById(R.id.btAdd);

    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        if (resultCode == 100 && resultCode == RESULT_OK) {

            Place place = Autocomplete.getPlaceFromIntent(data);
            edStartPoint.setText(place.getAddress());
        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {

            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(AddTripActivity.this, status.getStatusMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}