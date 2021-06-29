package com.johnmelodyme.bluetoothutilities.functions;

import android.Manifest;
import android.content.Context;
import android.util.Log;

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
    private static final String TAG = Constant.TAG_NAME;

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
     * @param context
     * Current instance of the application are required for
     * getting user permission either accepted or denied.
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
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken pt)
            {
                log_output(pt.toString(), permission);
            }
        }).onSameThread().check();
    }
}
