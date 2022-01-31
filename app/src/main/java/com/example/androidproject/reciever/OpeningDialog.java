package com.example.androidproject.reciever;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.Map;

public class OpeningDialog extends Worker {

    WorkerParameters workerParams;
    Context context;


    public OpeningDialog(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context=context;
        this.workerParams=workerParams;
    }

    @NonNull
    @Override
    public Result doWork() {

        String keyOfTrip = getInputData().getString("KEY");
        Log.i("Main", "onDoWork: "+keyOfTrip);
        Intent intent1 = new Intent(context,DialogActivity.class);
        intent1.putExtra("KEY",keyOfTrip);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   // important for start activity from no one
        context.startActivity(intent1);
        return Result.success();
    }
}