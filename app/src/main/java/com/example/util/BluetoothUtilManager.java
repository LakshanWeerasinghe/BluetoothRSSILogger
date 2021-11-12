package com.example.util;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Intent;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;
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

    public void scanForRssiValue(String macAddress, ArrayAdapter<String> arrayAdapter,
                                 List<String> rssiValueList) {
        if (bluetoothAdapter.isEnabled()) {

            // Changed by Kevin for the deprecated api call
            ArrayList<ScanFilter> scanFilters = new ArrayList<ScanFilter>() {{
                add(new ScanFilter.Builder().build());
            }};
            /*for (String macAddress : Constants.ALLOWED_MAC_ADDRESSES) {
                ScanFilter filter = new ScanFilter.Builder().setDeviceAddress(macAddress).build();
                scanFilters.add(filter);
            }*/
            ScanSettings settings = new ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).build();
            bluetoothAdapter.getBluetoothLeScanner().startScan(scanFilters, settings, new ScanCallback() {
                @Override
                public void onScanResult(int callbackType, ScanResult result) {
                    super.onScanResult(callbackType, result);
                    if (result.getScanRecord() == null || result.getScanRecord().getBytes() == null) {
                        return;
                    }

                    if (Util.isBeacon(result.getScanRecord().getBytes())) {
                        if(result.getDevice().getAddress().equals(macAddress)){
                            Integer rssi = result.getRssi();
                            rssiValueList.add(rssi.toString());
                            arrayAdapter.add(rssi.toString());
                        }

                    }
                }

                @Override
                public void onScanFailed(int errorCode) {
                    super.onScanFailed(errorCode);
                }
            });
        } else {
            bluetoothAdapter.getBluetoothLeScanner().stopScan(new ScanCallback() {
                @Override
                public void onScanResult(int callbackType, ScanResult result) {
                    super.onScanResult(callbackType, result);
                }
            });
        }

    }

    public boolean isBluetoothEnabled() {
        return bluetoothAdapter.isEnabled();
    }
}








































