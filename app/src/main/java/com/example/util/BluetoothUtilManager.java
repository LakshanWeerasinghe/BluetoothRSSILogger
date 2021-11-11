package com.example.util;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;

import java.util.Set;


public class BluetoothUtilManager {


    private static BluetoothUtilManager bluetoothUtilManager = null;
    private final BluetoothAdapter bluetoothAdapter;

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

    public boolean isBluetoothEnabled() {
        return bluetoothAdapter.isEnabled();
    }
}








































