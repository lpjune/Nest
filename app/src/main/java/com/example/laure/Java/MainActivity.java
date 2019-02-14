package com.example.laure.Java;

import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.os.StrictMode;


// client activity imports
import android.os.AsyncTask;
import android.widget.ListView;

import com.example.laure.nestapp.R;
import com.example.laure.Java.tcpclient.ClientListAdapter;
import com.example.laure.Java.tcpclient.TcpClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public boolean server_status;
    private boolean on_off_status;
    // Button/Switch declaration
    private ConstraintLayout logView;
    private Button nextButton, backButton, systemHaltButton, logButton, onOffButton;
    private Switch doorsSwitch, roofSwitch, extendPadSwitch, raisePadSwitch;
    private RadioButton backDot, nextDot;
    private TextView connectionView;

    // client activity declarations
    private ListView mList;
    private ArrayList<String> arrayList;
    private ClientListAdapter mAdapter;
    private TcpClient mTcpClient;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // allows network connections
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        // client activity on create
        arrayList = new ArrayList<String>();



        //relate the listView from java to the one created in xml
        mList = findViewById(R.id.list);
        mAdapter = new ClientListAdapter(this, arrayList);
        mList.setAdapter(mAdapter);

        // TextView initializers
        logView = findViewById(R.id.logView);
        connectionView = findViewById(R.id.connectionView);


        // Button initializers
        nextButton = findViewById(R.id.nextButton);
        backButton = findViewById(R.id.backButton);
        systemHaltButton = findViewById(R.id.systemHaltButton);
        logButton = findViewById(R.id.logButton);
        RadioGroup dotGroup = findViewById(R.id.rgroup);
        backDot = findViewById(R.id.back_dot);
        nextDot = findViewById(R.id.next_dot);
        onOffButton = findViewById(R.id.onOffButton);

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
        onOffButton.setOnClickListener(this);

        /// Switch OnClickListeners
        doorsSwitch.setOnClickListener(this);
        roofSwitch.setOnClickListener(this);
        extendPadSwitch.setOnClickListener(this);
        raisePadSwitch.setOnClickListener(this);
    }

    public void postToast(CharSequence text) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public void connectToServer() {
        new ConnectTask().execute("");
        return;
    }

    public void displayConnected() {
        postToast("Connected");
        connectionView.setText("Connected");
        connectionView.setTextColor(Color.GREEN);
        return;
    }

    public void showPopup(final View v) {
        final PopupMenu popup = new PopupMenu(MainActivity.this, v);
        popup.inflate(R.menu.burger_menu);
        popup.show();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(android.view.MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menuConnectBtn:
                        popup.dismiss();

                        LayoutInflater inflater = getLayoutInflater();
                        View dialoglayout = inflater.inflate(R.layout.connect_menu, null);


                        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
                        builder.setView(dialoglayout);
                        builder.setCancelable(false);

                        final EditText ip_text = dialoglayout.findViewById(R.id.ipconnectText);
                        final EditText port_text = dialoglayout.findViewById(R.id.portconnectText);

                        ip_text.setText("192.168.0.7");
                        port_text.setText("65432");

                        Button ip_button = dialoglayout.findViewById(R.id.ipConnectBtn);
                        Button ip_exit_button = dialoglayout.findViewById(R.id.ipExitBtn);

                        final AlertDialog alertDialog = builder.create();

                        ip_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String return_ip = ip_text.getText().toString();
                                String return_port = port_text.getText().toString();

                                TcpClient.getSERVER_IP(return_ip);
                                TcpClient.getSERVER_PORT(return_port);

                                connectToServer();
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
                        return true;

                    case R.id.menuDisconnectBtn:
                        // clear the data set
                        arrayList.clear();
                        // notify the adapter that the data set has changed.
                        mAdapter.notifyDataSetChanged();
                        // disconnect
                        mTcpClient.stopClient();
                        mTcpClient = null;
                        connectionView.setText("Disconnected");
                        connectionView.setTextColor(Color.parseColor("#FFFFFF"));
                        postToast("Disconnected");
                        return true;

                    case R.id.menuSettingsBtn:
                        return false;

                    case R.id.menuDiagnosticBtn:
                        PopupMenu popup2 = new PopupMenu(MainActivity.this, v);
                        popup2.inflate(R.menu.diagnostic_menu);
                        popup2.show();
                        return true;

                    default:
                        Log.e("went to default", "went to default");
                        return false;
                }
                }
        });
    }


    @Override
    public void onClick(View v) {
        String request = "";

        // Change Request Code/ Request Parameters
        switch (v.getId()){

            case R.id.doorsSwitch:
                if (doorsSwitch.isChecked()){
                    request = "doorsSwitchOn";
                }
                else {
                    request = "doorsSwitchOff";
                }
                sendButtonMessage(request);
                break;
            case R.id.roofSwitch:
                if (roofSwitch.isChecked()){
                    request = "roofSwitchOn";
                }
                else {
                    request = "roofSwitchOff";
                }
                sendButtonMessage(request);
                break;
            case R.id.extendPadSwitch:
                if (extendPadSwitch.isChecked()){
                    request = "extendPadSwitchOn";
                }
                else {
                    request = "extendPadSwitchOff";
                }
                sendButtonMessage(request);
                break;
            case R.id.raisePadSwitch:
                if (raisePadSwitch.isChecked()){
                    request = "raisePadSwitchOn";
                }
                else {
                    request = "raisePadSwitchOff";
                }
                sendButtonMessage(request);
                break;

            case R.id.backButton:
                request = "backButton";
                nextDot.setChecked(false);
                backDot.setChecked(true);
                sendButtonMessage(request);
                break;

            case R.id.nextButton:
                request = "nextButton";
                backDot.setChecked(false);
                nextDot.setChecked(true);
                sendButtonMessage(request);
                break;

            case R.id.onOffButton:
                request = "switchPower";
                if(!on_off_status) {
                    onOffButton.setText("Turn Off");
                    on_off_status = true;
                }
                else {
                    onOffButton.setText("Turn On");
                    on_off_status = false;
                }
                sendButtonMessage(request);
                break;

            case R.id.systemHaltButton:
                request = "systemHaltButton";
                sendButtonMessage(request);
                break;

            case R.id.logButton:
                request = "logButton";
                if (logView.getVisibility() == View.GONE){
                    logView.setVisibility(View.VISIBLE);
                    logButton.setText("»");
                } else {
                    logView.setVisibility(View.GONE);
                    logButton.setText("«");
                }

                break;
        }

        //SEND REQUEST HERE
        sendRequest(request);
    }

    public void checkSwitchError(String message) {
        if(message.contains("Door Error")) {
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
        else {
            return;
        }
    }


    public boolean sendButtonMessage(String request) {
        try {
            mTcpClient.sendMessage(request);
            arrayList.add(request);
            return true;
        } catch (Exception e) {
            Log.e("TCP", "S: Error", e);
            return false;
        }

    }

    private void sendRequest(String requestCode){
        Toast.makeText(this,requestCode,Toast.LENGTH_SHORT).show();
    }


    // CLIENT ACTIVITY METHODS
    // pause server
    @Override
    protected void onPause() {
        super.onPause();

        // disconnect
        mTcpClient.stopClient();
        mTcpClient = null;

    }

    // enable connect/disconnect buttons
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Button menuConnectButton = findViewById(R.id.menuConnectBtn);
        Button menuDisconnectButton = findViewById(R.id.menuDisconnectBtn);
        getServerStatus();
        if (!server_status) {
            // if the client is connected, enable the connect button and disable the disconnect one

            menuConnectButton.setEnabled(true);
            menuDisconnectButton.setEnabled(false);
        } else {
            // if the client is disconnected, enable the disconnect button and disable the connect one
            menuConnectButton.setEnabled(false);
            menuDisconnectButton.setEnabled(true);
        }

        return super.onPrepareOptionsMenu(menu);
    }


    public void getServerStatus() {
        this.server_status = mTcpClient.mRun;
        return;
    }

    public String getTimestamp() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        String timestamp = simpleDateFormat.format(new Date());
        return timestamp;
    }

    public class ConnectTask extends AsyncTask<String, String, TcpClient> {

        private int progressCount = 0;

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
            String message = arrayList.get(arrayList.size()-1);
            if(message.contains("Error")){
                checkSwitchError(message);
            }
            getServerStatus();
            if(progressCount == 0){
                if(server_status){
                    displayConnected();
                    progressCount++;
                }}
        }
    }
}