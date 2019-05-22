package com.example.nest;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.StrictMode;

import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RadioButton;

import com.example.nest.StreamClient.StreamClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import com.example.nest.TCPClient.ClientListAdapter;
import com.example.nest.TCPClient.TcpClient;
import org.jetbrains.annotations.NotNull;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    // client activity declarations
    private ListView mList;
    private ArrayList<String> arrayList;
    private ClientListAdapter mAdapter;
    private TcpClient mTcpClient;
    private StreamClient mStreamClient;

    private boolean server_status;
    private boolean on_off_status;

    private FloatingActionButton systemHaltButton;
    private Button nextButton, backButton, logButton, onOffButton;
    private SwitchCompat doorsSwitch, roofSwitch, extendPadSwitch, raisePadSwitch;
    private RadioButton backDot, nextDot;

    private ConstraintLayout logView;
    private TextView connectionView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Hide the status bar.
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // allows network connections
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //relate the listView from java to the one created in xml
        arrayList = new ArrayList<>();
        mList = findViewById(R.id.list);
        mAdapter = new ClientListAdapter(this, arrayList);
        mList.setAdapter(mAdapter);

        logView = findViewById(R.id.logView);
        connectionView = findViewById(R.id.connectionView);

        // Button initializers
        nextButton = findViewById(R.id.nextButton);
        backButton = findViewById(R.id.backButton);
        systemHaltButton = findViewById(R.id.systemHaltButton);
        logButton = findViewById(R.id.logButton);
        backDot = findViewById(R.id.back_dot);
        nextDot = findViewById(R.id.next_dot);

        // Switch initializers
        doorsSwitch = findViewById(R.id.doorsSwitch);
        roofSwitch = findViewById(R.id.roofSwitch);
        extendPadSwitch = findViewById(R.id.extendPadSwitch);
        raisePadSwitch = findViewById(R.id.raisePadSwitch);

        // Button OnClickListeners
        nextButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
        systemHaltButton.setOnClickListener(this);
        logButton.setOnClickListener(this);

        // Switch OnClickListeners
        doorsSwitch.setOnClickListener(this);
        roofSwitch.setOnClickListener(this);
        extendPadSwitch.setOnClickListener(this);
        raisePadSwitch.setOnClickListener(this);

        // ip of flask web server video
        String URL1 = "http://192.168.0.101:65432/video_feed1";
        String URL2 = "http://192.168.0.101:65432/video_feed2";

    }

    @Override
    protected void onPause() {
        super.onPause();

        // disconnect
        mTcpClient.stopClient();
        mTcpClient = null;
    }

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
            postToast("Could not get video: " + e);
        }
    }

    private void connectToServer() {
        new ConnectTask().execute("");
    }

    private void displayConnected() {
        postToast("Connected");
        connectionView.setText(R.string.connected);
        connectionView.setTextColor(Color.GREEN);
    }


    private void disconnectFromServer() {
        // clear the data set
        arrayList.clear();
        // notify the adapter that the data set has changed.
        mAdapter.notifyDataSetChanged();
        // disconnect
        mTcpClient.stopClient();
        mTcpClient = null;
        displayDisconnected();
    }


    private void displayDisconnected() {
        postToast("Disconnected");
        connectionView.setText(R.string.disconnected);
        connectionView.setTextColor(Color.parseColor("#FFFFFF"));
    }


    private void postToast(CharSequence text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onClick(View v) {
        String request = "";
        String URL1 = "http://192.168.0.101:65432/video_feed1";
        String URL2 = "http://192.168.0.101:65432/video_feed2";
        // switch case for different buttons
        switch(v.getId()) {
            case R.id.doorsSwitch:
                if(!server_status) {
                    doorsSwitch.setChecked(false);
                    postToast("Not connected to server");
                    break;
                }
                else {
                    request = ((doorsSwitch.isChecked()) ? "doorsSwitchOn" : "doorsSwitchOff");
                    sendButtonMessage(request);
                    break;
                }

            case R.id.roofSwitch:
                if(!server_status) {
                    roofSwitch.setChecked(false);
                    postToast("Not connected to server");
                    break;
                }
                else {
                    request = ((roofSwitch.isChecked()) ? "roofSwitchOn" : "roofSwitchOff");
                    sendButtonMessage(request);
                    break;
                }

            case R.id.extendPadSwitch:
                if(!server_status) {
                    extendPadSwitch.setChecked(false);
                    postToast("Not connected to server");
                    break;
                }
                else {
                    request = ((extendPadSwitch.isChecked()) ? "extendPadSwitchOn" : "extendPadSwitchOff");
                    sendButtonMessage(request);
                    break;
                }

            case R.id.raisePadSwitch:
                if(!server_status) {
                    raisePadSwitch.setChecked(false);
                    postToast("Not connected to server");
                    break;
                }
                else {
                    request = ((raisePadSwitch.isChecked()) ? "raisePadSwitchOn" : "raisePadSwitchOff");
                    sendButtonMessage(request);
                    break;
                }

            case R.id.backButton:
                request = "backButton";
                startVideo(URL1);
                nextDot.setChecked(false);
                backDot.setChecked(true);
                break;
//                if(!server_status) {
//                    nextDot.setChecked(false);
//                    backDot.setChecked(true);
//                    postToast("Not connected to server");
//                    break;
//                }
//                else {
//                    request = "backButton";
//                    startVideo(URL1);
//                    nextDot.setChecked(false);
//                    backDot.setChecked(true);
//                    break;
//                }

            case R.id.nextButton:
                request = "nextButton";
                startVideo(URL2);
                backDot.setChecked(false);
                nextDot.setChecked(true);
                break;
//                if(!server_status) {
//                    backDot.setChecked(true);
//                    nextDot.setChecked(false);
//                    postToast("Not connected to server");
//                    break;
//                }
//                else {
//                    request = "nextButton";
//                    startVideo(URL2);
//                    backDot.setChecked(false);
//                    nextDot.setChecked(true);
//                    break;
//                }

            case R.id.systemHaltButton:
                if(!server_status) {
                    postToast("Not connected to server");
                    break;
                }
                request = "systemHaltButton";
                sendButtonMessage(request);
                break;

            case R.id.logButton:
                request = "logButton";
                if (logView.getVisibility() == View.GONE) {
                    logView.setVisibility(View.VISIBLE);
                    logButton.setText("»");
                } else {
                    logView.setVisibility(View.GONE);
                    logButton.setText("«");
                }
                break;
        }
    }


    private void checkSwitchError(@NotNull String message) {
        if (message.contains("Door Error")) {
            boolean currentState = doorsSwitch.isChecked();
            doorsSwitch.setChecked(!currentState);
        }
        else if (message.contains("Roof Error")) {
            boolean currentState = roofSwitch.isChecked();
            roofSwitch.setChecked(!currentState);
        }
        else if (message.contains("Extend Error")) {
            boolean currentState = extendPadSwitch.isChecked();
            extendPadSwitch.setChecked(!currentState);
        }
        else if (message.contains("Raise Error")) {
            boolean currentState = raisePadSwitch.isChecked();
            raisePadSwitch.setChecked(!currentState);
        }
    }


    private void sendButtonMessage(String request) {
        try {
            mTcpClient.sendMessage(request);
            arrayList.add(request);
        } catch (Exception e) {
            Log.e("TCP", "S: Error", e);
        }
    }


    public void showBurgerPopup(final View v) {
        final PopupMenu burger_popup = new PopupMenu(this, v);
        burger_popup.getMenuInflater().inflate(R.menu.burger_menu, burger_popup.getMenu());
        burger_popup.show();
        burger_popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menuConnectBtn:
                        if(!server_status) {
                            showConnectAlert();
                            return true;
                        }
                        else {
                            postToast("Already connected to server");
                            return false;
                        }

                    case R.id.menuDisconnectBtn:
                        if(server_status) {
                            disconnectFromServer();
                            return true;
                        }
                        else {
                            postToast("Not connected to server");
                            return false;
                        }

                    case R.id.machineOnBtn:
                        String request = "switchPower";
                        if(server_status) {
                            if (!on_off_status) {
                                on_off_status = true;
                            } else {
                                on_off_status = false;
                            }
                            sendButtonMessage(request);
                            return true;
                        }
                        else {
                            postToast("Not connected to server");
                            return false;
                        }

                    case R.id.menuDiagnosticBtn:
                        showDiagnosticPopup(v);
                        return true;

                    case R.id.menuSettingsBtn:
                        //show settings
                        return true;

                    default:
                        return false;
                }
            }
        });
    }


    private void showDiagnosticPopup(View v) {
        PopupMenu diagnostic_popup = new PopupMenu(this, v);
        diagnostic_popup.getMenuInflater().inflate(R.menu.diagnostic_menu, diagnostic_popup.getMenu());
        diagnostic_popup.show();
    }


    private void showConnectAlert() {
        LayoutInflater inflater = getLayoutInflater();
        View connect_menu = inflater.inflate(R.layout.connect_menu, null);

        final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(MainActivity.this);
        builder.setView(connect_menu);
        builder.setCancelable(false);

        final EditText ip_text = connect_menu.findViewById(R.id.ipconnectText);
        final EditText port_text = connect_menu.findViewById(R.id.portconnectText);

        ip_text.setText("192.168.0.101");
        port_text.setText("65432");

        Button ip_button = connect_menu.findViewById(R.id.ipConnectBtn);
        Button ip_exit_button = connect_menu.findViewById(R.id.ipExitBtn);

        final AlertDialog alertDialog = builder.create();

        ip_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String return_ip = ip_text.getText().toString();
                String return_port = port_text.getText().toString();

                TcpClient.getSERVER_IP(return_ip);
                TcpClient.getSERVER_PORT(return_port);
                StreamClient.getSERVER_IP(return_ip);
                StreamClient.getSERVER_PORT(return_port);

                connectToServer();
                mStreamClient.startVideo(mStreamClient.video_1, MainActivity.this);

                alertDialog.dismiss();
            }
        });

        ip_exit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
        alertDialog.getWindow().setLayout(700, 500);
    }


    private void getServerStatus() {
        this.server_status = mTcpClient.mRun;
    }


    private String getTimestamp() {
        return new SimpleDateFormat("HH:mm:ss", Locale.US).format(new Date());
    }


    class ConnectTask extends AsyncTask<String, String, TcpClient> {

        private boolean alreadyConnected = false;

        @Override
        protected TcpClient doInBackground(String... message) {

            //we create a TCPClient object and
            mTcpClient = new TcpClient(new TcpClient.OnMessageReceived() {
                @Override
                //here the messageReceived method is implemented
                public void messageReceived(String message) {
                    //this method calls the onProgressUpdate
                    publishProgress(message);
                }
            });
            mTcpClient.run();
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            //Local time zone
            String timestamp = getTimestamp();
            //in the arrayList we add the messaged received from server
            arrayList.add(timestamp + ": " + values[0]);
            // notify the adapter that the data set has changed. This means that new message received
            // from server was added to the list
            mAdapter.notifyDataSetChanged();
            String message = arrayList.get(arrayList.size() - 1);
            if (message.contains("Error")) {
                checkSwitchError(message);
            }
            getServerStatus();
            if (!alreadyConnected) {
                if (server_status) {
                    displayConnected();
                    alreadyConnected = true;
                }
            }
        }
    }
}
