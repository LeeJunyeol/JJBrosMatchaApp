package com.matcha.jjbros.matchaapp.owner;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.matcha.jjbros.matchaapp.R;
import com.matcha.jjbros.matchaapp.common.Values;
import com.matcha.jjbros.matchaapp.entity.GenUser;
import com.matcha.jjbros.matchaapp.main.LoginActivity;
import com.matcha.jjbros.matchaapp.truck.FoodTruckMapActivity;

public class OwnerMainActivity extends AppCompatActivity{

    public static final int REQUEST_CODE_OWNERINFO = 1001;
    public static final int REQUEST_CODE_FOODTRUCKMAP = 1002;
    public static final int REQUEST_CODE_FOODMENUMNG = 1003;
    public static final int REQUEST_CODE_FESTINFO = 1004;
    private GenUser owner;
    private Toolbar tb_owner_main;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle dtToggle;

    private LocationManager locationManager;
    boolean isGPSEnabled;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_main);

        owner = (GenUser)getIntent().getParcelableExtra("owner");

        tb_owner_main = (Toolbar) findViewById(R.id.tb_owner_main);
        tb_owner_main.setTitle("메인");
        setSupportActionBar(tb_owner_main);

        // drawerLayout setting         //////////////////////////////////////////////
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_owner_main);

        dtToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(dtToggle);

        ActionBar ab = getSupportActionBar();
        if (null != ab) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
        ///////////////////////////////////////////////////////////////////////////////

        // Drawer Click Events
        TextView btnUpdate = (TextView)drawerLayout.findViewById(R.id.btn_myinfo_update);
        btnUpdate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), OwnerInfoActivity.class);
                intent.putExtra("owner", owner);
                startActivityForResult(intent, REQUEST_CODE_OWNERINFO);
            }
        });

        TextView btnLogout = (TextView)drawerLayout.findViewById(R.id.btn_myinfo_logout);
        btnLogout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                startActivityForResult(intent, REQUEST_CODE_OWNERINFO);
                finish();
            }
        });

        TextView btnNotice = (TextView)drawerLayout.findViewById(R.id.btn_myinfo_mynotice);
        btnNotice.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), OwnerNoticeActivity.class);
                startActivityForResult(intent, REQUEST_CODE_OWNERINFO);
            }
        });

        TextView btnCoupon = (TextView)drawerLayout.findViewById(R.id.btn_myinfo_coupon);
        btnCoupon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), OwnerCouponActivity.class);
                intent.putExtra("owner",owner);
                startActivityForResult(intent, REQUEST_CODE_OWNERINFO);
            }
        });

        TextView btnReview = (TextView)drawerLayout.findViewById(R.id.btn_myinfo_review_mng);
        btnReview.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), OwnerReviewActivity.class);
                startActivityForResult(intent, REQUEST_CODE_OWNERINFO);
            }
        });

        // 시간표 관리 버튼
        Button timeMngBtn = (Button) findViewById(R.id.timeMngButton);
        timeMngBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), OwnerTimeTableActivity.class);
                intent.putExtra("owner", owner);
                startActivityForResult(intent, REQUEST_CODE_OWNERINFO);
            }
        });

        // 판매 메뉴 관리 버튼
        Button foodMenuMngBtn = (Button) findViewById(R.id.foodMenuMngBtn);
        foodMenuMngBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), FoodMenuMngActivity.class);
                intent.putExtra("owner", owner);
                startActivityForResult(intent, REQUEST_CODE_FOODMENUMNG);
            }
        });


        // 푸드트럭 지도 버튼
        Button foodMapBtn = (Button) findViewById(R.id.foodMapButton);
        foodMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), FoodTruckMapActivity.class);
                intent.putExtra("owner", owner);
                intent.putExtra("loginType", Values.OWNER);
                startActivityForResult(intent, REQUEST_CODE_FOODTRUCKMAP);
            }
        });

        // 행사 정보 버튼
        Button festInfoBtn = (Button) findViewById(R.id.festInfoButton);
        festInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), FestInfoActivity.class);
                intent.putExtra("owner", owner);
                startActivityForResult(intent, REQUEST_CODE_FESTINFO);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_owner, menu);
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
        switch (item.getItemId()) {
            case R.id.mi_switch_my_location:
                if(item.isChecked()){
                    item.setChecked(false);
                    Intent intent = new Intent(getBaseContext(), LocationService.class);
                    stopService(intent);
                } else {
                    item.setChecked(true);

                    locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
                    isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

                    if (!isGPSEnabled) {
                        item.setChecked(false);
                        Toast.makeText(getApplicationContext(), "안되요", Toast.LENGTH_LONG).show();
                        showSettingsAlert();

                    } else {
                        Toast.makeText(getApplicationContext(), "되요", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getBaseContext(), LocationService.class);
                        intent.putExtra("owner", owner);
                        startService(intent);
                    }

                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(OwnerMainActivity.this);

        alertDialog.setTitle("알림");
        alertDialog.setMessage("GPS 셋팅이 필요합니다.\n 설정창으로 가시겠습니까?");
        // OK 를 누르게 되면 설정창으로 이동합니다.
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                OwnerMainActivity.this.startActivity(intent);
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

}
