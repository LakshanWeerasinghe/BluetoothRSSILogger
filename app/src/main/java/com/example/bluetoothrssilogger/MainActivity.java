package com.example.bluetoothrssilogger;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.util.BluetoothUtilManager;
import com.example.util.Constants;
import com.example.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private static final int REQUEST_ENABLE_BT = 0;

    Button onBtn, offBtn, scanBtn;
    TextView scanDevTv;
    ListView scanDevLv;

    ArrayAdapter<BluetoothDevice> arrayAdapter;

    private BluetoothUtilManager bluetoothUtilManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        onBtn = findViewById(R.id.onBtn);
        offBtn = findViewById(R.id.offBtn);
        scanBtn = findViewById(R.id.scanBtn);
        scanDevTv = findViewById(R.id.scanDevTv);
        scanDevLv = findViewById(R.id.scanDevLv);

        ArrayList<HashMap<String, BluetoothDevice>> arrayList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<BluetoothDevice>(this, android.R.layout.simple_list_item_1);
        scanDevLv.setAdapter(arrayAdapter);
        scanDevLv.setOnItemClickListener(this);

        bluetoothUtilManager = BluetoothUtilManager.getInstance();


        // on btn click
        onBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bluetoothUtilManager.switchOnBluetooth(MainActivity.this);
            }
        });

        // off btn click
        offBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothUtilManager.switchOffBluetooth(MainActivity.this);
            }
        });

        // scan devices btn click
        scanBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {

                arrayAdapter.clear();
                Set<BluetoothDevice> devices = bluetoothUtilManager
                        .getScanDevices(MainActivity.this);
                if (devices == null) {
                    scanDevTv.setText("");
                } else {
                    scanDevTv.setText("Scanned Devices");
                    for (BluetoothDevice device : devices) {
                        arrayAdapter.add(device);
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode == RESULT_OK) {
                    //bluetooth is on
                    Util.showToast(this, "Bluetooth is on");
                } else {
                    //user denied to turn bluetooth on
                    Util.showToast(this, "Couldn't on bluetooth");
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        BluetoothDevice bluetoothDevice = arrayAdapter.getItem(i);
        Util.showToast(this, "You selected: " + bluetoothDevice.getName());

        Intent intent = new Intent(MainActivity.this, ActivityTwo.class);
        intent.putExtra(Constants.DEVICE_MAC_ADDRESS, bluetoothDevice.getAddress());
        intent.putExtra(Constants.DEVICE_NAME, bluetoothDevice.getName());
        startActivity(intent);
    }


}