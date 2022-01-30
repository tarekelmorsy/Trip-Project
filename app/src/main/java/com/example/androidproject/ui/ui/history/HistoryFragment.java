package com.example.androidproject.ui.ui.history;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject.MainActivity;
import com.example.androidproject.R;
import com.example.androidproject.data.Data;
import com.example.androidproject.data.Trip;
 import com.example.androidproject.ui.ui.upcoming.AddAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class HistoryFragment extends Fragment {

    RecyclerView recyclerView;
    AddAdapter addAdapter;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_history, container, false);

AddAdapter.screen=3;
        DatabaseReference scoresRef = FirebaseDatabase.getInstance().getReference().child("history"+ MainActivity.storedPreference);
        scoresRef.keepSynced(true);
        DatabaseReference scoresRef2 = FirebaseDatabase.getInstance().getReference().child("history"+ MainActivity.storedUid);
        scoresRef2.keepSynced(true);

        recyclerView = view.findViewById(R.id.recHistory);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
       // FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        if(!MainActivity.storedPreference.equals("null")){

        FirebaseRecyclerOptions<Trip> options = new FirebaseRecyclerOptions.Builder<Trip>()
                .setQuery(scoresRef, Trip.class).build();

        addAdapter = new AddAdapter(options);
        recyclerView.setAdapter(addAdapter);}

        else if(! MainActivity.storedUid.equals("no id exist")){



            FirebaseRecyclerOptions<Trip> options = new FirebaseRecyclerOptions.Builder<Trip>()
                    .setQuery(scoresRef2, Trip.class).build();

            addAdapter = new AddAdapter(options);
            recyclerView.setAdapter(addAdapter);
        }


        return view;
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