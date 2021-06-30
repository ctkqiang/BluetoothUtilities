package com.johnmelodyme.bluetoothutilities.activities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.johnmelodyme.bluetoothutilities.Constant.LogLevel;
import com.johnmelodyme.bluetoothutilities.R;
import com.johnmelodyme.bluetoothutilities.functions.Functions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

public class PairedDevices extends AppCompatActivity
{
    public static final LogLevel LOG_LEVEL = LogLevel.DEBUG;
    public static BluetoothAdapter bluetoothAdapter;
    public ArrayList<String> paired_devices_list = new ArrayList<>();
    public ArrayList<String> devices_list = new ArrayList<>();
    public Set<BluetoothDevice> paired_devices;
    public ListView listView;
    public EditText search;

    /**
     * @param bundle required for rendering user interface component
     */
    public void render_user_interface(Bundle bundle)
    {
        Functions.log_output("{:ok, render_user_interface/1}", LOG_LEVEL);

        listView = (ListView) findViewById(R.id.listview);
        listView.setOnItemClickListener(on_item_clicked);

        search = (EditText) findViewById(R.id.search);
    }

    private final OnItemClickListener on_item_clicked = new OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {

            String item = (String) listView.getItemAtPosition(position);

            Functions.log_output(
                    "\n\n{:ok, on_item_clicked ->\n" + item + "[SELECTED], \n}\n",
                    LOG_LEVEL
            );

            Functions.write_into_file(item, PairedDevices.this);
        }
    };

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

                String uuid;
                String name;

                // Get Bluetooth UUID
                if (bluetoothDevice.getUuids() == null)
                {
                    uuid = "\t\"No UUID Found\"";
                }
                else
                {
                    uuid = (Arrays.toString(bluetoothDevice.getUuids()).trim());
                }

                // Get Bluetooth device Name
                if (bluetoothDevice.getName() == null)
                {
                    name = " \"NAME NOT FOUND \" ";
                }
                else
                {
                    name = bluetoothDevice.getName();
                }


                devices_list.add(
                        "\n" +
                        "Bluetooth Name: " + name + "\n\n" +
                        "Bluetooth Class: " + bluetoothDevice.getBluetoothClass() + "\n\n" +
                        "Bluetooth Address: " + bluetoothDevice.getAddress() +
                        "\n\n" +
                        "Bluetooth Type: " + bluetoothDevice.getType() + "\n\n" +
                        "Bluetooth Bond State: " + bluetoothDevice.getBondState() +
                        "\n\n" +
                        "Bluetooth UUID: \n\n" + uuid  + "\n\n"
                );

                paired_devices_list.clear();
                paired_devices_list.addAll(devices_list);

                Functions.log_output(
                        "\n\n{:ok, get_paired_devices/1" + "\n" +
                        "\tBluetooth Name: " + name + "\n" +
                        "\tBluetooth Class: " + bluetoothDevice.getBluetoothClass() + "\n" +
                        "\tBluetooth Address: " + bluetoothDevice.getAddress() + "\n" +
                        "\tBluetooth Type: " + bluetoothDevice.getType() + "\n" +
                        "\tBluetooth Bond State: " + bluetoothDevice.getBondState() + "\n" +
                        "\tBluetooth UUID: \n" + uuid +
                        "\n" +
                        "}\n",
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
        search_bluetooth_item(adapter);
    }

    public void search_bluetooth_item(ArrayAdapter<String> adapter)
    {
        search.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                adapter.getFilter().filter(s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                adapter.getFilter().filter(s);
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paired_devices);

        // Set Action bar to Center aligned
        Functions.render_action_bar(this);

        // Render User Interface on
        render_user_interface(savedInstanceState);

        // Bluetooth Instance
        bluetooth_instance(savedInstanceState);

        // Render List
        get_paired_devices();
    }
}