package com.matcha.jjbros.matchaapp.owner;

import android.content.Intent;
import android.content.res.Configuration;
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
import android.widget.ListView;

import com.matcha.jjbros.matchaapp.R;
import com.matcha.jjbros.matchaapp.entity.GenUser;

public class OwnerMainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_OWNERINFO = 1001;
    public static final int REQUEST_CODE_FOODTRUCKMAP = 1002;
    public static final int REQUEST_CODE_FOODMENUMNG = 1003;
    public static final int REQUEST_CODE_FESTINFO = 1004;
    private GenUser owner;
    private Toolbar tb_owner_main;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle dtToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_main);

        owner = (GenUser)getIntent().getParcelableExtra("owner");

        tb_owner_main = (Toolbar) findViewById(R.id.tb_owner_main);
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
}
