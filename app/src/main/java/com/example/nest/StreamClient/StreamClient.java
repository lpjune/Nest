package com.example.nest.StreamClient;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.nest.MainActivity;
import com.example.nest.R;

public class StreamClient {
    public Activity activity;
    public StreamClient(Activity context){
        activity = context;
    }

    // server computer's IPV4 Address
    private static String SERVER_IP;
    private static int SERVER_PORT;
    /// EX:  "http://192.168.0.101:65432/video_feed1"

    private String[] cameras = new String[]{"1", "2"};
    public int current_camera = 0;

    public static void getSERVER_IP(String ip) {
        StreamClient.SERVER_IP = ip;
    }

    public static void getSERVER_PORT(String port) {
        StreamClient.SERVER_PORT = Integer.parseInt(port);
    }

    public void startVideo() {
        try {
            String web_url = "http://" + this.SERVER_IP + ":" + this.SERVER_PORT + "/video_feed";
            String url = web_url + this.cameras[this.current_camera];
            WebView myBrowser= (this.activity).findViewById(R.id.webView);
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

    public boolean nextCamera() {
        if(current_camera == 0) {
            current_camera++;
            startVideo();
            return true;
        } else {
            return false;
        }
    }

    public boolean previousCamera() {
        if(current_camera == 1) {
            current_camera--;
            startVideo();
            return true;
        } else {
            return false;
        }
    }
}
