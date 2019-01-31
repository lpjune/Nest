package com.example.laure.nestapp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;
import android.os.StrictMode;


// client activity imports
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.VideoView;

import com.example.laure.nestapp.tcpclient.ClientListAdapter;
import com.example.laure.nestapp.tcpclient.TcpClient;

import java.util.ArrayList;



public class MainActivity extends AppCompatActivity implements View.OnClickListener, Switch.OnCheckedChangeListener{

    // Button/Switch declaration
    private ConstraintLayout logView;
    private Button nextButton, backButton, systemHaltButton, logButton;
    private Switch doorsSwitch, roofSwitch, extendPadSwitch, raisePadSwitch;
    private RadioButton backDot, nextDot;

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


        final EditText editText = findViewById(R.id.editText);
        Button send = findViewById(R.id.send_button);

        //relate the listView from java to the one created in xml
        mList = findViewById(R.id.list);
        mAdapter = new ClientListAdapter(this, arrayList);
        mList.setAdapter(mAdapter);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String message = editText.getText().toString();

                //add the text in the arrayList
                arrayList.add("c: " + message);

                //sends the message to the server
                if (mTcpClient != null) {
                    mTcpClient.sendMessage(message);
                }

                //refresh the list
                mAdapter.notifyDataSetChanged();
                editText.setText("");
            }
        });


        // TextView initializers
        logView = findViewById(R.id.logView);


        // Button initializers
        nextButton = findViewById(R.id.nextButton);
        backButton = findViewById(R.id.backButton);
        systemHaltButton = findViewById(R.id.systemHaltButton);
        logButton = findViewById(R.id.logButton);
        RadioGroup dotGroup = findViewById(R.id.rgroup);
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

        /// Switch OnClickListeners
        doorsSwitch.setOnCheckedChangeListener(this);
        roofSwitch.setOnCheckedChangeListener(this);
        extendPadSwitch.setOnCheckedChangeListener(this);
        raisePadSwitch.setOnCheckedChangeListener(this);
    }


    public void connectToServer() {
        new MainActivity.ConnectTask().execute("");
        return;
    }


    public void showPopup(final View v) {
        final PopupMenu popup = new PopupMenu(MainActivity.this, v);
        popup.inflate(R.menu.burger_menu);
        popup.show();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(android.view.MenuItem item) {
                String request = "";
                switch (item.getItemId()) {
                    case R.id.menuConnectBtn:
                        popup.dismiss();
                        request = "menuConnectBtn";

                        LayoutInflater inflater = getLayoutInflater();
                        View dialoglayout = inflater.inflate(R.layout.connect_menu, null);


                        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
                        builder.setView(dialoglayout);
                        builder.setCancelable(false);

                        final EditText ip_text = (EditText)dialoglayout.findViewById(R.id.ipconnectText);
                        final EditText port_text = (EditText)dialoglayout.findViewById(R.id.portconnectText);

                        ip_text.setText("130.18.64.135");
                        port_text.setText("65432");

                        Button ip_button = (Button)dialoglayout.findViewById(R.id.ipConnectBtn);

                        final AlertDialog alertDialog = builder.create();

                        ip_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // ... original code here
                                String return_ip = ip_text.getText().toString();
                                String return_port = port_text.getText().toString();
                                
                                TcpClient.getSERVER_IP(return_ip);
                                TcpClient.getSERVER_PORT(return_port);

                                connectToServer();
                                alertDialog.dismiss();
                            }
                        });
                        alertDialog.show();
                        return true;

                    case R.id.menuDisconnectBtn:
                        request = "menuDisconnectBtn";
                        // disconnect
                        mTcpClient.stopClient();
                        mTcpClient = null;
                        // clear the data set
                        arrayList.clear();
                        // notify the adapter that the data set has changed.
                        mAdapter.notifyDataSetChanged();
                        return true;

                    case R.id.menuSettingsBtn:
                        request = "menuSettingsBtn";
                        return false;

                    case R.id.menuDiagnosticBtn:
                        request = "menuDiagnosticBtn";
                        sendButtonMessage(request);
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

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        String request = "";
        String checked = Boolean.toString(isChecked);

        // Change Request Code/ Request Parameters
        switch(buttonView.getId()){
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
        }

        request += checked;

        //SEND REQUEST HERE
        sendRequest(request);
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
//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//
//        if (mTcpClient != null) {
//            // if the client is connected, enable the connect button and disable the disconnect one
//            menuConnectButton.setEnabled(true);
//            menuDisconnectButton.setEnabled(false);
//        } else {
//            // if the client is disconnected, enable the disconnect button and disable the connect one
//            menuConnectButton.setEnabled(false);
//            menuDisconnectButton.setEnabled(true);
//        }
//
//        return super.onPrepareOptionsMenu(menu);
//    }


    // connect class
    public class ConnectTask extends AsyncTask<String, String, TcpClient> {

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

            //in the arrayList we add the messaged received from server
            arrayList.add(values[0]);
            // notify the adapter that the data set has changed. This means that new message received
            // from server was added to the list
            mAdapter.notifyDataSetChanged();
        }
    }
}