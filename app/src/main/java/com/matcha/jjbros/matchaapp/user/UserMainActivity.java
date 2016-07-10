package com.matcha.jjbros.matchaapp.user;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.matcha.jjbros.matchaapp.R;
import com.matcha.jjbros.matchaapp.common.GpsInfo;


public class UserMainActivity extends AppCompatActivity {

    LocationManager locationManager;
    boolean isGPSEnabled;
    boolean isNetworkEnabled;
    Button btn_my_near_foodtruck;

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(UserMainActivity.this);

        alertDialog.setTitle("알림");
        alertDialog.setMessage("GPS 셋팅이 필요합니다.\n 설정창으로 가시겠습니까?");
        // OK 를 누르게 되면 설정창으로 이동합니다.
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                UserMainActivity.this.startActivity(intent);
            }
        });
        // Cancle 하면 종료 합니다.
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    private class GPSListener implements LocationListener {

        public void onLocationChanged(Location location) {
            //capture location data sent by current provider
            Double latitude = location.getLatitude();
            Double longitude = location.getLongitude();

        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        btn_my_near_foodtruck = (Button) findViewById(R.id.btn_my_near_foodtruck);
        btn_my_near_foodtruck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
                isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);


                if (!isGPSEnabled && !isNetworkEnabled) {
                    Toast.makeText(getApplicationContext(), "안되요", Toast.LENGTH_LONG).show();
                    showSettingsAlert();

                } else {
                    Toast.makeText(getApplicationContext(), "되요", Toast.LENGTH_LONG).show();


                }
            }
        });




    }
}