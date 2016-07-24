package com.matcha.jjbros.matchaapp.owner;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.matcha.jjbros.matchaapp.R;
import com.matcha.jjbros.matchaapp.entity.GenUser;

/**
 * Created by hadoop on 16. 7. 7.
 */
public class OwnerTimeTableActivity extends AppCompatActivity {
    private GenUser owner;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_timetable);

        owner = (GenUser)getIntent().getParcelableExtra("owner");

        Toolbar tb_owner_tmtbl = (Toolbar) findViewById(R.id.tb_owner_tmtbl);
        setSupportActionBar(tb_owner_tmtbl);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timetbl, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.mi_add_plan){
            Intent intent = new Intent(getApplicationContext(),AddPlanActivity.class);
            intent.putExtra("owner", owner);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
