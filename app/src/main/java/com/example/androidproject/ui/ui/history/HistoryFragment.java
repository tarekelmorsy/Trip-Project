package com.example.androidproject.ui.ui.history;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class HistoryFragment extends Fragment {

    RecyclerView recyclerView;
    AddAdapter addAdapter;
    private GoogleMap mMap;
    ArrayList markerPoints= new ArrayList();

    Polyline polyline=null;
    List<LatLng> latLngs=new ArrayList<>();

    List<Marker> markerList=new ArrayList<>();
     private List<LatLng> polyLineList;
    private PolylineOptions polylineOptions;
    private LatLng originl,destinationl;
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    FirebaseUser user;
    DatabaseReference databaseReference;
     ArrayList<Trip> arrayTrips;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_history, container, false);

        AddAdapter.screen=3;
        user= firebaseAuth.getCurrentUser();


        arrayTrips=new ArrayList<>();
          databaseReference = FirebaseDatabase.getInstance().getReference().child("history"+ Data.USER.getUid()) ;

          databaseReference.addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot snapshot) {
                  for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                      Trip trip=dataSnapshot.getValue(Trip.class);
                      arrayTrips.add(trip);
                   }
                   for (Trip trip :arrayTrips){

                      latLngs.add(new LatLng(trip.getEndLat(),trip.getEndLong()));
                      latLngs.add(new LatLng(trip.getStartLat(),trip.getStartLong()));

                  }



              }

              @Override
              public void onCancelled(@NonNull DatabaseError error) {

              }
          });

        recyclerView = view.findViewById(R.id.recHistory);
      Toast.makeText(getContext(), latLngs.size()+"", Toast.LENGTH_SHORT).show();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        FirebaseRecyclerOptions<Trip> options = new FirebaseRecyclerOptions.Builder<Trip>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("history"+ Data.USER.getUid()), Trip.class).build();

        addAdapter = new AddAdapter(options);
        recyclerView.setAdapter(addAdapter);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);


       // Toast.makeText(getContext(), arrayTrips.size()+"", Toast.LENGTH_SHORT).show();



         if (polyline!=null)polyline.remove();

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                mMap = googleMap;

                mMap.setTrafficEnabled(true);
                PolylineOptions polylineOptions=new PolylineOptions().addAll(latLngs).clickable(true);
                polyline=mMap.addPolyline(polylineOptions);
                polyline=mMap.addPolyline(polylineOptions);


                    for (LatLng latLng : latLngs) {
                        MarkerOptions markerOptions = new MarkerOptions().position(latLng);
                    mMap.addMarker(markerOptions);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5));
                        polyline.setColor(Color.rgb(new Random().nextInt(200),new Random().nextInt(200),new Random().nextInt(200)));


                    }


            }});

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