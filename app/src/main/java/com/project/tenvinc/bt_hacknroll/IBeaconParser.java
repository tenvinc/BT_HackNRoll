package com.project.tenvinc.bt_hacknroll;

import android.bluetooth.BluetoothDevice;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class IBeaconParser {

    public static String LOG_TAG = "IBeaconParser";

    public class ScannedBleDevice implements Serializable {
        // public BluetoothDevice BLEDevice;

        /**
         * Returns the hardware address of this BluetoothDevice.
         * <p>
         * For example, "00:11:22:AA:BB:CC".
         *
         * @return Bluetooth hardware address as string
         */
        public String MacAddress;

        public String DeviceName;
        public double RSSI;
        public double Distance;

        public byte[] CompanyId;
        public byte[] IbeaconProximityUUID;
        public byte[] Major;
        public byte[] Minor;
        public byte Tx;

        public boolean isIBeacon;

        public long ScannedTime;

        public String toHex(byte[] bytes) {
            int arrLength = bytes.length;
            char[] hex = new char[2 * arrLength];
            int j = 0;
            for (int i=0; i < arrLength; i++) {
                hex[j] = Character.forDigit((bytes[i] >> 4) & 0xF, 16);
                hex[j+1] = Character.forDigit((bytes[i] & 0xF), 16);
                j+=2;
            }
            return new String(hex);
        }
    }

    // use this method to parse those bytes and turn to an object which defined proceeding.
// the uuidMatcher works as a UUID filter, put null if you want parse any BLE advertising data around.
    public ScannedBleDevice ParseRawScanRecord(BluetoothDevice device,
                                                int rssi, byte[] advertisedData, byte[] uuidMatcher) {
        try {
            ScannedBleDevice parsedObj = new ScannedBleDevice();
            // parsedObj.BLEDevice = device;
            parsedObj.DeviceName = device.getName();
            parsedObj.MacAddress = device.getAddress();
            parsedObj.RSSI = rssi;

            int magicStartIndex = 5;
            int cnt = 0;
            int i = magicStartIndex;
            parsedObj.CompanyId = new byte[2];
            while (cnt < 2) {
                parsedObj.CompanyId[cnt] = advertisedData[i];
                cnt++;
                i++;
            }

            cnt = 0;
            byte[] identifier = new byte[2];
            while (cnt < 2) {
                identifier[cnt] = advertisedData[i];
                i++;
                cnt++;
            }
            if (parsedObj.toHex(identifier).equals("0215")) {
                Log.d("IBeaconParser","ibeacon found");
                parsedObj.isIBeacon = true;
            } else {
                parsedObj.isIBeacon = false;
            }

            cnt = 0;
            parsedObj.IbeaconProximityUUID = new byte[16];
            while (cnt < 16) {
                parsedObj.IbeaconProximityUUID[cnt] = advertisedData[i];
                i++;
                cnt++;
            }

            cnt = 0;
            parsedObj.Major = new byte[2];
            while (cnt < 2) {
                parsedObj.Major[cnt] = advertisedData[i];
                i++;
                cnt++;
            }

            cnt = 0;
            parsedObj.Minor = new byte[2];
            while (cnt < 2) {
                parsedObj.Minor[cnt] = advertisedData[i];
                cnt++;
                i++;
            }

            parsedObj.Tx = advertisedData[i];

            parsedObj.ScannedTime = new Date().getTime();
            return parsedObj;
        } catch (Exception ex) {
            Log.e(LOG_TAG, "skip one unknown format data...");
            // Log.e(LOG_TAG,
            // "Exception in ParseRawScanRecord with advertisedData: "
            // + Util.BytesToHexString(advertisedData, " ")
            // + ", detail: " + ex.getMessage());
            return null;
        }
    }
}
