package com.dialogdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jiajiewang on 16/6/14.
 */
public class BleEquipment extends BaseAdapter {
    ArrayList<BleInformation> list;
    Context context;

    public class ViewHolder {
        public TextView uuid;
        public TextView rssi;
        public TextView macAddress;
    }

    public BleEquipment(Context context, ArrayList<BleInformation> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {

            convertView = LayoutInflater.from(context).inflate(
                    R.layout.list_item_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.macAddress = (TextView) convertView.findViewById(
                    R.id.macAddress);
            viewHolder.rssi = (TextView) convertView.findViewById(
                    R.id.rssi);
            viewHolder.uuid = (TextView) convertView.findViewById(
                    R.id.uuid);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.uuid.setText(list.get(position).getUUID());
        viewHolder.macAddress.setText(list.get(position).getMacAddress());
        viewHolder.rssi.setText(list.get(position).getRSSI());
        return convertView;
    }
}
