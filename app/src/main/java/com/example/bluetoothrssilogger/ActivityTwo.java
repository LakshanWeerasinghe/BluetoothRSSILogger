package com.example.bluetoothrssilogger;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.util.BluetoothUtilManager;
import com.example.util.Constants;
import com.example.util.RSSILoggerThread;
import com.example.util.Util;

public class ActivityTwo extends AppCompatActivity {

    private RSSILoggerThread rssiLoggerThread;

    private BluetoothUtilManager bluetoothUtilManager;
    private String selectedDeviceMACAddress;
    private String selectedDeviceName;

    private Button startLogBtn, stopLogBtn;
    private ListView logResultLv;

    private ArrayAdapter<Integer> arrayAdapter;
    private boolean log = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);

        TextView activityTwoTitleTv = findViewById(R.id.activityTwoTitleTv);
        startLogBtn = findViewById(R.id.startLogBtn);
        stopLogBtn = findViewById(R.id.stopLogBtn);
        logResultLv = findViewById(R.id.logResultLv);

        Intent intent = getIntent();
        // check intent is null or not
        if (intent != null) {
            selectedDeviceMACAddress = intent.getStringExtra(Constants.DEVICE_MAC_ADDRESS);
            selectedDeviceName = intent.getStringExtra(Constants.DEVICE_NAME);
            activityTwoTitleTv.setText(selectedDeviceName);
        } else {
            Util.showToast(ActivityTwo.this, "Intent is null");
        }

        arrayAdapter = new ArrayAdapter<Integer>(
                this, android.R.layout.simple_list_item_1);

        logResultLv.setAdapter(arrayAdapter);



        bluetoothUtilManager = BluetoothUtilManager.getInstance();

        Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                Bundle bb = msg.getData();
                Integer rssiValue = bb.getInt(Constants.RSSI_KEY);
                activityTwoTitleTv.setText(rssiValue.toString());
                arrayAdapter.add(rssiValue);
            }
        };

        rssiLoggerThread = new RSSILoggerThread(handler, this, selectedDeviceMACAddress);



        startLogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                log = true;
                new Thread(rssiLoggerThread).start();
            }
        });

        stopLogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                log = false;
            }
        });
    }

    public boolean getLogStatus() {
        return log;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}