package com.example.bluetoothrssilogger;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.util.BluetoothUtilManager;
import com.example.util.Constants;
import com.example.util.RSSILoggerThread;
import com.example.util.Util;

import java.util.List;

public class ActivityTwo extends AppCompatActivity {

    private final Handler handler = new Handler();
    private RSSILoggerThread rssiLoggerThread;

    private BluetoothUtilManager bluetoothUtilManager;
    private String selectedDeviceMACAddress;
    private String selectedDeviceName;

    private Button startLogBtn, stopLogBtn;
    private ListView logResultLv;

    private ArrayAdapter<Integer> arrayAdapter;
    private List<Integer> rssiValueList;
    private boolean log = false;

    private Runnable rssiLoggingThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);

        TextView activityTwoTitleTv = findViewById(R.id.activityTwoTitleTv);
        startLogBtn = findViewById(R.id.startLogBtn);
        stopLogBtn = findViewById(R.id.stopLogBtn);
        logResultLv = findViewById(R.id.logResultLv);
        logResultLv.setAdapter(arrayAdapter);

        Intent intent = getIntent();

        bluetoothUtilManager = BluetoothUtilManager.getInstance();

        rssiLoggerThread = new RSSILoggerThread(this);

        // check intent is null or not
        if (intent != null) {
            selectedDeviceMACAddress = intent.getStringExtra(Constants.DEVICE_MAC_ADDRESS);
            selectedDeviceName = intent.getStringExtra(Constants.DEVICE_NAME);
            activityTwoTitleTv.setText(selectedDeviceName);
        } else {
            Util.showToast(ActivityTwo.this, "Intent is null");
        }

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

    public void addRSSIValue(int value) {
        arrayAdapter.add(value);
        rssiValueList.add(value);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}