package com.example.androidproject.ui.ui.upcoming;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject.data.Data;
import com.example.androidproject.data.Trip;
import com.example.androidproject.databinding.FragmentHomeBinding;
import com.example.androidproject.ui.AddTripActivity;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.siddharthks.bubbles.FloatingBubblePermissions;


public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    RecyclerView recyclerView;
    AddAdapter addAdapter;
      FloatingActionButton btAdd;
    Calendar calendar;
FirebaseUser user;
    public  FirebaseAuth FIREBASEAUTH;
    DatabaseReference scoresRef;
    FloatingActionButton btAdd;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AddAdapter.screen=1;
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        FIREBASEAUTH=FirebaseAuth.getInstance();
        DatabaseReference scoresRef2 = FirebaseDatabase.getInstance().getReference().child("trips" + MainActivity.storedUid);
        scoresRef2.keepSynced(true);
        DatabaseReference scoresRef1 = FirebaseDatabase.getInstance().getReference().child("trips" + MainActivity.storedPreference);
        scoresRef1.keepSynced(true);

        user= FIREBASEAUTH.getCurrentUser();

        recyclerView = binding.recUpcoming;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
          FirebaseRecyclerOptions<Trip> options=new FirebaseRecyclerOptions.Builder<Trip>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("trips"+ Data.USER.getUid()),Trip.class).build();
          //options.getSnapshots().get(0);

         addAdapter = new AddAdapter(options);
        recyclerView.setAdapter(addAdapter);


        if(!MainActivity.storedPreference.equals("null")) {
            FirebaseRecyclerOptions<Trip> options = new FirebaseRecyclerOptions.Builder<Trip>()
                    .setQuery(scoresRef, Trip.class).build();
            //options.getSnapshots().get(0);
            addAdapter = new AddAdapter(options);
            recyclerView.setAdapter(addAdapter);
        }

        else if(! MainActivity.storedUid.equals("no id exist")){
            FirebaseRecyclerOptions<Trip> options = new FirebaseRecyclerOptions.Builder<Trip>()
                    .setQuery(scoresRef2, Trip.class).build();
            //options.getSnapshots().get(0);
            addAdapter = new AddAdapter(options);
            recyclerView.setAdapter(addAdapter);
        }
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