package com.matcha.jjbros.matchaapp.owner;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.matcha.jjbros.matchaapp.R;

/**
 * Created by hadoop on 16. 7. 13.
 */
public class AddPlanActivity extends AppCompatActivity implements OnMapReadyCallback{
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_add_plan);

        Toolbar tb_add_plan = (Toolbar) findViewById(R.id.tb_add_plan);
        setSupportActionBar(tb_add_plan);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        int PLACE_PICKER_REQUEST = 1;

        LatLngBounds newarkBounds = new LatLngBounds(
                new LatLng(40.712216, -74.22655),       // South west corner
                new LatLng(40.773941, -74.12544));      // North east corner


        PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
        intentBuilder.setLatLngBounds(newarkBounds);
        Context context = getApplicationContext();
        startActivityForResult(intentBuilder.build(context), PLACE_PICKER_REQUEST);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        LatLng sydney = new LatLng(-34, 151);
        map.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        map.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private GoogleApiClient setGoogleServiceBuilder(){

        GoogleApiClient mGoogleApiClientBuilder = new GoogleApiClient.Builder(this.getApplicationContext());

        //Places 서비스에 사용될 API 사용요청

        mGoogleApiClientBuilder.addApi(Places.GEO_DATA_API);

        mGoogleApiClientBuilder.addApi(Places.PLACE_DETECTION_API);

        //Fused Location Provider 서비스에 사용될 API 사용요청

        mGoogleApiClientBuilder.addApi(LocationServices.API);



        CallbackConnectedGoogleService callbackConnectedGoogleService = new

                CallbackConnectedGoogleService(this);

        mGoogleApiClientBuilder.addConnectionCallbacks(callbackConnectedGoogleService);

        mGoogleApiClientBuilder.addOnConnectionFailedListener(callbackConnectedGoogleService);

        GoogleApiClient mGoogleApiClient = mGoogleApiClientBuilder.build();

        mGoogleApiClient.connect();

        return mGoogleApiClientBuilder;

    }
}
