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
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.androidproject.R;

public class DialogActivity2 extends AppCompatActivity {
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    private NotificationManager mNotifyManager;
    private static final int NOTIFICATION_ID = 0;
    public MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog2);
        createNotificationChannel();
        mediaPlayer= MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI);
        mediaPlayer.start();

        AlertDialog.Builder alertdialog = new AlertDialog.Builder(DialogActivity2.this);
        alertdialog.setCancelable(false);   // that make the dialog cant cancelled until u click inside the dialog itself
        alertdialog.setTitle("TRIP REMINDER");
        alertdialog.setMessage("Select Start , Snooze , Cancel.");


        alertdialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(DialogActivity2.this, "You pressed cancel.", Toast.LENGTH_SHORT).show();
                dialogInterface.cancel();
                mediaPlayer.stop();
                finish();
            }
        });

        alertdialog.setPositiveButton("Snooze", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(DialogActivity2.this, "You pressed snooze.", Toast.LENGTH_SHORT).show();
                sendNotification();
                mediaPlayer.stop();
                finish();
            }
        });

        alertdialog.setNeutralButton("Start", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                Toast.makeText(DialogActivity2.this, "you pressed start.", Toast.LENGTH_SHORT).show();
                //Intent intent1 = new Intent(DialogActivity2.this,MainActivity.class);
               // startActivity(intent1);
                mediaPlayer.stop();

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
        Intent intent = new Intent(this,DialogActivity2.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);
        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID)
                .setContentTitle("TRIP REMINDER")
                .setContentText("Start Now !")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        return notifyBuilder;

    }
}