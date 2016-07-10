package com.matcha.jjbros.matchaapp.user;

import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.matcha.jjbros.matchaapp.R;
import com.matcha.jjbros.matchaapp.common.GpsInfo;


public class UserMainActivity extends AppCompatActivity {

    LocationManager locationManager;
    boolean isGPSEnabled;
    boolean isNetworkEnabled;
    Button btn_my_near_foodtruck;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        btn_my_near_foodtruck = (Button)findViewById(R.id.btn_my_near_foodtruck);
        btn_my_near_foodtruck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                locationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
                isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                if (!isGPSEnabled && !isNetworkEnabled) {
                    Toast.makeText(getApplicationContext(),"안되요",Toast.LENGTH_LONG).show();

                }
            }
        });




    }
}