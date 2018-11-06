package com.example.laure.nestapp;

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

    }
}
