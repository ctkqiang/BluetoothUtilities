package com.johnmelodyme.bluetoothutilities.functions;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.content.pm.PackageManager;

import androidx.appcompat.app.AppCompatActivity;

import com.johnmelodyme.bluetoothutilities.Constant.Constant;
import com.johnmelodyme.bluetoothutilities.Constant.LogLevel;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class Functions
{
    public static final String TAG = Constant.TAG_NAME;
    public static BluetoothAdapter bluetoothAdapter;
    public static boolean is_bluetooth_supported;

    public static void log_output(String message, LogLevel logLevel)
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
    public static void get_user_permission(Context context)
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
    public static int is_bluetooth_module_exist(Context context)
    {
        is_bluetooth_supported =
                context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH);

        if (is_bluetooth_supported)
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
    public static int is_bluetooth_enabled(Context context)
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
    public static void enable_bluetooth(AppCompatActivity activity)
    {
        Intent on_enable_bluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

        log_output("{:ok, enable_bluetooth/1}", LogLevel.DEBUG);

        activity.startActivityForResult(on_enable_bluetooth, Constant.REQUEST_ENABLE_BLUETOOTH);
        activity.setResult(Activity.RESULT_CANCELED, on_enable_bluetooth);
    }
}
