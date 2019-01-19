package com.project.tenvinc.bt_hacknroll;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class BtAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private AppCompatActivity context;

    public BtAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.context = (AppCompatActivity) context;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
