package com.matcha.jjbros.matchaapp.owner;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.matcha.jjbros.matchaapp.R;

/**
 * Created by hadoop on 16. 7. 13.
 */
public class AddPlanActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_add_plan);

        Toolbar tb_owner_tmtbl = (Toolbar) findViewById(R.id.tb_owner_tmtbl);
        setSupportActionBar(tb_owner_tmtbl);
    }
}
