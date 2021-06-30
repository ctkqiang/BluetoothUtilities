package com.johnmelodyme.bluetoothutilities.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.johnmelodyme.bluetoothutilities.Constant.LogLevel;
import com.johnmelodyme.bluetoothutilities.R;
import com.johnmelodyme.bluetoothutilities.functions.Functions;

public class ShowWebView extends AppCompatActivity
{
    public static final LogLevel LOG_LEVEL = LogLevel.DEBUG;
    public WebView webView;
    public ProgressBar progressBar;

    /**
     * @param bundle required for user interface rendering at
     *               beginning of the Instance.
     */
    public void render_user_interface(@NonNull Bundle bundle)
    {
        Functions.log_output("{:ok, render_user_interface/1}", LOG_LEVEL);

        // Set Action bar to Center aligned
        Functions.render_action_bar(this);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        webView = (WebView) findViewById(R.id.wv);
        webView.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
                set_progress_bar_visibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url)
            {
                super.onPageFinished(view, url);
                set_progress_bar_visibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request,
                                        WebResourceError error)
            {
                super.onReceivedError(view, request, error);
                set_progress_bar_visibility(View.GONE);
            }
        });

        // Render WebView
        Functions.open_url_in_app(ShowWebView.this, bundle, webView);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_web_view);

        // Render User Interface
        render_user_interface(savedInstanceState);
    }

    private void set_progress_bar_visibility(int visibility)
    {
        if (progressBar != null)
        {
            Functions.log_output("{:ok, set_progress_bar_visibility/1}", LOG_LEVEL);

            progressBar.setVisibility(visibility);
        }
    }
}