package com.example.nest.StreamClient;

import android.app.Activity;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.example.nest.R;

public class StreamClient {
    // server computer's IPV4 Address
    private static String SERVER_IP;
    private static int SERVER_PORT;
    // different video stream URLs
    // EX:  "http://192.168.0.101:65432/"
    private String web_url = "http://" + SERVER_IP + ":" + SERVER_PORT + "/";
    // EX:  "http://192.168.0.101:65432/video_feed1"
    public String video_1 = web_url + "video_feed1";
    private String video_2 = web_url + "video_feed2";

    public static void getSERVER_IP(String ip) {
        StreamClient.SERVER_IP = ip;
    }

    public static void getSERVER_PORT(String port) {
        StreamClient.SERVER_PORT = Integer.parseInt(port);
    }

    public void startVideo(String url, Activity activity) {
        try {
            WebView myBrowser= activity.findViewById(R.id.webView);
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
