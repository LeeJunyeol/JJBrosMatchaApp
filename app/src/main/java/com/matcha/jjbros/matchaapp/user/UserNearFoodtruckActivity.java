package com.matcha.jjbros.matchaapp.user;


import android.Manifest;
import android.content.Context;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.matcha.jjbros.matchaapp.R;


public class UserNearFoodtruckActivity extends FragmentActivity {

    private GoogleMap mGoogleMap;
    Marker mMarker;
    Double latitude;
    Double longitude;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_near_foodtruck);

        MapsInitializer.initialize(getApplicationContext());

        startLocationService();

    }


    private void init() {

        GooglePlayServicesUtil.isGooglePlayServicesAvailable(UserNearFoodtruckActivity.this);
        mGoogleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_user_near_foodtruck)).getMap();

            LatLng latLng = new LatLng(latitude, longitude);

            // Showing the current location in Google Map
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

            // Map 을 zoom 합니다.
            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(18));


        MarkerOptions myCurrentPosition = new MarkerOptions();
        myCurrentPosition.position(latLng);// 위도 • 경도
        myCurrentPosition.title("Current Position");// 제목 미리보기
        myCurrentPosition.snippet("현재위치");
        myCurrentPosition.icon(BitmapDescriptorFactory.fromResource(R.drawable.iamhere));
        mMarker = mGoogleMap.addMarker(myCurrentPosition);
        mMarker.showInfoWindow();

    }


    private void startLocationService() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        GPSListener gpsListener = new GPSListener();

        long minTime = 10000;
        float minDistance = 0;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, gpsListener);

    }


    private class GPSListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {

             latitude = location.getLatitude();
             longitude = location.getLongitude();
                mMarker.remove();
             init();


        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }







}

