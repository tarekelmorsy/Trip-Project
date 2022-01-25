package com.example.androidproject.ui.ui.history;

import android.os.Bundle;
import android.util.AndroidException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject.R;
import com.example.androidproject.data.Data;
import com.example.androidproject.data.Trip;
import com.example.androidproject.ui.ui.history.map.ApiInterface;
import com.example.androidproject.ui.ui.history.map.Result;
import com.example.androidproject.ui.ui.history.map.Route;
import com.example.androidproject.ui.ui.upcoming.AddAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.ButtCap;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class HistoryFragment extends Fragment  {
     RecyclerView recyclerView;
    AddAdapter addAdapter;
    private GoogleMap mMap;
    Button button2;
    ArrayList markerPoints= new ArrayList();

    Polyline polyline=null;
    List<LatLng>latLngs=new ArrayList<>();
    List<Marker> markerList=new ArrayList<>();
    private ApiInterface apiInterface;
    private List<LatLng> polyLineList;
    private PolylineOptions polylineOptions;
    private LatLng originl,destinationl;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        AddAdapter.screen = 3;
        
        recyclerView = view.findViewById(R.id.recHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        FirebaseRecyclerOptions<Trip> options = new FirebaseRecyclerOptions.Builder<Trip>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("history"), Trip.class).build();
        addAdapter = new AddAdapter(options);
        recyclerView.setAdapter(addAdapter);

        button2=view.findViewById(R.id.button2);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
          originl=new LatLng(31.423281, 31.810107);
          destinationl=new LatLng(31.421743, 31.810869
          );

        LatLng latLng3=new LatLng(27.039551, 29.993143);

         latLngs.add(latLng3);
        latLngs.add(originl);
        latLngs.add(destinationl);

//
//        mapFragment.getMapAsync((OnMapReadyCallback) getContext());


        Retrofit retrofit=new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("https://maps.googleapis.com/")
                .build();
        apiInterface=retrofit.create(ApiInterface.class);

        if(polyline!=null)polyline.remove();
        PolylineOptions polylineOptions=new PolylineOptions().addAll(latLngs).clickable(true);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                mMap = googleMap;
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                mMap.setTrafficEnabled(true);
                getDirection("31.423281"+","+"31.810107","31.421743"+","+"31.810869");

                //polyline=mMap.addPolyline(polylineOptions);
                // Add a marker in Sydney and move the camera
               // LatLng sydney = new LatLng(-34, 151);
                //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
                /* for (LatLng latLng : latLngs) {
                    MarkerOptions markerOptions = new MarkerOptions().position(latLng);
                    mMap.addMarker(markerOptions);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
                }*/

            }});
        return view;
    }
private void getDirection(String origin,String destination){


        apiInterface.gitDirection("driving","less_driving",origin,destination, Data.KEYMAP)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Result>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull Result result) {
                        polyLineList=new ArrayList<>();
                        List<Route> routeList=result.getRoutes();
                        for(Route route:routeList){
                            String polyLine=route.getOverviewPolyline().getPoints();
                            polyLineList.addAll(decodePoly(polyLine));
                        }
                        polylineOptions=new PolylineOptions();
                        polylineOptions.color(R.color.text_color);
                        polylineOptions.width(8);
                        polylineOptions.startCap(new ButtCap());
                        polylineOptions.jointType(JointType.ROUND);
                        polylineOptions.addAll(polyLineList);
                        mMap.addPolyline(polylineOptions);
                        LatLngBounds.Builder builder=new LatLngBounds.Builder();
                        builder.include(originl);
                        builder.include(destinationl);
                        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(),100));


                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
}
private List<LatLng> decodePoly(String encoded){


        List<LatLng> poly=new ArrayList<>();
        int index=0 , len= encoded.length();
        int lat=0,lng=0;
    while(index<len){


        int b,shift=0,result=0;
        do {
            b = encoded.charAt(index++) - 63;
            result |= (b & 0x1f) << shift;
            shift += 5;
        }while (b>=0x20);
        int dlat=((result&1)!=0? ~(result>>1):(result>>1));
        lat+=dlat;
        shift=0;
        result =0;

        do {
            b = encoded.charAt(index++) - 63;
            result |= (b & 0x1f) << shift;
            shift += 5;
        }while (b>=0x20);
        int dlng=((result&1)!=0? ~(result>>1):(result>>1));
        lng+=dlng;

       LatLng p =new LatLng((((double) lat/1E5)),(((double) lng/1E5)));

       poly.add(p);
        }
    return poly;
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
/*




 */