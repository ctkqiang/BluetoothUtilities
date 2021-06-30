package com.johnmelodyme.bluetoothutilities.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

import com.johnmelodyme.bluetoothutilities.Constant.LogLevel;
import com.johnmelodyme.bluetoothutilities.R;
import com.johnmelodyme.bluetoothutilities.functions.Functions;

public class ShowWebView extends AppCompatActivity
{
    public static final LogLevel LOG_LEVEL = LogLevel.DEBUG;
    public WebView webView;

    /**
     * @param bundle required for user interface rendering at
     *               beginning of the Instance.
     */
    public void render_user_interface(@NonNull Bundle bundle)
    {
        Functions.log_output("{:ok, render_user_interface/1}", LOG_LEVEL);

        webView = (WebView) findViewById(R.id.wv);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_web_view);

        // Render User Interface
        render_user_interface(savedInstanceState);

        // Render WebView
        Functions.open_url_in_app(ShowWebView.this, savedInstanceState, webView);
    }
}