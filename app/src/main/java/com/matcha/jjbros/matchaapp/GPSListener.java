package com.matcha.jjbros.matchaapp;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;


/**
 * Created by Yu on 2016-07-09.
 */
public class GPSListener implements LocationListener {
    @Override
    public void onLocationChanged(Location location) {

        Double latitude  = location.getLatitude();
        Double longitude = location.getLongitude();



    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
