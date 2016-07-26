package com.matcha.jjbros.matchaapp.user;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.matcha.jjbros.matchaapp.R;



public class UserMainActivity extends AppCompatActivity {

    LocationManager locationManager;
    boolean isGPSEnabled;
    Button btn_my_near_foodtruck;
    Button btn_best_foodtruck;
    Button btn_category_drink_dessert;
    Toolbar toolbar;
    DrawerLayout dlDrawer;
    ActionBarDrawerToggle dtToggle;
    ListView listview;
    String[] navItems = {"Brown", "Cadet Blue", "Dark Olive Green", "Dark Orange", "Golden Rod"};


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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        dtToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        dtToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (dtToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        dlDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        listview = (ListView) findViewById(R.id.lv_activity_main_nav_list);
        listview.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, navItems));

        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (null != ab) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        dtToggle = new ActionBarDrawerToggle(this, dlDrawer, R.string.app_name, R.string.app_name);
        dlDrawer.setDrawerListener(dtToggle);





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

//        btn_category_drink_dessert.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(getApplicationContext(), UserCategoryDrinkDessertActivity.class);
//                startActivity(intent);
//
//
//            }
//        });




    }
}