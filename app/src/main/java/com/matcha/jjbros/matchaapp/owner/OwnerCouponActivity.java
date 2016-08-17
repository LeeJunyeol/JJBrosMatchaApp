package com.matcha.jjbros.matchaapp.owner;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.matcha.jjbros.matchaapp.R;
import com.matcha.jjbros.matchaapp.entity.GenUser;

/**
 * Created by hadoop on 16. 7. 7.
 */
public class OwnerCouponActivity extends AppCompatActivity {

    GenUser owner = new GenUser();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_coupon_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        owner = (GenUser) getIntent().getParcelableExtra("owner");

        FloatingActionButton fabAdd = (FloatingActionButton) findViewById(R.id.btn_add_coupon_owner);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddCouponActivity.class);
                intent.putExtra("owner", owner);
                startActivity(intent);
            }
        });

    }


}
