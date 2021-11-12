package com.example.util;

import android.os.Handler;

import com.example.bluetoothrssilogger.ActivityTwo;

public class RSSILoggerThread implements Runnable {

    private final Handler handler = new Handler();
    private ActivityTwo activity;

    public RSSILoggerThread(ActivityTwo activity) {
        this.activity = activity;
    }

    @Override
    public void run() {
            activity.addRSSIValue(2);
//        while (activity.getLogStatus()) {
//            activity.addRSSIValue(2);
//            handler.postDelayed(this::run, 3000);
//        }
    }
}
