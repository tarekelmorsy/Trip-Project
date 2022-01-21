package com.example.androidproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.androidproject.data.Trip;
import com.example.androidproject.databinding.FragmentHomeBinding;
import com.example.androidproject.ui.AddTripActivity;
import com.example.androidproject.ui.ui.home.AddAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    AddAdapter addAdapter;
    FloatingActionButton btAdd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recUpcoming2);
        btAdd = findViewById(R.id.floatingActionButton2);

        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        FirebaseRecyclerOptions<Trip> options = new FirebaseRecyclerOptions.Builder<Trip>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("trips"), Trip.class).build();

        //   Toast.makeText(MainActivity.this, options.getSnapshots().get(0).getTripName()+options.getSnapshots().get(1).getTripName(), Toast.LENGTH_SHORT).show();
        if (!options.getSnapshots().isEmpty()) {

            Toast.makeText(MainActivity.this, options.getSnapshots().get(1).toString(), Toast.LENGTH_SHORT).show();


        }
        addAdapter = new AddAdapter(options);
        recyclerView.setAdapter(addAdapter);


        btAdd.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AddTripActivity.class));

        });


    }


    @Override
    public void onStart() {
        super.onStart();
        addAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        addAdapter.stopListening();
    }
}