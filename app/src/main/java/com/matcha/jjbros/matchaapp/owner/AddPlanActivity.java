package com.matcha.jjbros.matchaapp.owner;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.search.SearchAdView;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.matcha.jjbros.matchaapp.R;

/**
 * Created by hadoop on 16. 7. 13.
 */
/*https://github.com/googlemaps/android-samples.git*/
public class AddPlanActivity extends AppCompatActivity implements OnMapReadyCallback, OnMapClickListener, OnMapLongClickListener{

    private GoogleMap mMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_add_plan);

        Toolbar tb_add_plan = (Toolbar) findViewById(R.id.tb_add_plan);
        setSupportActionBar(tb_add_plan);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.plan_map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .title("Marker"));
        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {

    }

    private Marker addMarkersToMap(LatLng latlng) {
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(latlng)
                .title("Melbourne")
                .snippet("Population: 4,137,400")
                .draggable(true));
        return marker;
    }

    public void onResetMap(View view){
        if (!checkReady()) {
            return;
        }
        // Clear the map because we don't want duplicates of the markers.
        mMap.clear();
    }

    private boolean checkReady() {
        if (mMap == null) {
            Toast.makeText(this, R.string.map_not_ready, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_plan, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.mi_reset_plan_map){
            onResetMap(findViewById(R.layout.activity_owner_add_plan));
        }
        return super.onOptionsItemSelected(item);
    }
}
