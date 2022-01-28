package com.example.androidproject.ui.ui.upcoming;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject.MainActivity;
import com.example.androidproject.data.Data;
import com.example.androidproject.data.Trip;
import com.example.androidproject.databinding.FragmentHomeBinding;
import com.example.androidproject.reciever.AlarmReceiver;
import com.example.androidproject.ui.AddTripActivity;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;


public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    RecyclerView recyclerView;
    AddAdapter addAdapter;
    FloatingActionButton btAdd;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AddAdapter.screen=1;

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);


        recyclerView = binding.recUpcoming;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
          FirebaseRecyclerOptions<Trip> options=new FirebaseRecyclerOptions.Builder<Trip>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("trips"+ Data.USER.getUid()),Trip.class).build();
          //options.getSnapshots().get(0);
         addAdapter = new AddAdapter(options);
        recyclerView.setAdapter(addAdapter);


        btAdd=binding.floatingActionButton;
        btAdd.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), AddTripActivity.class));

        });

    View root = binding.getRoot();

        return root;





    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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