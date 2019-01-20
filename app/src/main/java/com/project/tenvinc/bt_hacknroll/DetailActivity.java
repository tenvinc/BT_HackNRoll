package com.project.tenvinc.bt_hacknroll;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements KeepDialogListener{
    private List<String> data = new ArrayList<>();
    private FusedLocationProviderClient mFusedLocationClient;
    private TextView detailView;
    private TextView nameView;
    private TextView majorView;
    private TextView uuidView;
    private TextView minorView;
    private TextView macView;
    private final int PERMISSION_REQUEST_COARSE_LOCATION = 12;
    private final int PERMISSION_REQUEST_FINE_LOCATION = 13;
    private String TAG = "DetailActivity";
    private int pos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        Button locationBtn = findViewById(R.id.locationBtn);
        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationBtLe(true);
                AddDialog dialog = new AddDialog();
                Bundle data = new Bundle();
                data.putInt("pos", pos);
                dialog.setArguments(data);
                dialog.show(getSupportFragmentManager(), "add to tracked");
            }
        });
        validatePermissions(this);

        Intent intent = getIntent();
        nameView = findViewById(R.id.nameView);
        majorView = findViewById(R.id.majorView);
        uuidView = findViewById(R.id.uuidView);
        minorView = findViewById(R.id.minorView);
        macView = findViewById(R.id.macView);
        detailView = findViewById(R.id.detailView);
        pos = intent.getIntExtra("pos", -1);


        nameView.setText(intent.getStringExtra("name"));
        macView.setText(intent.getStringExtra("mac"));
        uuidView.setText(intent.getStringExtra("uuid"));
        minorView.setText(intent.getStringExtra("minor"));
        majorView.setText(intent.getStringExtra("major"));

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

            if (activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);
                builder.setTitle("This app requires location access");
                builder.setMessage("Please grant location access so this app can detect beacons.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            activity.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    PERMISSION_REQUEST_FINE_LOCATION);
                        }
                    }
                });
                builder.show();
            }
        }
    }
    private void locationBtLe(boolean enable) {
        if (enable) {
            data.clear();
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            String locationString = Double.toString(location.getLatitude()) + " "
                                    + Double.toString(location.getLongitude());
                            detailView.setText(locationString);
                            Log.d(TAG, Double.toString(location.getLatitude()));
                            Log.d(TAG, Double.toString(location.getLongitude()));
                        }
                    }
                });
    }

    @Override
    public void keep(String name, int i) {
        nameView.setText(name);
        NamedBeacon beacon = DataCentre.getInstance().beacons.get(i);
        beacon.setName(name);
        DataCentre.getInstance().trackedBeacons.add(i, beacon);
    }
}