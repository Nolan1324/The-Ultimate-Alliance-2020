package com.nolankuza.theultimatealliance.util;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import com.nolankuza.theultimatealliance.structure.DeviceInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Sync {
    public static final int GAMEDATA = 1;
    public static final int PITDATA = 2;
    public static final int PLAYOFFDATA = 8;
    public static final int MATCHES = 3;
    public static final int TEAMS = 4;
    public static final int ASSIGNMENTS = 5;
    public static final int SETTINGS = 6;
    public static final int STUDENTS = 7;

    public static List<BluetoothDevice> getDevices(BluetoothAdapter bAdapter) {
        List<BluetoothDevice> devices = new ArrayList<>(bAdapter.getBondedDevices());
        Collections.sort(devices, new BluetoothDeviceComparator());
        return devices;
    }

    public static List<DeviceInfo> getDeviceInfos(BluetoothAdapter bAdapter) {
        List<BluetoothDevice> devices = getDevices(bAdapter);
        List<DeviceInfo> deviceInfos = new ArrayList<>();
        for(BluetoothDevice d : devices) {
            deviceInfos.add(new DeviceInfo(d.getName()));
        }
        return deviceInfos;
    }

    private static class BluetoothDeviceComparator implements Comparator<BluetoothDevice> {
        @Override
        public int compare(BluetoothDevice bluetoothDevice, BluetoothDevice t1) {
            return bluetoothDevice.getName().compareTo(t1.getName());
        }
    }
}
