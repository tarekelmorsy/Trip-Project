package com.example.androidproject.reciever;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.androidproject.R;
import com.example.androidproject.SimpleService;
import com.example.androidproject.data.Data;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.siddharthks.bubbles.FloatingBubblePermissions;

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
        //map.put("notes",dataForTrip[8]);

        // FirebaseDatabase.getInstance().getReference().child("trips"+user.getUid()).removeValue();
        //FirebaseDatabase.getInstance().getReference().child("trips"+user.getUid());
        FirebaseDatabase.getInstance().getReference().child("trips"+user.getUid()).
                orderByChild("alarm").equalTo(dataForTrip[0])
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot appleSnapshot: snapshot.getChildren()) {
                            appleSnapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        FirebaseDatabase.getInstance().getReference().child("history" + user.getUid()).push().setValue(map);
        DataForAlarm.deleteAlarmForOneTrip(map);

        Log.i("Main", "doWork: "+dataForTrip[2]);

        String lat = dataForTrip[2];
        Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q="+lat+"&mode=d"));
        intent.setPackage("com.google.android.apps.maps");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(intent);


        ///to open map


       /* FloatingBubblePermissions.startPermissionRequest((Activity) context);
        Intent intent= new Intent(getApplicationContext(), SimpleService.class);
        intent.putExtra("note",dataForTrip[8]);
        getApplicationContext().startService(intent);*/

        return Result.success();

    }
}
