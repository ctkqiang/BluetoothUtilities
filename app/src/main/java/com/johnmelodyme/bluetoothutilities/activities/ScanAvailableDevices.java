package com.johnmelodyme.bluetoothutilities.activities;

import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.johnmelodyme.bluetoothutilities.Constant.BluetoothStatus;
import com.johnmelodyme.bluetoothutilities.Constant.Constant;
import com.johnmelodyme.bluetoothutilities.Constant.LogLevel;
import com.johnmelodyme.bluetoothutilities.R;
import com.johnmelodyme.bluetoothutilities.functions.Functions;
import com.johnmelodyme.bluetoothutilities.model.DiscoveredDevices;
import com.johnmelodyme.bluetoothutilities.user_interface.BluetoothCustomAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class ScanAvailableDevices extends AppCompatActivity
{
    public static final LogLevel LOG_LEVEL = LogLevel.DEBUG;
    public static UUID BLUETOOTH_UUID;
    public static BluetoothCustomAdapter bluetoothCustomAdapter;
    public static BluetoothAdapter bluetoothAdapter;
    public static BluetoothDevice bluetoothDevice;
    public ArrayList<DiscoveredDevices> discovered_devices_list = new ArrayList<>();
    public ArrayList<DiscoveredDevices> available_devices_list = new ArrayList<>();
    public ListView listView;
    public SearchView search;

    public void render_user_interface(Bundle bundle)
    {
        Functions.log_output("{:ok, render_user_interface/1}", LOG_LEVEL);

        listView = (ListView) findViewById(R.id.listview_scan);
        listView.setOnItemClickListener(on_item_clicked);

        search = (SearchView) findViewById(R.id.search_scan);
    }

    public void bluetooth_instance(Bundle bundle)
    {
        Functions.log_output("{:ok, bluetooth_instance}", LOG_LEVEL);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BLUETOOTH_UUID = UUID.fromString(Constant.BLUETOOTH_SERIAL);
    }

    public void scan_available_devices(Context context)
    {
        IntentFilter discoveryIntent;

        if (bluetoothAdapter.isDiscovering())
        {
            bluetoothAdapter.cancelDiscovery();
        }
        else
        {
            bluetoothAdapter.startDiscovery();

            discoveryIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            context.registerReceiver(broadcast_receiver, discoveryIntent);
        }
    }

    private final BroadcastReceiver broadcast_receiver = new BroadcastReceiver()
    {

        /**
         * This method is called when the BroadcastReceiver is receiving an Intent
         * broadcast.  During this time you can use the other methods on
         * BroadcastReceiver to view/modify the current result values.  This method
         * is always called within the main thread of its process, unless you
         * explicitly asked for it to be scheduled on a different thread using
         * {@link Context#registerReceiver(BroadcastReceiver,
         * IntentFilter, String, Handler)}. When it runs on the main
         * thread you should
         * never perform long-running operations in it (there is a timeout of
         * 10 seconds that the system allows before considering the receiver to
         * be blocked and a candidate to be killed). You cannot launch a popup dialog
         * in your implementation of onReceive().
         *
         * <p><b>If this BroadcastReceiver was launched through a &lt;receiver&gt; tag,
         * then the object is no longer alive after returning from this
         * function.</b> This means you should not perform any operations that
         * return a result to you asynchronously. If you need to perform any follow up
         * background work, schedule a {@link JobService} with
         * {@link JobScheduler}.
         * <p>
         * If you wish to interact with a service that is already running and previously
         * bound using ,
         * you can use {@link #peekService}.
         *
         * <p>The Intent filters used in {@link Context#registerReceiver}
         * and in application manifests are <em>not</em> guaranteed to be exclusive. They
         * are hints to the operating system about how to find suitable recipients. It is
         * possible for senders to force delivery to specific recipients, bypassing filter
         * resolution.  For this reason, {@link #onReceive(Context, Intent) onReceive()}
         * implementations should respond only to known actions, ignoring any unexpected
         * Intents that they may receive.
         *
         * @param context The Context in which the receiver is running.
         * @param intent  The Intent being received.
         */
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action))
            {
                bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                String uuid;
                String name;

                // Get Bluetooth UUID
                if (bluetoothDevice.getUuids() == null)
                {
                    uuid = "\t\"No UUID Found\"";
                }
                else
                {
                    uuid = (Arrays.toString(bluetoothDevice.getUuids()));
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

                if (bluetoothDevice != null)
                {
                    available_devices_list.add(
                            new DiscoveredDevices(
                                    name,
                                    bluetoothDevice.getAddress(),
                                    bluetoothDevice.getBondState(),
                                    bluetoothDevice.getType(),
                                    bluetoothDevice.getUuids()
                            )
                    );

                    discovered_devices_list.clear();
                    discovered_devices_list.addAll(available_devices_list);

                    Functions.log_output(
                            "\n\n{:ok, get_paired_devices/1" + "\n" +
                            "\tBluetooth Name: " + name + "\n" +
                            "\tBluetooth Class: " + bluetoothDevice.getBluetoothClass() + "\n" +
                            "\tBluetooth Address: " + bluetoothDevice.getAddress() + "\n" +
                            "\tBluetooth Type: " + bluetoothDevice.getType() + "\n" +
                            "\tBluetooth Bond State: " + bluetoothDevice.getBondState() + "\n" +
                            "\tBluetooth UUID: \n" + Arrays.toString(bluetoothDevice.getUuids()) +
                            "\n" +
                            "}\n",
                            LOG_LEVEL
                    );

                    render_list(available_devices_list, ScanAvailableDevices.this);
                }
            }

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED))
            {
                int state = intent.getIntExtra(
                        BluetoothAdapter.EXTRA_STATE,
                        BluetoothAdapter.ERROR
                );

                switch (state)
                {
                    case BluetoothAdapter.STATE_TURNING_OFF:
                    case BluetoothAdapter.STATE_OFF:
                    {
                        Functions.log_output(
                                BluetoothStatus.BLUETOOTH_DISABLED.toString(),
                                LOG_LEVEL
                        );
                        break;
                    }

                    case BluetoothAdapter.STATE_TURNING_ON:
                    case BluetoothAdapter.STATE_ON:
                    {
                        Functions.log_output(
                                BluetoothStatus.BLUETOOTH_ENABLED.toString(),
                                LOG_LEVEL
                        );
                        break;
                    }

                    default:
                    {
                        break;
                    }
                }
            }
        }
    };

    public void render_list(ArrayList<DiscoveredDevices> discoveredDevices, Context context)
    {
        Functions.log_output("{:ok, render_list/1}", LOG_LEVEL);

        bluetoothCustomAdapter = new BluetoothCustomAdapter(discoveredDevices, context);

        listView.setAdapter(bluetoothCustomAdapter);
    }

    public final AdapterView.OnItemClickListener on_item_clicked = (parent, view, position, id) ->
    {
        DiscoveredDevices discoveredDevices = available_devices_list.get(position);

        Functions.log_output(discoveredDevices.getAddress(), LOG_LEVEL);
    };


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_available_devices);

        // Set Action bar to Center aligned
        Functions.render_action_bar(this);

        // Render User Interfaces Component
        render_user_interface(savedInstanceState);

        // Initiate Bluetooth Instance
        bluetooth_instance(savedInstanceState);

        // Discovery Devices
        scan_available_devices(this);
    }
}