package com.matcha.jjbros.matchaapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OwnerMainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_OWNERINFO = 1001;
    public static final int REQUEST_CODE_FOODTRUCKMAP = 1002;
    public static final int REQUEST_CODE_FOODMENUMNG = 1003;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_main);

        // 시간표 관리 버튼
        Button timeMngBtn = (Button) findViewById(R.id.timeMngButton);
        timeMngBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), OwnerInfoActivity.class);
                startActivityForResult(intent, REQUEST_CODE_OWNERINFO);
            }
        });

        // 판매 메뉴 관리 버튼
        Button foodMenuMngBtn = (Button) findViewById(R.id.foodMenuMngBtn);
        foodMenuMngBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), FoodMenuMngActivity.class);
                startActivityForResult(intent, REQUEST_CODE_FOODMENUMNG);
            }
        });


        // 푸드트럭 지도 버튼
        Button foodMapBtn = (Button) findViewById(R.id.foodMapButton);
        foodMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), FoodTruckMapActivity.class);
                startActivityForResult(intent, REQUEST_CODE_FOODTRUCKMAP);
            }
        });

    }
}
