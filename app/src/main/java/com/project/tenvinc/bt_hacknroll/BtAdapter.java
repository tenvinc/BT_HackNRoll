package com.project.tenvinc.bt_hacknroll;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class BtAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private AppCompatActivity context;
    private List<String> data;

    public BtAdapter(Context context, List<String> data) {
        inflater = LayoutInflater.from(context);
        this.context = (AppCompatActivity) context;
        this.data = data;
    }

    public void setData(List<String> data) {
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
    public View getView(int i, View view, ViewGroup parent) {
        view = inflater.inflate(R.layout.list_bt, parent, false);

        TextView macText = view.findViewById(R.id.macText);

        macText.setText(data.get(i));

        return view;
    }
}
