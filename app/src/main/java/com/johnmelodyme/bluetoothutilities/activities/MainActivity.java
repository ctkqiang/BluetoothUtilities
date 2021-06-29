package com.johnmelodyme.bluetoothutilities.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.johnmelodyme.bluetoothutilities.Constant.Constant;
import com.johnmelodyme.bluetoothutilities.R;


public class MainActivity extends AppCompatActivity
{

    private static final String TAG = Constant.TAG_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}