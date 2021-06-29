package com.johnmelodyme.bluetoothutilities.activities;

/**
 * Copyright (c) 2021 John Melody Me
 * <p>
 * Developers own the copyright to software unless the developer is
 * the client's employee or the software is part of a larger work made
 * for hire under a written agreement. In order to own the copyright,
 * the client must have an agreement transferring ownership from the
 * developer to the client.
 *
 * @author: John Melody Me <Johnmelody@dingtalk.com>
 */

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.johnmelodyme.bluetoothutilities.Constant.BluetoothStatus;
import com.johnmelodyme.bluetoothutilities.Constant.LogLevel;
import com.johnmelodyme.bluetoothutilities.R;
import com.johnmelodyme.bluetoothutilities.functions.Functions;


public class MainActivity extends AppCompatActivity
{
    public static final LogLevel LOG_LEVEL = LogLevel.DEBUG;
    public Context context = MainActivity.this;
    public TextView status;

    /**
     * @param activity
     * required for rendering action bar within the
     * instance of the activity
     */
    public void render_action_bar(AppCompatActivity activity)
    {
        Functions.log_output("{:ok,  render_action_bar/1}", LOG_LEVEL);

        activity.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        activity.getSupportActionBar().setCustomView(R.layout.action_bar);
    }

    /**
     * @param bundle
     * required for user interface rendering at
     * beginning of the Instance.
     */
    public void render_user_interface(Bundle bundle)
    {
        Functions.log_output("{:ok, render_user_interface/1}", LOG_LEVEL);

        // Set Action bar to Center aligned
        render_action_bar(this);

        // Bluetooth Current Instance and Status
        status = findViewById(R.id.status);

        try
        {
            if (Functions.is_bluetooth_enabled(context) == 1)
            {
                status.setText(BluetoothStatus.BLUETOOTH_ENABLED.toString());

                Functions.log_output("{:ok, is_bluetooth_enabled/1, true}", LOG_LEVEL);
            }
            else
            {
                Functions.enable_bluetooth(this);
            }
        } catch (Exception e)
        {
            Functions.log_output(e.toString(), LOG_LEVEL);
        }

    }

    @Override
    protected void onStart()
    {
        super.onStart();

        Functions.log_output("{:ok, init_app/1}", LOG_LEVEL);

        // Get User Permissions instances
        Functions.get_user_permission(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        render_user_interface(savedInstanceState);
    }
}