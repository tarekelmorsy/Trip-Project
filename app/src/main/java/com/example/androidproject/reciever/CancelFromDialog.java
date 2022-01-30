package com.example.androidproject.reciever;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.androidproject.data.Data;
import com.example.androidproject.data.Trip;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CancelFromDialog extends Worker {
    WorkerParameters workerParams;
    Context context;
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    FirebaseUser user;

    public CancelFromDialog(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.workerParams = workerParams;
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {

        user= firebaseAuth.getCurrentUser();

        String keyOfTrip = getInputData().getString("KEY");
        Log.i("Main", "Hello From CancelfromDialog: "+keyOfTrip);

        String[] dataForTrip = keyOfTrip.split("&");
        Map<String, Object> map = new HashMap<>();

        map.put("alarm", dataForTrip[0]);
        map.put("date", dataForTrip[1]);
        map.put("endPoint", dataForTrip[2]);
        map.put("repeat", dataForTrip[3]);
        map.put("startPoint", dataForTrip[4]);
        map.put("status", Data.CANCEL);
        map.put("tripName", dataForTrip[6]);
        map.put("way", dataForTrip[7]);

        //FirebaseDatabase.getInstance().getReference().child("trips"+user.getUid()).removeValue();
        FirebaseDatabase.getInstance().getReference().child("history" + user.getUid()).push().setValue(map);

        FirebaseDatabase.getInstance().getReference().child("tripCancel" + user.getUid()).push().setValue(map)
                .addOnSuccessListener(unused ->
                        Toast.makeText(context, "Trip Cancel is Successfully.", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Error while Cancel", Toast.LENGTH_SHORT).show();
                });

        return Result.success();
    }
}
