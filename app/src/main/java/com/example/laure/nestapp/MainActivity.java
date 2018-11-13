package com.example.laure.nestapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, Switch.OnCheckedChangeListener{

    // Button/Switch declaration
    private Button burgerButton, menuSettingsButton, menuConnectButton, next_button, back_button, systemHaltButton;
    private Switch doorsSwitch, roofSwitch, extendPadSwitch, raisePadSwitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Button initializers
        next_button = (Button)findViewById(R.id.nextButton);
        back_button = (Button)findViewById(R.id.backButton);
        burgerButton = (Button)findViewById(R.id.burgerButton);
        menuConnectButton = (Button)findViewById(R.id.menuConnectBtn);
        menuSettingsButton = (Button)findViewById(R.id.menuSettingsBtn);
        systemHaltButton = (Button)findViewById(R.id.systemHaltButton);

        // Switch initializers
        doorsSwitch = (Switch)findViewById(R.id.doorsSwitch);
        roofSwitch = (Switch)findViewById(R.id.roofSwitch);
        extendPadSwitch = (Switch)findViewById(R.id.extendPadSwitch);
        raisePadSwitch = (Switch)findViewById(R.id.raisePadSwitch);

        // Button OnClickListeners
        next_button.setOnClickListener(this);
        back_button.setOnClickListener(this);
        burgerButton.setOnClickListener(this);
        menuSettingsButton.setOnClickListener(this);
        menuConnectButton.setOnClickListener(this);
        systemHaltButton.setOnClickListener(this);

        /// Switch OnClickListeners
        doorsSwitch.setOnCheckedChangeListener(this);
        roofSwitch.setOnCheckedChangeListener(this);
        extendPadSwitch.setOnCheckedChangeListener(this);
        raisePadSwitch.setOnCheckedChangeListener(this);

    }

    @Override
    public void onClick(View v) {
        String request = "";
        int camID = 1;

        // Change Request Code/ Request Parameters
        switch (v.getId()){

            case R.id.nextButton:
                request = "next_button";
                if(camID == 1){
                    camID += 1;
                }
                break;
            case R.id.backButton:
                request = "back_button";
                if(camID == 2){
                    camID -= 1;
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