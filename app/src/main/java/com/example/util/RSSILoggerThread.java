package com.example.util;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.bluetoothrssilogger.ActivityTwo;

public class RSSILoggerThread implements Runnable {

    private final BluetoothUtilManager bluetoothUtilManager;
    private final Handler handler;
    private final ActivityTwo activityTwo;
    private final String macAddress;

    public RSSILoggerThread(Handler handler, ActivityTwo activityTwo, String macAddress) {
        this.bluetoothUtilManager = BluetoothUtilManager.getInstance();
        this.handler = handler;
        this.activityTwo = activityTwo;
        this.macAddress = macAddress;
    }

    @Override
    public void run() {
        while (activityTwo.getLogStatus()) {
            Integer rssi = bluetoothUtilManager.scanForRssiValue(macAddress);
            if (rssi != null){
                Message m = Message.obtain(); //get null message
                Bundle b = new Bundle();
                b.putInt(Constants.RSSI_KEY, rssi);
                m.setData(b);
                handler.sendMessage(m);
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
