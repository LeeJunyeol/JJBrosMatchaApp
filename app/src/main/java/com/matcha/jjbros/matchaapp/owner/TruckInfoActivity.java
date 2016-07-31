package com.matcha.jjbros.matchaapp.owner;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.matcha.jjbros.matchaapp.R;
import com.matcha.jjbros.matchaapp.entity.GenUser;
import com.matcha.jjbros.matchaapp.entity.Owner;

/**
 * Created by hadoop on 16. 7. 28.
 */
public class TruckInfoActivity extends OwnerMainActivity{
    private Owner owner;
    private ImageView foodtruckImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_foodtruck_view);

        owner = (Owner)getIntent().getParcelableExtra("foodtruckInfo");
        String ownerEmail = owner.getEmail();
        int resID = getApplicationContext().getResources().getIdentifier(ownerEmail+"_"+"01", "drawable", getApplicationContext().getPackageName());

        foodtruckImg = (ImageView)findViewById(R.id.iv_foodtruck_view);
        foodtruckImg.setImageResource(resID);
  }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
