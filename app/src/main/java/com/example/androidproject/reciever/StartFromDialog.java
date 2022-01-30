package com.example.androidproject.reciever;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.androidproject.data.Data;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class StartFromDialog extends Worker {
    WorkerParameters workerParams;
    Context context;
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    FirebaseUser user;

    public StartFromDialog(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
        this.workerParams = workerParams;
    }


    @NonNull
    @Override
    public Result doWork() {

        user= firebaseAuth.getCurrentUser();

        String keyOfTrip = getInputData().getString("KEY");
        Log.i("Main", "Hello from StartfromDialog: "+keyOfTrip);

        String[] dataForTrip = keyOfTrip.split("&");
        Map<String, Object> map = new HashMap<>();

        map.put("alarm", dataForTrip[0]);
        map.put("date", dataForTrip[1]);
        map.put("endPoint", dataForTrip[2]);
        map.put("repeat", dataForTrip[3]);
        map.put("startPoint", dataForTrip[4]);
        map.put("status", Data.DONE);
        map.put("tripName", dataForTrip[6]);
        map.put("way", dataForTrip[7]);

       // FirebaseDatabase.getInstance().getReference().child("trips"+user.getUid()).removeValue();
        //FirebaseDatabase.getInstance().getReference().child("trips"+user.getUid());
        FirebaseDatabase.getInstance().getReference().child("history" + user.getUid()).push().setValue(map);


        return Result.success();

    }
}
