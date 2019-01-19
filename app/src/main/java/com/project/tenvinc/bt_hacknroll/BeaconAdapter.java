package com.project.tenvinc.bt_hacknroll;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

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

        macText.setText(data.get(i).getBeacon().getBluetoothAddress());
        nameText.setText(data.get(i).getName());

        int TxPower = data.get(i).getTxPower();
        double rssi = data.get(i).getRSSI();
        distanceText.setText(Double.toString(data.get(i).getApproxDist(TxPower, rssi)));

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
