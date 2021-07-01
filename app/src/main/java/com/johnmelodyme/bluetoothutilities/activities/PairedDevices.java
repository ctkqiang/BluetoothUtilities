package com.johnmelodyme.bluetoothutilities.activities;

import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import com.johnmelodyme.bluetoothutilities.Constant.Constant;
import com.johnmelodyme.bluetoothutilities.Constant.LogLevel;
import com.johnmelodyme.bluetoothutilities.R;
import com.johnmelodyme.bluetoothutilities.functions.Functions;
import com.johnmelodyme.bluetoothutilities.model.DiscoveredDevices;
import com.johnmelodyme.bluetoothutilities.user_interface.BluetoothCustomAdapter;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;

public class PairedDevices extends AppCompatActivity implements SearchView.OnQueryTextListener
{
    public static final LogLevel LOG_LEVEL = LogLevel.DEBUG;
    public static UUID BLUETOOTH_UUID;
    public static BluetoothAdapter bluetoothAdapter;
    public BluetoothCustomAdapter bluetoothCustomAdapter;
    public BluetoothDevice bluetoothDevice;
    public ArrayList<DiscoveredDevices> paired_devices_list = new ArrayList<>();
    public ArrayList<DiscoveredDevices> devices_list = new ArrayList<>();
    public Set<BluetoothDevice> paired_devices;
    public ListView listView;
    public SearchView search;

    /**
     * @param bundle required for rendering user interface component
     */
    public void render_user_interface(Bundle bundle)
    {
        Functions.log_output("{:ok, render_user_interface/1}", LOG_LEVEL);

        listView = (ListView) findViewById(R.id.listview);
        listView.setOnItemClickListener(on_item_clicked);
        listView.setTextFilterEnabled(true);

        search = (SearchView) findViewById(R.id.search);
        search.setIconified(false);
        search.setClickable(true);
        search.clearFocus();
        search.setOnQueryTextListener(this);
        search.setOnCloseListener(() -> false);
        search.setQueryHint(Constant.SEARCH_BAR);
    }

    private final OnItemClickListener on_item_clicked = new OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            DiscoveredDevices discoveredDevices = devices_list.get(position);

            Functions.log_output(discoveredDevices.getAddress(), LOG_LEVEL);
        }
    };

    public void bluetooth_instance(Bundle bundle)
    {
        Functions.log_output("{:ok, init_bluetooth_instance/1}", LOG_LEVEL);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BLUETOOTH_UUID = UUID.fromString(Constant.BLUETOOTH_SERIAL);
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
                        new DiscoveredDevices(
                                name,
                                bluetoothDevice.getAddress(),
                                bluetoothDevice.getBondState(),
                                bluetoothDevice.getType(),
                                bluetoothDevice.getUuids()
                        )
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
                        "}\n\n",
                        LOG_LEVEL
                );

            }
            Functions.register_device(this, broadcastReceiver);
            render_list(devices_list, this);
        }
        else
        {
            String msg = "No Paired Devices Found";
            Functions.show_toast(msg, this);
            Functions.log_output(msg, LOG_LEVEL);
        }
    }

    /**
     * @param device device needed
     * @return RFCOMM
     * @throws IOException null
     */
    private BluetoothSocket create_bluetooth_socket(BluetoothDevice device) throws IOException
    {
        try
        {
            Method method = device.getClass().getMethod(
                    "createInsecureRfcommSocketToServiceRecord",
                    UUID.class
            );

            return (BluetoothSocket) method.invoke(device, BLUETOOTH_UUID);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return device.createRfcommSocketToServiceRecord(BLUETOOTH_UUID);
    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver()
    {

        /**
         * This method is called when the BroadcastReceiver is receiving an Intent
         * broadcast.  During this time you can use the other methods on
         * BroadcastReceiver to view/modify the current result values.  This method
         * is always called within the main thread of its process, unless you
         * explicitly asked for it to be scheduled on a different thread using
         * {@link Context registerReceiver(BroadcastReceiver,
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
         * bound using {@link Context #bindService(Intent, ServiceConnection, int) bindService()},
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
            }
        }
    };

    public void render_list(ArrayList<DiscoveredDevices> discoveredDevices, Context context)
    {
        Functions.log_output("{:ok, render_list/1}", LOG_LEVEL);

        bluetoothCustomAdapter = new BluetoothCustomAdapter(discoveredDevices, context);

        listView.setAdapter(bluetoothCustomAdapter);

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

    /**
     * Called when the user submits the query. This could be due to a key press on the
     * keyboard or due to pressing a submit button.
     * The listener can override the standard behavior by returning true
     * to indicate that it has handled the submit request. Otherwise return false to
     * let the SearchView handle the submission by launching any associated intent.
     *
     * @param query the query text that is to be submitted
     * @return true if the query has been handled by the listener, false to let the
     * SearchView perform the default action.
     */
    @Override
    public boolean onQueryTextSubmit(String query)
    {
        bluetoothCustomAdapter.getFilter().filter(query);

        return true;
    }

    /**
     * Called when the query text is changed by the user.
     *
     * @param newText the new content of the query text field.
     * @return false if the SearchView should perform the default action of showing any
     * suggestions if available, true if the action was handled by the listener.
     */
    @Override
    public boolean onQueryTextChange(String newText)
    {
        if (TextUtils.isEmpty(newText.toLowerCase()))
        {
            listView.clearTextFilter();
        }
        else
        {
            listView.setFilterText(newText);
        }

        return true;
    }

    /**
     * Called when pointer capture is enabled or disabled for the current window.
     *
     * @param hasCapture True if the window has pointer capture.
     */
    @Override
    public void onPointerCaptureChanged(boolean hasCapture)
    {
        listView.clearTextFilter();
    }
}