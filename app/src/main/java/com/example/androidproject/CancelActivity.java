package com.example.androidproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.androidproject.data.Data;
import com.example.androidproject.data.Trip;
import com.example.androidproject.ui.ui.upcoming.AddAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CancelActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    AddAdapter addAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        recyclerView = findViewById(R.id.recCancel);
        AddAdapter.screen=2;
        DatabaseReference scoresRef2 = FirebaseDatabase.getInstance().getReference().child("tripCancel" + MainActivity.storedUid);
        scoresRef2.keepSynced(true);


        DatabaseReference scoresRef1= FirebaseDatabase.getInstance().getReference().child("tripCancel" + MainActivity.storedPreference);
        scoresRef1.keepSynced(true);


if(MainActivity.storedPreference.equals("null")) {
    recyclerView.setLayoutManager(new LinearLayoutManager(CancelActivity.this));
    FirebaseRecyclerOptions<Trip> options = new FirebaseRecyclerOptions.Builder<Trip>()
            .setQuery(scoresRef1, Trip.class).build();

        //   Toast.makeText(MainActivity.this, options.getSnapshots().get(0).getTripName()+options.getSnapshots().get(1).getTripName(), Toast.LENGTH_SHORT).show();
        if (!options.getSnapshots().isEmpty()) {

            Toast.makeText(CancelActivity.this, options.getSnapshots().get(1).toString(), Toast.LENGTH_SHORT).show();


        }
        addAdapter = new AddAdapter(options);
        recyclerView.setAdapter(addAdapter);}

else if(! MainActivity.storedUid.equals("no id exist")){


    recyclerView.setLayoutManager(new LinearLayoutManager(CancelActivity.this));
    FirebaseRecyclerOptions<Trip> options = new FirebaseRecyclerOptions.Builder<Trip>()
            .setQuery(scoresRef2, Trip.class).build();

    //   Toast.makeText(MainActivity.this, options.getSnapshots().get(0).getTripName()+options.getSnapshots().get(1).getTripName(), Toast.LENGTH_SHORT).show();
    if (!options.getSnapshots().isEmpty()) {

        Toast.makeText(CancelActivity.this, options.getSnapshots().get(1).toString(), Toast.LENGTH_SHORT).show();


    }
    addAdapter = new AddAdapter(options);
    recyclerView.setAdapter(addAdapter);



}



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