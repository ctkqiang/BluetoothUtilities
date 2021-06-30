package com.johnmelodyme.bluetoothutilities.user_interface;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.johnmelodyme.bluetoothutilities.R;
import com.johnmelodyme.bluetoothutilities.model.DiscoveredDevices;

import java.util.ArrayList;


public class BluetoothCustomAdapter extends ArrayAdapter<DiscoveredDevices> implements
                                                                            View.OnClickListener
{
    public Context context;
    public ArrayList<DiscoveredDevices> discoveredDevices;

    private static class ViewHolder
    {
        TextView name;
        TextView address;
        TextView type;
        TextView state;
        TextView uuid;
    }

    public BluetoothCustomAdapter(ArrayList<DiscoveredDevices> discovereddevices,
                                  @NonNull Context context)
    {
        super(context, R.layout.custom_devices_discovered, discovereddevices);
        this.discoveredDevices = discovereddevices;
        this.context = context;
    }

    @Override
    public void onClick(View v)
    {

    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        ViewHolder viewHolder = new ViewHolder();
        View result;
        DiscoveredDevices discovered = getItem(position);

        if (convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.custom_devices_discovered, parent, false);

            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.address = (TextView) convertView.findViewById(R.id.address);
            viewHolder.type = (TextView) convertView.findViewById(R.id.type);
            viewHolder.state = (TextView) convertView.findViewById(R.id.bond);
            viewHolder.uuid = (TextView) convertView.findViewById(R.id.uuid);

            result = convertView;

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.name.setText("Bluetooth Name: " + discovered.getName());
        viewHolder.address.setText("Bluetooth Address: " + discovered.getAddress());
        viewHolder.type.setText("Bluetooth Type: " + discovered.getType());
        viewHolder.state.setText("Bluetooth Bond State: " + discovered.getState());
        viewHolder.uuid.setText("Bluetooth UUID: \n" + discovered.getUuid());

        return convertView;
    }
}