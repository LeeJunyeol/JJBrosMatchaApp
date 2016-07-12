package com.matcha.jjbros.matchaapp.user;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.matcha.jjbros.matchaapp.R;


public class UserMainActivity extends AppCompatActivity {

    LocationManager locationManager;
    boolean isGPSEnabled;
    Button btn_my_near_foodtruck;
    Button btn_best_foodtruck;
    Button btn_category_drink_dessert;

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);



        btn_my_near_foodtruck = (Button) findViewById(R.id.btn_my_near_foodtruck);
        btn_best_foodtruck = (Button) findViewById(R.id.btn_best_foodtruck);
         btn_category_drink_dessert = (Button)findViewById(R.id.btn_category_drink_dessert);


        btn_my_near_foodtruck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
                isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);



                if (!isGPSEnabled) {
                    Toast.makeText(getApplicationContext(), "안되요", Toast.LENGTH_LONG).show();
                    showSettingsAlert();

                } else {
                    Toast.makeText(getApplicationContext(), "되요", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(),UserNearFoodtruckActivity.class);
                    startActivity(intent);


                }
            }
        });



        btn_best_foodtruck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), UserBestFoodtruckListActivity.class);
                startActivity(intent);


            }
        });

        btn_category_drink_dessert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), UserCategoryDrinkDessertActivity.class);
                startActivity(intent);


            }
        });




    }
}