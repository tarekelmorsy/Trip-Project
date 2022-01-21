package com.example.androidproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.androidproject.data.Trip;
import com.example.androidproject.ui.ui.upcoming.AddAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class CancelActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    AddAdapter addAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel);
        recyclerView = findViewById(R.id.recCancel);
        AddAdapter.screen=2;

        recyclerView.setLayoutManager(new LinearLayoutManager(CancelActivity.this));
        FirebaseRecyclerOptions<Trip> options = new FirebaseRecyclerOptions.Builder<Trip>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("tripCancel"), Trip.class).build();

        //   Toast.makeText(MainActivity.this, options.getSnapshots().get(0).getTripName()+options.getSnapshots().get(1).getTripName(), Toast.LENGTH_SHORT).show();
        if (!options.getSnapshots().isEmpty()) {

            Toast.makeText(CancelActivity.this, options.getSnapshots().get(1).toString(), Toast.LENGTH_SHORT).show();


        }
        addAdapter = new AddAdapter(options);
        recyclerView.setAdapter(addAdapter);



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