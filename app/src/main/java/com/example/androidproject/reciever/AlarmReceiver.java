package com.example.androidproject.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        Intent intent1 = new Intent(context,DialogActivity2.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   // important for start activity from no one
        context.startActivity(intent1);
    }
}