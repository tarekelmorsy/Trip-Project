package com.example.androidproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.siddharthks.bubbles.FloatingBubbleConfig;
import com.siddharthks.bubbles.FloatingBubbleService;

import java.util.zip.Inflater;

public class SimpleService extends FloatingBubbleService {
    @Override
    protected FloatingBubbleConfig getConfig() {
        Context context = getApplicationContext();
        //TextView tvFloat = (TextView) LayoutInflater.from(this).inflate(R.id.tv_float, null);
        return new FloatingBubbleConfig.Builder()
                .bubbleIcon(getApplicationContext().getDrawable( R.drawable.icon_note))
                .removeBubbleIcon(getApplicationContext().getDrawable( com.siddharthks.bubbles.R.drawable.close_default_icon))
                .bubbleIconDp(70)
                .expandableView(getInflater().inflate(R.layout.sample_view,null ))
                .removeBubbleIconDp(54)
                .paddingDp(4)
                .borderRadiusDp(0)
                .physicsEnabled(true)
                .expandableColor(Color.WHITE)
                .triangleColor(0xFF215A64)
                .gravity(Gravity.LEFT)
                .build();


    }
    @Override
    protected boolean onGetIntent(@NonNull Intent intent) {
        // your logic to get information from the intent
      String    notes=intent.getStringExtra("note");
        Log.i(TAG, "onGetIntent: testttttttttttttttttttttttttttttttttttt"+notes);

        // tv.setText(notes);

        return true;
    }



}

