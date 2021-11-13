package com.example.util;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.util.Set;


public class BluetoothUtilManager {


    private static BluetoothUtilManager bluetoothUtilManager = null;
    private final BluetoothAdapter bluetoothAdapter;
    private Integer value;
    private Handler handler;
    private String macAddress;
    private final BroadcastReceiver receiver = createReceiver();


    private BluetoothUtilManager() {
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    // lazy instantiation of BluetoothManager
    public static BluetoothUtilManager getInstance() {
        if (bluetoothUtilManager == null) {
            synchronized (BluetoothUtilManager.class) {
                if (bluetoothUtilManager == null) {
                    bluetoothUtilManager = new BluetoothUtilManager();
                }
            }
        }
        return bluetoothUtilManager;
    }

    public void switchOnBluetooth(Activity activity) {
        if (!bluetoothAdapter.isEnabled()) {

            // intent to turn on bluetooth
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(intent, 0);
        } else {
            Util.showToast(activity, "Bluetooth is already on");
        }
    }

    public void switchOffBluetooth(Activity activity) {
        if (bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.disable();
        } else {
            Util.showToast(activity, "Bluetooth is already turned off");
        }
    }

    public Set<BluetoothDevice> getScanDevices(Activity activity) {
        if (bluetoothAdapter.isEnabled()) {
            return bluetoothAdapter.getBondedDevices();
        } else {
            Util.showToast(activity, "Turn on bluetooth to get scanned devices");
            return null;
        }
    }

    private BroadcastReceiver createReceiver() {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String action = intent.getAction();
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
                    BluetoothDevice device = intent
                            .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                    if (BluetoothUtilManager.this.macAddress.equals(device.getAddress())) {
                        Message m = Message.obtain(); //get null message
                        Bundle b = new Bundle();
                        b.putInt(Constants.RSSI_KEY, rssi);
                        m.setData(b);
                        BluetoothUtilManager.this.handler.sendMessage(m);
                    }
                }
            }
        };
    }

    public void scanDevices(Handler handler, String macAddress) {
        bluetoothAdapter.startDiscovery();
        this.handler = handler;
        this.macAddress = macAddress;
    }

    public BroadcastReceiver getReceiver() {
        return receiver;
    }


    public boolean isBluetoothEnabled() {
        return bluetoothAdapter.isEnabled();
    }

}








































