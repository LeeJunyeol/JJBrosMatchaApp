package com.matcha.jjbros.matchaapp.user;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.matcha.jjbros.matchaapp.R;
import com.matcha.jjbros.matchaapp.common.Values;
import com.matcha.jjbros.matchaapp.entity.GenUser;
import com.matcha.jjbros.matchaapp.entity.User;
import com.matcha.jjbros.matchaapp.main.LoginActivity;
import com.matcha.jjbros.matchaapp.owner.LocationService;

/**
 * Created by jylee on 2016-08-27.
 */
public class UserSettingActivity extends AppCompatActivity{
    private TextView bookmarkAlertOnOff;
    private TextView neartruckAlertOnOff;
    private TextView eventCouponAlertOnOff;

    private LocationManager locationManager;
    boolean isGPSEnabled;

    private GenUser user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);

        user = (GenUser)getIntent().getParcelableExtra("user");

        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_user_setting);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        if (null != ab) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        bookmarkAlertOnOff = (TextView) findViewById(R.id.bookmark_alert_onoff);
        neartruckAlertOnOff = (TextView) findViewById(R.id.neartruck_alert_onoff);
        eventCouponAlertOnOff = (TextView) findViewById(R.id.event_coupon_alert_onoff);

        if(Values.USER_NEAR_TRUCK_ALERT==false) {
            neartruckAlertOnOff.setBackgroundResource(R.drawable.circle_white);
            neartruckAlertOnOff.setTextColor(getResources().getColor(R.color.black));
            neartruckAlertOnOff.setText("OFF");
        } else {
            neartruckAlertOnOff.setBackgroundResource(R.drawable.circle_green_a400);
            neartruckAlertOnOff.setTextColor(getResources().getColor(R.color.material_White));
            neartruckAlertOnOff.setText("ON");
        }

        bookmarkAlertOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "북마크 기능 준비 중입니다.", Toast.LENGTH_SHORT).show();
            }
        });

        neartruckAlertOnOff.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), UserLocationService.class);
                intent.putExtra("user", user);

                if(Values.USER_NEAR_TRUCK_ALERT==false){
                    locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
                    isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                    if (!isGPSEnabled) {
                        showSettingsAlert();
                    } else {
                        Values.USER_NEAR_TRUCK_ALERT = true;
                        neartruckAlertOnOff.setBackgroundResource(R.drawable.circle_green_a400);
                        neartruckAlertOnOff.setTextColor(getResources().getColor(R.color.material_White));
                        neartruckAlertOnOff.setText("ON");
                        startService(intent);
                    }

                } else {
                    Values.USER_NEAR_TRUCK_ALERT = false;
                    neartruckAlertOnOff.setBackgroundResource(R.drawable.circle_white);
                    neartruckAlertOnOff.setTextColor(getResources().getColor(R.color.black));
                    neartruckAlertOnOff.setText("OFF");
                    stopService(intent);
                }
            }
        });

        eventCouponAlertOnOff.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), UserCouponService.class);
                intent.putExtra("user", user);
                if(Values.USER_EVENT_PUSH_ALERT==false){
                    locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
                    isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                    if (!isGPSEnabled) {
                        showSettingsAlert();
                    } else {
                        Values.USER_EVENT_PUSH_ALERT = true;
                        eventCouponAlertOnOff.setBackgroundResource(R.drawable.circle_green_a400);
                        eventCouponAlertOnOff.setTextColor(getResources().getColor(R.color.material_White));
                        eventCouponAlertOnOff.setText("ON");
                        startService(intent);
                    }
                } else {
                    Values.USER_EVENT_PUSH_ALERT = false;
                    eventCouponAlertOnOff.setBackgroundResource(R.drawable.circle_white);
                    eventCouponAlertOnOff.setTextColor(getResources().getColor(R.color.black));
                    eventCouponAlertOnOff.setText("OFF");
                    stopService(intent);
                }
            }
        });
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(UserSettingActivity.this);

        alertDialog.setTitle("알림");
        alertDialog.setMessage("GPS 셋팅이 필요합니다.\n 설정창으로 가시겠습니까?");
        // OK 를 누르게 되면 설정창으로 이동합니다.
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                UserSettingActivity.this.startActivity(intent);
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
