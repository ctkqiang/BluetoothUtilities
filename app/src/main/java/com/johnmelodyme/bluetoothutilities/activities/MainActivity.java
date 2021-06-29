package com.johnmelodyme.bluetoothutilities.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.johnmelodyme.bluetoothutilities.Constant.BluetoothStatus;
import com.johnmelodyme.bluetoothutilities.Constant.Constant;
import com.johnmelodyme.bluetoothutilities.Constant.LogLevel;
import com.johnmelodyme.bluetoothutilities.R;
import com.johnmelodyme.bluetoothutilities.functions.Functions;

import java.util.function.Function;


public class MainActivity extends AppCompatActivity
{

    public static final String TAG = Constant.TAG_NAME;
    public static final LogLevel LOG_LEVEL = LogLevel.DEBUG;
    public TextView status;

    /**
     * @param bundle
     * required for user interface rendering at
     * beginning of the Instance.
     */
    public void render_user_interface(Bundle bundle)
    {
        Functions.log_output("Rendering User Interface Components", LOG_LEVEL);

        // Bluetooth Current Instance and Status
        status = (TextView) findViewById(R.id.status);
        status.setText(BluetoothStatus.BLUETOOTH_ENABLED.toString());
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        Functions.log_output("Starting Application", LOG_LEVEL);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        render_user_interface(savedInstanceState);
    }
}