package com.johnmelodyme.bluetoothutilities.functions;

import android.util.Log;

import com.johnmelodyme.bluetoothutilities.Constant.Constant;
import com.johnmelodyme.bluetoothutilities.Constant.LogLevel;

public class Functions
{
    private static final String TAG = Constant.TAG_NAME;

    public static void log_output(String message, LogLevel logLevel)
    {
        if(logLevel == LogLevel.DEBUG)
        {
            Log.d(TAG, logLevel + message);
        }
    }
}
