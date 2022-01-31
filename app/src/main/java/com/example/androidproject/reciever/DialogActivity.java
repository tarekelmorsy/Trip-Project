package com.example.androidproject.reciever;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.example.androidproject.R;
import com.example.androidproject.data.Trip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class DialogActivity extends AppCompatActivity {
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    private NotificationManager mNotifyManager;
    private static final int NOTIFICATION_ID = 0;
    public MediaPlayer mediaPlayer;
    public String keyOfTrip ;

    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog2);
        createNotificationChannel();
        mediaPlayer= MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI);
        mediaPlayer.start();

        user= firebaseAuth.getCurrentUser();

        keyOfTrip = getIntent().getStringExtra("KEY");
        Log.i("Main", "onDialogeActivity: "+keyOfTrip);


        AlertDialog.Builder alertdialog = new AlertDialog.Builder(DialogActivity.this);
        alertdialog.setCancelable(false);   // that make the dialog cant cancelled until u click inside the dialog itself
        alertdialog.setTitle("TRIP REMINDER");
        alertdialog.setMessage("Select Start , Snooze , Cancel.");


        /*DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("trips"+ Data.USER.getUid());
        databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                for (DataSnapshot dataSnapshot : task.getResult().getChildren()) {
                    Trip trip = dataSnapshot.getValue(Trip.class);
                    arrayTrips.add(trip);
                }
                Log.i("Main", "onArray:size= "+arrayTrips.size() );
                for( int i =0 ; i<arrayTrips.size();i++){
                    Trip trip = arrayTrips.get(i);
                    String check = trip.getDate()+trip.getAlarm();
                    Log.i("Main", "onArray: "+check );

                    if ( check.equals(key)){
                        myTrip=trip;
                    }
                }
            }
        });
*/


        alertdialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                sendActionForCancel(keyOfTrip);
                Toast.makeText(DialogActivity.this, "You pressed cancel.", Toast.LENGTH_SHORT).show();
                dialogInterface.cancel();
                mediaPlayer.stop();
                finish();

            }
        });

        alertdialog.setPositiveButton("Snooze", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(DialogActivity.this, "You pressed snooze.", Toast.LENGTH_SHORT).show();
                sendNotification();
                mediaPlayer.stop();
                finish();
            }
        });

        alertdialog.setNeutralButton("Start", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                Toast.makeText(DialogActivity.this, "you pressed start.", Toast.LENGTH_SHORT).show();
                sendActionForStart(keyOfTrip);
                mediaPlayer.stop();
               // finish();
            }
        });
        alertdialog.create().getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        alertdialog.show();

    }


    public void sendNotification(){
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();
        notifyBuilder.setOngoing(true); //to make it undragable from bar
        mNotifyManager.notify(NOTIFICATION_ID, notifyBuilder.build());
    }

    public void createNotificationChannel()
    {
        mNotifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // to check the version of API of device as notification channel statrt from API26 version_Codes_O ((O-> oreo))
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            // Create a NotificationChannel with ID that declared above and name that name for
            // the notification setting in app setting ,, importance high for shows everywhere, makes noise and peeks.

            NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_CHANNEL_ID,
                    "Trip Notification", NotificationManager.IMPORTANCE_HIGH);

            //set up setting for notification of this channel
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setShowBadge(true);   // to show dot on app icon
            notificationChannel.setDescription("Notification from Trip");
            mNotifyManager.createNotificationChannel(notificationChannel);
        }
    }

    private NotificationCompat.Builder getNotificationBuilder(){

        Intent intent = new Intent(this, DialogActivity.class);
        intent.putExtra("KEY",keyOfTrip);
        Log.i("Main", "getNotificationBuilder: "+keyOfTrip);

        // flag is important to send data in pending intent
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID)
                .setContentTitle("TRIP REMINDER")
                .setContentText("Start Now !")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        return notifyBuilder;

    }

    private void sendActionForCancel (String keyOfTrip ){
        Data dataInput = new Data.Builder().putString("KEY",keyOfTrip).build();
        WorkManager workManager = WorkManager.getInstance();
        WorkRequest workRequest = new OneTimeWorkRequest.Builder(CancelFromDialog.class)
                .addTag(keyOfTrip).setInputData(dataInput)
                .build();
        Log.i("Main", "onSendActionForCancel: "+keyOfTrip);
        workManager.enqueue(workRequest);
    }

    private void sendActionForStart (String keyOfTrip ){
        Data dataInput = new Data.Builder().putString("KEY",keyOfTrip).build();
        WorkManager workManager = WorkManager.getInstance();
        WorkRequest workRequest = new OneTimeWorkRequest.Builder(StartFromDialog.class)
                .addTag(keyOfTrip).setInputData(dataInput)
                .build();
        Log.i("Main", "onSendActionForCancel: "+keyOfTrip);
        workManager.enqueue(workRequest);
    }
}