package com.example.androidproject.ui.ui.cancel;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject.MainActivity;
import com.example.androidproject.R;
import com.example.androidproject.data.Data;
import com.example.androidproject.data.Trip;
 import com.example.androidproject.ui.ui.upcoming.AddAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class CancelFragment extends Fragment {

     RecyclerView recyclerView;
    AddAdapter addAdapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_cancel, container, false);



        recyclerView = view.findViewById(R.id.recCancel);
        AddAdapter.screen=2;

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        if(!MainActivity.storedPreference.equals("null")){
        FirebaseRecyclerOptions<Trip> options = new FirebaseRecyclerOptions.Builder<Trip>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("tripCancel"+ MainActivity.storedPreference), Trip.class).build();

        addAdapter = new AddAdapter(options);
        recyclerView.setAdapter(addAdapter);}
        else if( Data.USER.getUid()!=null){

            FirebaseRecyclerOptions<Trip> options = new FirebaseRecyclerOptions.Builder<Trip>()
                    .setQuery(FirebaseDatabase.getInstance().getReference().child("tripCancel"+ Data.USER.getUid()), Trip.class).build();

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