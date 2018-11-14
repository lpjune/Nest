package com.example.laure.nestapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, Switch.OnCheckedChangeListener{

    // Button/Switch declaration
    private TextView logTextView;
    private Button burgerButton, menuSettingsButton, menuConnectButton, nextButton, backButton, systemHaltButton, logButton;
    private Switch doorsSwitch, roofSwitch, extendPadSwitch, raisePadSwitch;
    private RadioButton backDot, nextDot;
    private int cam = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // TextView initializers
        logTextView = (TextView) findViewById(R.id.logTextView);

        // Button initializers
        nextButton = (Button)findViewById(R.id.nextButton);
        backButton = (Button)findViewById(R.id.backButton);
        burgerButton = (Button)findViewById(R.id.burgerButton);
        menuConnectButton = (Button)findViewById(R.id.menuConnectBtn);
        menuSettingsButton = (Button)findViewById(R.id.menuSettingsBtn);
        systemHaltButton = (Button)findViewById(R.id.systemHaltButton);
        logButton = (Button)findViewById(R.id.logButton);
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
        cam = 1;
        switch (v.getId()){

            case R.id.backButton:
                request = "backButton";
                if(cam == 2){
                    cam--;
                    backDot.setSelected(true);
                }
                break;

            case R.id.nextButton:
                request = "nextButton";
                if(cam == 1){
                    cam++;
                    nextDot.setSelected(true);
                }
                break;

            case R.id.burgerButton:

                //Show or hide menu
                if (menuConnectButton.getVisibility() == View.GONE){
                    menuConnectButton.setVisibility(View.VISIBLE);
                    menuSettingsButton.setVisibility(View.VISIBLE);
                } else {
                    menuConnectButton.setVisibility(View.GONE);
                    menuSettingsButton.setVisibility(View.GONE);
                }

                request = "burgerButton";
                break;
            case R.id.menuConnectBtn:
                request = "menuConnectBtn";
                break;
            case R.id.menuSettingsBtn:
                request = "menuSettingsBtn";
                break;
            case R.id.systemHaltButton:
                request = "systemHaltButton";
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
                request = "doorsSwitch ";
                break;
            case R.id.roofSwitch:
                request = "roofSwitch ";
                break;
            case R.id.extendPadSwitch:
                request = "extendPadSwitch ";
                break;
            case R.id.raisePadSwitch:
                request = "raisePadSwitch ";
                break;
        }

        request += checked;

        //SEND REQUEST HERE
        sendRequest(request);
    }

    private void sendRequest(String requestCode){
        Toast.makeText(this,requestCode,Toast.LENGTH_SHORT).show();
    }

}