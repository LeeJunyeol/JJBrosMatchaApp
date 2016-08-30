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
import android.widget.TextView;
import android.widget.Toast;
import com.matcha.jjbros.matchaapp.R;
import com.matcha.jjbros.matchaapp.common.Values;
import com.matcha.jjbros.matchaapp.entity.GenUser;
import com.matcha.jjbros.matchaapp.main.LoginActivity;
import com.matcha.jjbros.matchaapp.truck.FoodTruckMapActivity;
import com.matcha.jjbros.matchaapp.truck.UserNearFoodtruckActivity;


public class UserMainActivity extends AppCompatActivity {
    Button btn_truck_map;
    LocationManager locationManager;
    boolean isGPSEnabled;
    Button btn_my_near_foodtruck;
    Button btn_best_foodtruck;
    Button btn_category_drink_dessert;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle dtToggle;
    ListView listview;

    GenUser user;

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
        getMenuInflater().inflate(R.menu.main_user, menu);
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

        user = (GenUser) getIntent().getParcelableExtra("user");
        Values.GENUSER = user;

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (null != ab) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        // drawerLayout setting         //////////////////////////////////////////////
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_user_main);

        dtToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(dtToggle);

        btn_my_near_foodtruck = (Button) findViewById(R.id.btn_my_near_foodtruck);
        btn_best_foodtruck = (Button) findViewById(R.id.btn_best_foodtruck);
        btn_category_drink_dessert = (Button)findViewById(R.id.btn_category_drink_dessert);
        btn_truck_map = (Button)findViewById(R.id.btn_truck_map);

        btn_truck_map.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),FoodTruckMapActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("loginType", Values.USER);
                startActivity(intent);
            }
        });

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
                    intent.putExtra("user", user);
                    intent.putExtra("loginType", Values.USER);
                    startActivity(intent);
                }
            }
        });

        btn_best_foodtruck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserBestFoodtruckListActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("loginType", Values.USER);
                startActivity(intent);
            }
        });

        userMyInfoEvents();

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

    public void userMyInfoEvents(){
        // Drawer Click Events
        TextView btnUpdate = (TextView)drawerLayout.findViewById(R.id.btn_myinfo_update_user);
        btnUpdate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), UserMyinfoUpdateActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });

        TextView btnLogout = (TextView)drawerLayout.findViewById(R.id.btn_myinfo_logout_user);
        btnLogout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        TextView btnBookmark = (TextView)drawerLayout.findViewById(R.id.btn_myinfo_bookmark_user);
        btnBookmark.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), UserBookmarkTruckListActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });

        TextView btnCoupon = (TextView)drawerLayout.findViewById(R.id.btn_myinfo_coupon_user);
        btnCoupon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), UserCouponActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });

        TextView btnReview = (TextView)drawerLayout.findViewById(R.id.btn_myinfo_review_user);
        btnReview.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), UserReviewActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });

        TextView btnEventNotice = (TextView) drawerLayout.findViewById(R.id.btn_myinfo_event_notice_user);
        btnEventNotice.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), UserEventNoticeActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });

        TextView btnSetting = (TextView) drawerLayout.findViewById(R.id.btn_myinfo_setting_user);
        btnSetting.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), UserSettingActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        showCloseAlert();
    }

    public void showCloseAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(UserMainActivity.this);

        alertDialog.setTitle("알림");
        alertDialog.setMessage("종료하시겠습니까?");
        // OK 를 누르게 되면 설정창으로 이동합니다.
        alertDialog.setPositiveButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        // Cancle 하면 종료 합니다.
        alertDialog.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

}