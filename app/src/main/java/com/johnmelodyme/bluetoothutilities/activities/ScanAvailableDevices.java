package com.johnmelodyme.bluetoothutilities.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;

import com.johnmelodyme.bluetoothutilities.Constant.LogLevel;
import com.johnmelodyme.bluetoothutilities.R;
import com.johnmelodyme.bluetoothutilities.functions.Functions;

public class ScanAvailableDevices extends AppCompatActivity
{
    public static final LogLevel LOG_LEVEL = LogLevel.DEBUG;
    public static BluetoothAdapter bluetoothAdapter;
    public ListView listView;

    public void render_user_interface(Bundle bundle)
    {
        Functions.log_output("{:ok, render_user_interface/1}", LOG_LEVEL);

        listView = (ListView) findViewById(R.id.listview_scan);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_available_devices);

        // Set Action bar to Center aligned
        Functions.render_action_bar(this);

        // Render User Interfaces Component
        render_user_interface(savedInstanceState);
    }
}