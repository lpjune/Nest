package com.example.laure.nestapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button burgerButton, menuSettingsButton, menuConnectButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //start server connection

        // button listeners
//        int camID = 1;
//        backButton = (Button)findViewById(R.id.backButton);
//        nextButton = (Button)findViewById(R.id.nextButton);
        burgerButton = (Button)findViewById(R.id.burgerButton);
        menuConnectButton = (Button)findViewById(R.id.menuConnectBtn);
        menuSettingsButton = (Button)findViewById(R.id.menuSettingsBtn);

        burgerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menuConnectButton.getVisibility() == View.GONE){
                    menuConnectButton.setVisibility(View.VISIBLE);
                    menuSettingsButton.setVisibility(View.VISIBLE);
                } else {
                    menuConnectButton.setVisibility(View.GONE);
                    menuSettingsButton.setVisibility(View.GONE);
                }
            }
        });

//        backButton.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                if (camID == 1) {
//                    return;
//                }
//                else {
//                    camID -= 1;
//                    return camID;
//                }
//            }
//        });

//        nextButton.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                if (camID == 2) {
//                    return;
//                }
//                else {
//                    camID += 1;
//                    return camID;
//                }
//            }
//        });

    }
}
