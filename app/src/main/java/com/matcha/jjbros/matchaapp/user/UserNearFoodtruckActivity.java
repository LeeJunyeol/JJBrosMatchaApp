package com.matcha.jjbros.matchaapp.user;


import android.Manifest;
import android.content.Context;

import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.matcha.jjbros.matchaapp.R;
import com.matcha.jjbros.matchaapp.common.DBControl;
import com.matcha.jjbros.matchaapp.entity.Schedule;
import com.matcha.jjbros.matchaapp.entity.ScheduleVO;

import org.postgresql.geometric.PGpoint;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

public class UserNearFoodtruckActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener{

    SupportMapFragment mapFragment;
    GoogleMap googleMap;
    LocationManager locationManager;
    Location location;
    double lat;
    double lng;
    LatLng currentPosition;
    Marker marker;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_near_foodtruck);


        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
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
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,10000,0,this);


        mapFragment =  (SupportMapFragment) this.getSupportFragmentManager().findFragmentById(R.id.map_user_near_foodtruck);;
        mapFragment.getMapAsync(this);


    }



    @Override
    public void onMapReady(GoogleMap map) {

        googleMap = map;
        currentPosition = new LatLng(lat, lng);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 18));
        marker = googleMap.addMarker(new MarkerOptions().position(currentPosition));

    }




    @Override
    public void onLocationChanged(Location location) {

       final Location setlocation = location;


        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                double setLat = setlocation.getLatitude();
                double setLng = setlocation.getLatitude();


                Connection conn = null;
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
                    }
                } catch (Exception e){
                    Log.d("PPJY", e.getLocalizedMessage());
                }


                PreparedStatement pstm = null;
                ResultSet rs = null;

//                String sql = "select * from \"REALTIME_LOCATION\" where \"OWNER_ID\"=?";
//                try {
//                    pstm = conn.prepareStatement(sql);
//                    //pstm.setInt(1, owner_id[0]);
//                    rs = pstm.executeQuery();
//                    while(rs.next()){
//                        scheduleVO = new ScheduleVO();
//                        PGpoint pGpoint = (PGpoint)rs.getObject(2);
//                        scheduleVO.setLat(pGpoint.x);
//                        scheduleVO.setLng(pGpoint.y);
//                        scheduleVO.setStart_date(rs.getDate(3));
//                        scheduleVO.setEnd_date(rs.getDate(4));
//                        scheduleVO.setStart_time(rs.getTime(5));
//                        scheduleVO.setEnd_time(rs.getTime(6));
//                        scheduleVO.setDay(rs.getString(7));
//                        scheduleVO.setRepeat(rs.getBoolean(8));
//                        scheduleVO.setOwner_id(rs.getInt(9));
//
//                        schedule = new Schedule(rs.getInt(1), 0, scheduleVO);
//                        tmp_schedule_key = rs.getInt(1) +"_"+ 0;
//                        scheduleList.put(tmp_schedule_key, schedule);
//                    }
//
//                } catch (SQLException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }

            }
        });


        lat = setlocation.getLatitude();
        lng = setlocation.getLongitude();
        currentPosition = null;
        marker.remove();

        onMapReady(googleMap);

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
































//    private GoogleMap mGoogleMap;
//    Marker mMarker;
//    Double latitude;
//    Double longitude;
//
//
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_user_near_foodtruck);
//
//        MapsInitializer.initialize(getApplicationContext());
//
//        startLocationService();
//
//    }
//
//
//    private void init() {
//
//
//        GooglePlayServicesUtil.isGooglePlayServicesAvailable(UserNearFoodtruckActivity.this);
//        mGoogleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_user_near_foodtruck)).getMap();
//
//            LatLng latLng = new LatLng(latitude, longitude);
//
//            // Showing the current location in Google Map
//            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//
//            // Map 을 zoom 합니다.
//            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(18));
//
//
//        MarkerOptions myCurrentPosition = new MarkerOptions();
//        myCurrentPosition.position(latLng);// 위도 • 경도
//        myCurrentPosition.title("Current Position");// 제목 미리보기
//        myCurrentPosition.snippet("현재위치");
//        myCurrentPosition.icon(BitmapDescriptorFactory.fromResource(R.drawable.iamhere));
//        mMarker = mGoogleMap.addMarker(myCurrentPosition);
//        mMarker.showInfoWindow();
//
//
//    }
//
//
//    private void startLocationService() {
//        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//
//        GPSListener gpsListener = new GPSListener();
//
//        long minTime = 10000;
//        float minDistance = 0;
//
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, gpsListener);
//
//    }
//
//

//
//
//    private class GPSListener implements LocationListener {
//
//        @Override
//        public void onLocationChanged(Location location) {
//
//             latitude = location.getLatitude();
//             longitude = location.getLongitude();
//             mMarker.remove();
//             init();
//
//
//        }
//
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) {
//
//        }
//
//        @Override
//        public void onProviderEnabled(String provider) {
//
//        }
//
//        @Override
//        public void onProviderDisabled(String provider) {
//
//        }
//    }
//
//
//}








