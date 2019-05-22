package com.example.nest.StreamClient;

import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.nest.R;

public class StreamClient {
    // server computer's IPV4 Address
    private static String SERVER_IP;
    private static int SERVER_PORT;


    private void startVideo(String url) {
        try {
            WebView myBrowser=(WebView)findViewById(R.id.webView);
            WebSettings websettings = myBrowser.getSettings();
            websettings.setSupportZoom(true);
            websettings.setBuiltInZoomControls(true);
            websettings.setJavaScriptEnabled(true);
            myBrowser.setWebViewClient(new WebViewClient());
            myBrowser.loadUrl(url);
        } catch(Exception e) {
            Log.e("Stream Client", "SC: " + e);
        }
    }

}
