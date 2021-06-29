package com.johnmelodyme.bluetoothutilities.functions;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.content.pm.PackageManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.johnmelodyme.bluetoothutilities.Constant.BluetoothStatus;
import com.johnmelodyme.bluetoothutilities.Constant.Constant;
import com.johnmelodyme.bluetoothutilities.Constant.LogLevel;
import com.johnmelodyme.bluetoothutilities.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class Functions
{
    public static final String TAG = Constant.TAG_NAME;
    public static boolean b;

    public static void log_output(@NonNull String message, @NonNull LogLevel logLevel)
    {
        switch (logLevel)
        {
            case DEBUG:
            {
                Log.d(TAG, logLevel + message);
                break;
            }
            case INFO:
            {
                Log.i(TAG, logLevel + message);
                break;
            }
            case VERBOSE:
            {
                Log.v(TAG, logLevel + message);
                break;
            }
            default:
            {
                System.out.println(logLevel + message);
                break;
            }
        }
    }

    /**
     * @param context Current instance of the application are required for
     *                getting user permission either accepted or denied.
     */
    public static void get_user_permission(@NonNull Context context)
    {
        LogLevel permission = LogLevel.INFO;

        Dexter.withContext(context).withPermissions(
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION
        ).withListener(new MultiplePermissionsListener()
        {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport)
            {
                log_output(multiplePermissionsReport.toString(), permission);
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> l,
                                                           PermissionToken pt)
            {
                log_output(pt.toString(), permission);
            }
        }).onSameThread().check();
    }

    /**
     * @return int value 0 and 1
     * 0 : if bluetooth module is not found in the user device
     * 1 : if bluetooth module is found in the user device
     */
    public static int is_bluetooth_module_exist(@NonNull Context context)
    {
        b = context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH);

        if (b)
        {
            return 1;
        }
        else
        {
            return 0;
        }
    }

    /**
     * @return int value 0 and 1
     * 0: if bluetooth is disabled
     * 1: if bluetooth is enabled
     */
    public static int is_bluetooth_enabled(@NonNull Context context)
    {
        if (is_bluetooth_module_exist(context) == 1)
        {
            if (BluetoothAdapter.getDefaultAdapter().isEnabled())
            {
                return 1;
            }
            else
            {
                return 0;
            }
        }
        else
        {
            return 0;
        }
    }

    /**
     * @param activity Required for intent requesting User to enable Bluetooth
     */
    public static void enable_bluetooth(@NonNull AppCompatActivity activity)
    {
        Intent on_enable_bluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

        log_output("{:ok, enable_bluetooth/1}", LogLevel.DEBUG);

        activity.startActivityForResult(on_enable_bluetooth, Constant.REQUEST_ENABLE_BLUETOOTH);
        activity.setResult(Activity.RESULT_CANCELED, on_enable_bluetooth);
    }

    /**
     * @param activity Required for intent requesting User to Enable or Disable Bluetooth
     */
    @SuppressLint("SetTextI18n")
    public static void bluetooth_toggle(@NonNull AppCompatActivity activity)
    {
        activity.setContentView(R.layout.activity_main);

        TextView t = (TextView) activity.findViewById(R.id.status);
        String enable = BluetoothStatus.BLUETOOTH_ENABLED.toString();
        String disable = BluetoothStatus.BLUETOOTH_DISABLED.toString();


        if (BluetoothAdapter.getDefaultAdapter().isEnabled())
        {
            BluetoothAdapter.getDefaultAdapter().disable();
            t.setText("STATUS: " + BluetoothStatus.BLUETOOTH_DISABLED.toString());
            t.setTextColor(activity.getResources().getColor(R.color.red));

            log_output("{:ok, " + disable + "}", LogLevel.DEBUG);
        }
        else
        {
            BluetoothAdapter.getDefaultAdapter().enable();
            t.setText("STATUS: " + BluetoothStatus.BLUETOOTH_ENABLED.toString());
            t.setTextColor(activity.getResources().getColor(R.color.main));

            log_output("{:ok, " + enable + "}", LogLevel.DEBUG);
        }
    }

    /**
     * @param activity Required for intent requesting User to set discoverability
     */
    public static void set_discoverable(@NonNull AppCompatActivity activity)
    {
        Intent on_discover = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);

        log_output("{:ok, set_discoverable/1}", LogLevel.DEBUG);

        activity.startActivityForResult(on_discover, Constant.REQUEST_DISCOVERABLE);
        activity.setResult(Activity.RESULT_CANCELED, on_discover);
    }

    /**
     * @param message Required for developer log
     * @param context Required context for render toast.
     */
    public static void show_toast(@NonNull String message, Context context)
    {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * @param receiver required For registration
     * @param context  required for rendering
     */
    public static void register_filter(BroadcastReceiver receiver, Context context)
    {
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        context.registerReceiver(receiver, filter);
    }


    // TODO https://stackoverflow.com/questions/4715865/how-to-programmatically-tell-if-a
    //  -bluetooth-device-is-connected

}
