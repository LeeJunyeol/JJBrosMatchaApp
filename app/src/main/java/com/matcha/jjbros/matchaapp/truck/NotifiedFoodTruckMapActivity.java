package com.matcha.jjbros.matchaapp.truck;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.matcha.jjbros.matchaapp.R;
import com.matcha.jjbros.matchaapp.common.DBControl;
import com.matcha.jjbros.matchaapp.common.PermissionUtils;
import com.matcha.jjbros.matchaapp.entity.GenUser;
import com.matcha.jjbros.matchaapp.entity.RealtimeLocationOwner;
import com.matcha.jjbros.matchaapp.owner.OwnerInfoActivity;
import com.matcha.jjbros.matchaapp.user.UserMainActivity;

import org.postgresql.geometric.PGpoint;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

/**
 * Created by jylee on 2016-08-28.
 */
public class NotifiedFoodTruckMapActivity extends AppCompatActivity implements LocationListener, OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMyLocationButtonClickListener {

    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    double lat = 0.0;
    double lng = 0.0;
    private LatLng currentPosition;
    private Marker uMarker;
    private GenUser user;
    private String truckName;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean mPermissionDenied = false;

    private HashMap<Marker, RealtimeLocationOwner> truckRealtimeLocationMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notified_food_truck_map);

        truckName = getIntent().getStringExtra("truckName");
        user = getIntent().getParcelableExtra("user");

        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // GPS 프로바이더 사용가능여부
        Boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 네트워크 프로바이더 사용가능여부
        Boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        // Register the listener with the Location Manager to receive location updates
        // 30초마다 갱신
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 30000, 0, this);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 0, this);

        // 수동으로 위치 구하기
        String locationProvider = LocationManager.GPS_PROVIDER;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
        if (lastKnownLocation != null) {
            double lng = lastKnownLocation.getLatitude();
            double lat = lastKnownLocation.getLatitude();
            currentPosition = new LatLng(lat, lng);
            Log.d("Main", "longtitude=" + lng + ", latitude=" + lat);
        }

        mapFragment = (SupportMapFragment) this.getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        new LoadOwnerRealtimeLocation().execute(truckName);
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

    @Override
    public void onInfoWindowClick(Marker marker) {
        if(truckRealtimeLocationMap.containsKey(marker)){
            Intent intent = new Intent(getBaseContext(), FoodTruckViewActivity.class);
            intent.putExtra("ownerID", truckRealtimeLocationMap.get(marker).getOwner_id());
            intent.putExtra("GenUser", user);
            startActivity(intent);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMarkerClickListener(this);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        if (currentPosition == null) {
            currentPosition = new LatLng(37.541957, 126.988168);
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 13));

        mMap.setOnMyLocationButtonClickListener(this);
        enableMyLocation();
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    public class LoadOwnerRealtimeLocation extends AsyncTask<String, Integer, RealtimeLocationOwner> {
        @Override
        protected RealtimeLocationOwner doInBackground(String... truckname) {
            Connection conn = null;
            int result = 0;

            Log.d("현재 트럭이름", truckname[0]);
            try {
                Class.forName("org.postgresql.Driver").newInstance();
                String url = new DBControl().url;
                Properties props = new Properties();
                props.setProperty("user", "postgres");
                props.setProperty("password", "admin123");

                Log.d("url", url);
                conn = DriverManager.getConnection(url, props);
                if (conn == null) // couldn't connect to server
                {
                    Log.d("connection : ", "null");
                    return null;
                }
            } catch (Exception e) {
                Log.d("PPJY", e.getLocalizedMessage());
                return null;
            }

            PreparedStatement pstm = null;
            ResultSet rs = null;
            int res = 0;

            RealtimeLocationOwner realtimeLocationOwner = null;

            String sql = "SELECT \"REALTIME_LOCATION\".\"location\", \"REALTIME_LOCATION\".\"OWNER_ID\", \"OWNER\".\"NAME\" "
                    + "FROM \"REALTIME_LOCATION\" INNER JOIN \"OWNER\" ON \"REALTIME_LOCATION\".\"OWNER_ID\"=\"OWNER\".\"ID\" "
                    + "WHERE \"REALTIME_LOCATION\".\"STATUS\"=TRUE AND \"OWNER\".\"NAME\"=?;";
            try {
                pstm = conn.prepareStatement(sql);
                pstm.setString(1, truckname[0]);
                rs = pstm.executeQuery();

                if (rs.next()) {
                    realtimeLocationOwner = new RealtimeLocationOwner();
                    PGpoint pGpoint = (PGpoint) rs.getObject(1);
                    realtimeLocationOwner.setLat(pGpoint.x);
                    realtimeLocationOwner.setLng(pGpoint.y);
                    realtimeLocationOwner.setOwner_id(rs.getInt(2));
                    realtimeLocationOwner.setTruck_name(rs.getString(3));

                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                try {
                    pstm.close();
                    conn.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return realtimeLocationOwner;
        }

        @Override
        protected void onPostExecute(RealtimeLocationOwner realtimeLocationOwner) {
            super.onPostExecute(realtimeLocationOwner);
            if (realtimeLocationOwner != null) {
                LatLng latLng;
                Log.d("realtimelocationowner1", String.valueOf(realtimeLocationOwner.getLat()));
                if (!truckRealtimeLocationMap.isEmpty()) {
                    Iterator itr = truckRealtimeLocationMap.keySet().iterator();
                    while (itr.hasNext()) {
                        Marker marker = (Marker) itr.next();
                        marker.remove();
                    }
                    truckRealtimeLocationMap.clear();
                    latLng = new LatLng(realtimeLocationOwner.getLat(), realtimeLocationOwner.getLng());
                    Marker marker = mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(realtimeLocationOwner.getTruck_name())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_truck))
                    );
                    truckRealtimeLocationMap.put(marker, realtimeLocationOwner);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
                } else {
                    latLng = new LatLng(realtimeLocationOwner.getLat(), realtimeLocationOwner.getLng());
                    Log.d("realtimelocationowner2", String.valueOf(realtimeLocationOwner.getLat()));
                    Marker marker = mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(realtimeLocationOwner.getTruck_name())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_truck))
                    );
                    truckRealtimeLocationMap.put(marker, realtimeLocationOwner);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));

                }
            }
        }

    }

    @Override
    public void onBackPressed() {
        showSettingsAlert();
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(NotifiedFoodTruckMapActivity.this);

        alertDialog.setTitle("알림");
        alertDialog.setMessage("메인 화면으로 이동합니까?");
        // OK 를 누르게 되면 설정창으로 이동합니다.
        alertDialog.setPositiveButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getBaseContext(), UserMainActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
                finish();
            }
        });
        // Cancle 하면 종료 합니다.
        alertDialog.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

}