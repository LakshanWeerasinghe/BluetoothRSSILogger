package com.example.bluetoothrssilogger;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_DISCOVER_BT = 1;

    TextView mStatusBluetoothTv, mPairedTv, mRssiTv;
    ImageView mBluetoothIv;
    Button mOnBtn, mOffBtn, mDiscoverableBtn, mPairedBtn, mViewRssiBtn;

    BluetoothAdapter bluetoothAdapter;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // widgets
        mStatusBluetoothTv = findViewById(R.id.statusBluetoothTv);
        mPairedTv = findViewById(R.id.pairedTv);
        mBluetoothIv = findViewById(R.id.bluetoothIv);
        mRssiTv = findViewById(R.id.rssiTv);
        mOnBtn = findViewById(R.id.onBtn);
        mOffBtn = findViewById(R.id.offBtn);
        mDiscoverableBtn = findViewById(R.id.discoverableBtn);
        mPairedBtn = findViewById(R.id.pairedBtn);
        mViewRssiBtn = findViewById(R.id.viewRssiBtn);

        // adapter
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        registerReceiver(receiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));


        // check if bluetooth is available or not
        if(bluetoothAdapter == null){
            mStatusBluetoothTv.setText("Bluetooth is not available");
        }
        else{
            mStatusBluetoothTv.setText("Bluetooth is available");
        }

        // set image according to bluetooth status(on/off)
        if(bluetoothAdapter.isEnabled()){
            mBluetoothIv.setImageResource(R.drawable.ic_action_on);
        }
        else{
            mBluetoothIv.setImageResource(R.drawable.ic_action_off);
        }

        // on button click
        mOnBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(!bluetoothAdapter.isEnabled()){
                    showToast("Turning On Bluetooth...");
                    // intent to on Bluetooth
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(intent, REQUEST_ENABLE_BT);
                }
                else {
                    showToast("Bluetooth is already on");
                }
            }
        });


        // off button click
        mOffBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bluetoothAdapter.isEnabled()){
                    bluetoothAdapter.disable();
                    showToast("Turning Bluetooth Off");
                    mBluetoothIv.setImageResource(R.drawable.ic_action_off);
                }
                else {
                    showToast("Bluetooth is already off");
                }
            }
        });

        // discoverable button click
        mDiscoverableBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!bluetoothAdapter.isDiscovering()){
                    showToast("Making Your Device Discoverable");
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                    startActivityForResult(intent, REQUEST_DISCOVER_BT);
                }
            }
        });

        // paired button click
        mPairedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bluetoothAdapter.isEnabled()){
                    mPairedTv.setText("Paired Devices");
                    Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
                    for(BluetoothDevice device: devices){
                        mPairedTv.append("\nDevice: " + device.getName() + ", " + device);
                    }
                }
                else {
                    // bluetooth is off so can't get paired devices
                    showToast("Turn on bluetooth to get paired devices");
                }
            }
        });

        // view rssi button click
        mViewRssiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bluetoothAdapter != null){
                    bluetoothAdapter.startDiscovery();
                }
            }
        });
    }


    private final BroadcastReceiver receiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)) {
                int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,Short.MIN_VALUE);
                String name = intent.getStringExtra(BluetoothDevice.EXTRA_NAME);
                String macAddress = intent.getStringExtra(BluetoothDevice.EXTRA_DEVICE);
                mRssiTv.append( "Device: " + name + " " + macAddress + " => " + rssi + "dBm\n");
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        switch (requestCode){
            case REQUEST_ENABLE_BT:
                if (resultCode == RESULT_OK){
                    // bluetooth is on
                    mBluetoothIv.setImageResource(R.drawable.ic_action_on);
                    showToast("Bluetooth is on");
                }
                else{
                    // user denied to turn bluetooth on
                    showToast("couldn't on bluetooth");
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // toast message function
    private void showToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}