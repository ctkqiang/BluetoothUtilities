package com.johnmelodyme.bluetoothutilities.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.johnmelodyme.bluetoothutilities.Constant.LogLevel;
import com.johnmelodyme.bluetoothutilities.R;
import com.johnmelodyme.bluetoothutilities.functions.Functions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;

public class PairedDevices extends AppCompatActivity
{
    public static final LogLevel LOG_LEVEL = LogLevel.DEBUG;
    public static BluetoothAdapter bluetoothAdapter;
    public ArrayList<String> paired_devices_list = new ArrayList<>();
    public ArrayList<String> devices_list = new ArrayList<>();
    public Set<BluetoothDevice> paired_devices;
    public ListView listView;
    public EditText search;

    public void render_user_interface(Bundle bundle)
    {
        Functions.log_output("{:ok, render_user_interface/1}", LOG_LEVEL);

        listView = (ListView) findViewById(R.id.listview);
    }

    public void bluetooth_instance(Bundle bundle)
    {
        Functions.log_output("{:ok, init_bluetooth_instance/1}", LOG_LEVEL);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public void get_paired_devices()
    {
        paired_devices = bluetoothAdapter.getBondedDevices();

        if (listView != null)
        {
            listView.clearChoices();
        }

        if (paired_devices != null)
        {
            Functions.log_output("{:ok, get_paired_devices/1}", LOG_LEVEL);

            for (BluetoothDevice bluetoothDevice : paired_devices)
            {
                devices_list.add(
                        "Bluetooth Name: " + bluetoothDevice.getName() + "\n\n" +
                        "Bluetooth Address: " + bluetoothDevice.getAddress() + "\n\n" +
                        "Bluetooth Type: " + bluetoothDevice.getType() + "\n\n" +
                        "Bluetooth Bond State: " + bluetoothDevice.getBondState() + "\n\n" +
                        "Bluetooth UUID: \n" + Arrays.toString(bluetoothDevice.getUuids()) + "\n\n"
                );

                paired_devices_list.addAll(devices_list);

                Functions.log_output(
                        "{:ok, get_paired_devices/1" +
                        "Bluetooth Name: " + bluetoothDevice.getName() + "\n" +
                        "Bluetooth Address: " + bluetoothDevice.getAddress() + "\n" +
                        "Bluetooth Type: " + bluetoothDevice.getType() + "\n" +
                        "Bluetooth Bond State: " + bluetoothDevice.getBondState() + "\n" +
                        "Bluetooth UUID: \n" + Arrays.toString(bluetoothDevice.getUuids()) + "\n" +
                        "}",
                        LOG_LEVEL
                );

            }
            render_list();
        }
        else
        {
            String msg = "No Paired Devices Found";
            Functions.show_toast(msg, this);
            Functions.log_output(msg, LOG_LEVEL);
        }

    }

    public void render_list()
    {
        Functions.log_output("{:ok, render_list/1}", LOG_LEVEL);

        ArrayAdapter<String> adapter;

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                paired_devices_list
        );

        listView.setAdapter(adapter);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        get_paired_devices();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paired_devices);

        // Render User Interface on
        render_user_interface(savedInstanceState);

        // Bluetooth Instance
        bluetooth_instance(savedInstanceState);

        // Render List
        get_paired_devices();
    }
}