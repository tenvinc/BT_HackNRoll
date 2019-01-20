package com.project.tenvinc.bt_hacknroll;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class BeaconAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private AppCompatActivity context;
    private List<NamedBeacon> data;

    public BeaconAdapter(Context context, List<NamedBeacon> data) {
        inflater = LayoutInflater.from(context);
        this.context = (AppCompatActivity) context;
        this.data = data;
    }

    public void setData(List<NamedBeacon> data) {
        this.data = data;
    }

    public List<NamedBeacon> getData() { return data; }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup parent) {
        view = inflater.inflate(R.layout.list_beacon, parent, false);

        TextView macText = view.findViewById(R.id.macText);
        TextView nameText = view.findViewById(R.id.nameText);
        TextView distanceText = view.findViewById(R.id.approxDistance);
        Button ringBtn = view.findViewById(R.id.ringBtn);
        TextView rssiText = view.findViewById(R.id.rssi);

        macText.setText(data.get(i).getBeacon().getBluetoothAddress());
        nameText.setText(data.get(i).getName());

        int TxPower = -70;  //Constant from measurement
        double rssi = data.get(i).getRSSI();

        rssiText.setText(Double.toString(rssi));

        //distanceText.setText(Double.toString(rssi));
        double distance = data.get(i).getAltApproxDist(TxPower, rssi);
        if (distance < 2) {
            distanceText.setText("Very Near!");
            distanceText.setTextColor(Color.rgb(0, 200, 0));
        } else if (distance < 5) {
            distanceText.setText("Nearby.");
            distanceText.setTextColor(Color.rgb(204, 204, 0));
        } else if (distance != -1){
            distanceText.setText("Not Nearby");
            distanceText.setTextColor(Color.rgb(200, 0, 0));
        } else {
            distanceText.setText("Can't find");
            distanceText.setTextColor(Color.BLACK);
        }

        ringBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MainActivity","ring");
                ScanningActivity activity = (ScanningActivity) context;
                activity.startRingBeacon(data.get(i).getBeacon().getBluetoothAddress());
            }
        });

        return view;
    }
}
