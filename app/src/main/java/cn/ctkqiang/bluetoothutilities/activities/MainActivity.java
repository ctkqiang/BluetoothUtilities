package cn.ctkqiang.bluetoothutilities.activities;

/**
 * Copyright (c) 钟智强
 * <p>
 * Developers own the copyright to software unless the developer is
 * the client's employee or the software is part of a larger work made
 * for hire under a written agreement. In order to own the copyright,
 * the client must have an agreement transferring ownership from the
 * developer to the client.
 *
 * @author: 钟智强 <johnmelodymel@qq.com>
 */

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import cn.ctkqiang.bluetoothutilities.Constant.BluetoothStatus;
import cn.ctkqiang.bluetoothutilities.Constant.Constant;
import cn.ctkqiang.bluetoothutilities.Constant.LogLevel;
import cn.ctkqiang.bluetoothutilities.R;
import cn.ctkqiang.bluetoothutilities.functions.Functions;


public class MainActivity extends AppCompatActivity
{
    public static final LogLevel LOG_LEVEL = LogLevel.DEBUG;
    public Context context = MainActivity.this;
    public TextView status;
    public TextView process_view;


    /**
     * @param bundle required for user interface rendering at
     *               beginning of the Instance.
     */
    public void render_user_interface(@NonNull Bundle bundle)
    {
        Functions.log_output("{:ok, render_user_interface/1}", LOG_LEVEL);

        // Set Action bar to Center aligned
        Functions.render_action_bar(this);

        // Bluetooth Current Instance and Status
        status = (TextView) findViewById(R.id.status);
        process_view = (TextView) findViewById(R.id.process);

        // Get Bluetooth Status
        bluetooth_status(this);
    }


    @SuppressLint("SetTextI18n")
    private void bluetooth_status(@NonNull AppCompatActivity activity)
    {
        Thread newThread = new Thread(() ->
        {
            try
            {
                if (Functions.is_bluetooth_enabled(context) == 0x1)
                {
                    status.setText("STATUS: " + BluetoothStatus.BLUETOOTH_ENABLED.toString());
                    status.setTextColor(activity.getResources().getColor(R.color.main));

                    Functions.log_output("{:ok, is_bluetooth_enabled/1, true}", LOG_LEVEL);
                } else
                {
                    status.setText("STATUS: " + BluetoothStatus.BLUETOOTH_DISABLED.toString());
                    status.setTextColor(activity.getResources().getColor(R.color.red));
                    Functions.enable_bluetooth(MainActivity.this);
                }
            } catch (Exception e)
            {
                Functions.log_output(e.toString(), LOG_LEVEL);
            }
        });

        newThread.start();
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        Functions.log_output("{:ok, init_app/1}", LOG_LEVEL);

        // Get User Permissions instances
        Functions.get_user_permission(context);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        unregisterReceiver(bluetooth_state_receiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Render User Interface
        render_user_interface(savedInstanceState);

        // Initiate Broadcast registration
        Functions.register_filter(bluetooth_state_receiver, this);

        // Render Process
        // render_process(savedInstanceState, new String[]{"sh", "-l", "-c", "adb -d -s
        // bluetooth"});
    }

    @SuppressLint("NonConstantResourceId")
    public void onButtonClicked(View view)
    {
        Button clickedButton = (Button) view;

        switch (clickedButton.getId())
        {
            case R.id.onoffbutton:
            {
                Functions.bluetooth_toggle(this);
                break;
            }

            case R.id.setdiscover:
            {
                Functions.set_discoverable(this);
                break;
            }

            case R.id.paireddevices:
            {
                Functions.route_to(context, PairedDevices.class);
                break;
            }

            case R.id.scan_new_devices:
            {
                Functions.route_to(context, ScanAvailableDevices.class);
                break;
            }

            default:
            {
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_CANCELED)
        {
            Functions.show_toast(Constant.bluetooth_required, this);
        } else if (requestCode == RESULT_OK)
        {
            bluetooth_status(this);
        } else
        {
            bluetooth_status(this);
            Functions.show_toast("{:error, unknown_error/0}", this);
        }
    }


    public BroadcastReceiver bluetooth_state_receiver = new BroadcastReceiver()
    {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED))
            {
                int state = intent.getIntExtra(
                        BluetoothAdapter.EXTRA_STATE,
                        BluetoothAdapter.ERROR
                );

                switch (state)
                {
                    case BluetoothAdapter.STATE_OFF:
                    {
                        Functions.show_toast(
                                "{:ok, bluetooth_state/3, OFF}",
                                MainActivity.this
                        );

                        Functions.log_output(
                                "{:ok, bluetooth_state/3, OFF}",
                                LOG_LEVEL
                        );
                        break;
                    }

                    case BluetoothAdapter.STATE_TURNING_OFF:
                    {
                        Functions.show_toast(
                                "{:ok, bluetooth_state/3, TURNING_OFF}",
                                MainActivity.this
                        );

                        Functions.log_output(
                                "{:ok, bluetooth_state/3, TURNING_OFF}",
                                LOG_LEVEL
                        );

                        break;
                    }

                    case BluetoothAdapter.STATE_TURNING_ON:
                    {
                        Functions.show_toast(
                                "{:ok, bluetooth_state/3, TURNING_ON}",
                                MainActivity.this
                        );

                        Functions.log_output(
                                "{:ok, bluetooth_state/3, TURNING_ON}",
                                LOG_LEVEL
                        );
                        break;
                    }

                    case BluetoothAdapter.STATE_ON:
                    {
                        Functions.show_toast(
                                "{:ok, bluetooth_state/3, ON}",
                                MainActivity.this
                        );

                        Functions.log_output(
                                "{:ok, bluetooth_state/3, ON}",
                                LOG_LEVEL
                        );

                        status.setText("STATUS: " + BluetoothStatus.BLUETOOTH_ENABLED.toString());
                        status.setTextColor(getResources().getColor(R.color.main));
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_action_bar, menu);
        return true;
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.source_code:
            {
                Functions.parse_url(Constant.source_code_url, this, ShowWebView.class);
                return true;
            }

            case R.id.about:
            {
                Functions.parse_url(Constant.dev_profile_url, this, ShowWebView.class);
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public void render_process(Bundle bundle, String[] command)
    {
        process_view.setText(Functions.execute_command(this, command));
    }
}