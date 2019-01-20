package com.project.tenvinc.bt_hacknroll;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static android.bluetooth.BluetoothAdapter.STATE_CONNECTED;
import static java.util.UUID.fromString;

public class ScanningActivity extends AppCompatActivity implements BeaconConsumer {

    // DEBUG
    private long refTime = System.currentTimeMillis();

    private static final String IBEACON_LAYOUT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24";
    private BeaconManager manager;

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothGatt bluetoothGatt;
    private final static UUID SERIAL_SERVICE_UUID = fromString("0000dfb0-0000-1000-8000-00805f9b34fb");
    private final static UUID SERIAL_CHAR_UUID = fromString("0000dfb1-0000-1000-8000-00805f9b34fb");
    private BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                ring(gatt);
            }
        }

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == STATE_CONNECTED) {
                bluetoothGatt.discoverServices();
            }
        }
    };

    private final String TAG = "ScanningActivity";

    private final int REQUEST_ENABLE_BT = 123;
    private final int PERMISSION_REQUEST_COARSE_LOCATION = 12;

    private ListView beaconList;
    private List<NamedBeacon> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanning);

        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        validatePermissions(this);

        manager = BeaconManager.getInstanceForApplication(this);
        manager.setAndroidLScanningDisabled(true);
        manager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(IBEACON_LAYOUT));
        manager.setForegroundScanPeriod(10000);
        manager.setDebug(true);
        //manager.setBeaconSimulator(new MyBeaconSimulator());

        manager.bind(this);

        data = new ArrayList<>();
        beaconList = findViewById(R.id.beaconList);
        beaconList.setAdapter(new BeaconAdapter(this, data));
    }

    @Override
    public void onBeaconServiceConnect() {
        manager.removeAllRangeNotifiers();
        manager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> collection, Region region) {
                //data.clear();
                Log.d(TAG, "Current time is " + (System.currentTimeMillis() - refTime));
                Log.d(TAG, "Beacon size is " + collection.size());
                for (Beacon beacon : collection) {
                    Log.d(TAG, beacon.getBluetoothAddress());

                    NamedBeacon newEntry = new NamedBeacon(beacon.getBluetoothName(), beacon);
                    boolean found = false;
                    for (int i=0; i<data.size(); i++) {
                        if (data.get(i).getMacAddress() == newEntry.getMacAddress()) {
                            data.remove(i);
                            data.add(newEntry);
                            found = true;
                            break;
                        }
                    }

                    if (found == false) {
                        data.add(newEntry);
                    }
                }
                BeaconAdapter adapter = (BeaconAdapter) beaconList.getAdapter();
                adapter.setData(data);
                adapter.notifyDataSetChanged();
            }
        });

        try {
            manager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {    }
    }



    private void validatePermissions(final Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);
                builder.setTitle("This app requires location access");
                builder.setMessage("Please grant location access so this app can detect beacons.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            activity.requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                    PERMISSION_REQUEST_COARSE_LOCATION);
                        }
                    }
                });
                builder.show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "coarse location permission granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {

                        }
                    });
                    builder.show();
                }
                return;
            default:
                Log.e(TAG, "Something has gone wrong");
        }
    }

    public void startRingBeacon(String beaconMac) {
        BluetoothDevice bluetoothDevice = mBluetoothAdapter.getRemoteDevice(beaconMac);
        bluetoothGatt = bluetoothDevice.connectGatt(this, true, gattCallback);
    }

    public void ring(BluetoothGatt bluetoothGatt) {
        BluetoothGattService service = bluetoothGatt.getService(SERIAL_SERVICE_UUID);
        Log.d(TAG, service.toString());
        BluetoothGattCharacteristic characteristic =
                service.getCharacteristic(SERIAL_CHAR_UUID);
        Log.d(TAG, characteristic.toString());
        String data = "1";
        characteristic.setValue(data.getBytes());
        characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
        bluetoothGatt.writeCharacteristic(characteristic);
        Log.d(TAG, "finished sending data");
        bluetoothGatt.close();
    }
}
