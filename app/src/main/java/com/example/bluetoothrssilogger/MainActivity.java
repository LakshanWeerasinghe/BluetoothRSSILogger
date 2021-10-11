package com.example.bluetoothrssilogger;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private static final int REQUEST_ENABLE_BT = 0;

    Button onBtn, offBtn, scanBtn;
    TextView scanDevTv;
    ListView scanDevLv;

    ArrayAdapter<BluetoothDevice> arrayAdapter;

    BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        onBtn = findViewById(R.id.onBtn);
        offBtn = findViewById(R.id.offBtn);
        scanBtn = findViewById(R.id.scanBtn);
        scanDevTv = findViewById(R.id.scanDevTv);
        scanDevLv = (ListView) findViewById(R.id.scanDevLv);

        ArrayList<HashMap<String,BluetoothDevice>> arrayList=new ArrayList<>();
        arrayAdapter = new ArrayAdapter<BluetoothDevice>(this, android.R.layout.simple_list_item_1);
        scanDevLv.setAdapter(arrayAdapter);
        scanDevLv.setOnItemClickListener(this);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // on btn click
        onBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!bluetoothAdapter.isEnabled()) {
                    showToast("Turning on bluetooth...");
                    // intent to on bluetooth
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(intent, REQUEST_ENABLE_BT);
                } else {
                    showToast("Bluetooth is already on");
                }
            }
        });

        // off btn click
        offBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bluetoothAdapter.isEnabled()) {
                    bluetoothAdapter.disable();
                    showToast("Turning bluetooth off");
                    // TODO: if user decide to off bluetooth
                } else {
                    showToast("Bluetooth is already off");
                }
            }
        });

        // scan devices btn click
        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bluetoothAdapter.isEnabled()) {
                    arrayAdapter.clear();
                    scanDevTv.setText("Scanned Devices");
                    Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
                    for(BluetoothDevice device: devices) {
                        arrayAdapter.add(device);
                    }
                } else {
                    // bluetooth is off so can't scan devices
                    arrayAdapter.clear();
                    scanDevTv.setText("");
                    showToast("Turn on bluetooth to get scanned devices");
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case REQUEST_ENABLE_BT:
                if(resultCode == RESULT_OK) {
                    //bluetooth is on
                    showToast("Bluetooth is on");
                } else {
                    //user denied to turn bluetooth on
                    showToast("Couldn't on bluetooth");
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        BluetoothDevice item = arrayAdapter.getItem(i);
        showToast("You selected: " + item.getName());

        Intent intent = new Intent(MainActivity.this, ActivityTwo.class);
        startActivity(intent);
    }

    //toast message function
    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}