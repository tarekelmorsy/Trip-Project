package com.example.androidproject.ui.ui.history;

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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_history, container, false);

        AddAdapter.screen=3;


        recyclerView = view.findViewById(R.id.recHistory);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        FirebaseRecyclerOptions<Trip> options = new FirebaseRecyclerOptions.Builder<Trip>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("history"+ Data.USER.getUid()), Trip.class).build();

        addAdapter = new AddAdapter(options);
        recyclerView.setAdapter(addAdapter);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        originl=new LatLng(31.423281, 31.810107);
        destinationl=new LatLng(31.421743, 31.810869
        );


         latLngs.add(originl);
        latLngs.add(destinationl);
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
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
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