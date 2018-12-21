package com.example.laure.nestapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.os.StrictMode;


// client activity imports
import android.os.AsyncTask;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;

import com.example.laure.nestapp.tcpclient.ClientListAdapter;
import com.example.laure.nestapp.tcpclient.TcpClient;

import java.util.ArrayList;



public class MainActivity extends AppCompatActivity implements View.OnClickListener, Switch.OnCheckedChangeListener{

    // Button/Switch declaration
    private TextView logTextView;
    private Button burgerButton, menuSettingsButton, menuConnectButton, menuDisconnectButton, menuDiagnosticButton, nextButton, backButton, systemHaltButton, logButton;
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

        final EditText editText = (EditText) findViewById(R.id.editText);
        Button send = (Button) findViewById(R.id.send_button);

        //relate the listView from java to the one created in xml
        mList = (ListView) findViewById(R.id.list);
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
        logTextView = (TextView) findViewById(R.id.logTextView);
        logTextView.setMovementMethod(new ScrollingMovementMethod());

        // Button initializers
        nextButton = (Button)findViewById(R.id.nextButton);
        backButton = (Button)findViewById(R.id.backButton);
        burgerButton = (Button)findViewById(R.id.burgerButton);
        menuConnectButton = (Button)findViewById(R.id.menuConnectBtn);
        menuDisconnectButton = (Button)findViewById(R.id.menuDisconnectBtn);
        menuDiagnosticButton = (Button)findViewById(R.id.menuDiagnosticBtn);
        menuSettingsButton = (Button)findViewById(R.id.menuSettingsBtn);
        systemHaltButton = (Button)findViewById(R.id.systemHaltButton);
        logButton = (Button)findViewById(R.id.logButton);
        RadioGroup dotGroup = (RadioGroup)findViewById(R.id.rgroup);
        backDot = (RadioButton)findViewById(R.id.back_dot);
        nextDot = (RadioButton)findViewById(R.id.next_dot);

        // Switch initializers
        doorsSwitch = (Switch)findViewById(R.id.doorsSwitch);
        roofSwitch = (Switch)findViewById(R.id.roofSwitch);
        extendPadSwitch = (Switch)findViewById(R.id.extendPadSwitch);
        raisePadSwitch = (Switch)findViewById(R.id.raisePadSwitch);

        // Button OnClickListeners
        nextButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
        burgerButton.setOnClickListener(this);
        menuSettingsButton.setOnClickListener(this);
        menuConnectButton.setOnClickListener(this);
        menuDisconnectButton.setOnClickListener(this);
        menuDiagnosticButton.setOnClickListener(this);
        systemHaltButton.setOnClickListener(this);
        logButton.setOnClickListener(this);

        /// Switch OnClickListeners
        doorsSwitch.setOnCheckedChangeListener(this);
        roofSwitch.setOnCheckedChangeListener(this);
        extendPadSwitch.setOnCheckedChangeListener(this);
        raisePadSwitch.setOnCheckedChangeListener(this);
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

            case R.id.burgerButton:

                //Show or hide menu
                if (menuConnectButton.getVisibility() == View.GONE){
                    menuConnectButton.setVisibility(View.VISIBLE);
                    menuDisconnectButton.setVisibility(View.VISIBLE);
                    menuDiagnosticButton.setVisibility(View.VISIBLE);
                    menuSettingsButton.setVisibility(View.VISIBLE);
                } else {
                    menuConnectButton.setVisibility(View.GONE);
                    menuDisconnectButton.setVisibility(View.GONE);
                    menuDiagnosticButton.setVisibility(View.GONE);
                    menuSettingsButton.setVisibility(View.GONE);
                }

                request = "burgerButton";
                break;
            case R.id.menuConnectBtn:
                request = "menuConnectBtn";
                // connect to the server
                new MainActivity.ConnectTask().execute("");
                break;
            case R.id.menuDisconnectBtn:
                request = "menuDisconnectBtn";
                // disconnect
                mTcpClient.stopClient();
                mTcpClient = null;
                // clear the data set
                arrayList.clear();
                // notify the adapter that the data set has changed.
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.menuSettingsBtn:
                request = "menuSettingsBtn";
                break;
            case R.id.menuDiagnosticBtn:
                request = "menuDiagnosticBtn";
                sendButtonMessage(request);
                break;
            case R.id.systemHaltButton:
                request = "systemHaltButton";
                sendButtonMessage(request);
                break;
            case R.id.logButton:
                request = "logButton";
                if (logTextView.getVisibility() == View.GONE){
                    logTextView.setVisibility(View.VISIBLE);
                    logButton.setText("»");
                } else {
                    logTextView.setVisibility(View.GONE);
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
                request = "doorsSwitch";
                sendButtonMessage(request);
                break;
            case R.id.roofSwitch:
                request = "roofSwitch";
                sendButtonMessage(request);
                break;
            case R.id.extendPadSwitch:
                request = "extendPadSwitch";
                sendButtonMessage(request);
                break;
            case R.id.raisePadSwitch:
                request = "raisePadSwitch";
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
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if (mTcpClient != null) {
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

    // connect/disconnect from server on button press
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menuConnectBtn:
                // connect to the server
                new MainActivity.ConnectTask().execute("");
                return true;
            case R.id.menuDisconnectBtn:
                // disconnect
                mTcpClient.stopClient();
                mTcpClient = null;
                // clear the data set
                arrayList.clear();
                // notify the adapter that the data set has changed.
                mAdapter.notifyDataSetChanged();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

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