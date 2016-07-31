package com.matcha.jjbros.matchaapp.user;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.matcha.jjbros.matchaapp.R;
import com.matcha.jjbros.matchaapp.common.DBControl;
import com.matcha.jjbros.matchaapp.entity.Owner;
import com.matcha.jjbros.matchaapp.entity.ScheduleVO;
import com.matcha.jjbros.matchaapp.entity.TruckScheduleInfo;
import com.matcha.jjbros.matchaapp.owner.TruckInfoActivity;

import org.postgresql.geometric.PGpoint;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

public class UserNearFoodtruckActivity extends AppCompatActivity implements OnMapReadyCallback,
        OnInfoWindowClickListener, LocationListener{

    private SupportMapFragment mapFragment;
    private GoogleMap googleMap;
    double lat = 0.0;
    double lng = 0.0;
    private LatLng currentPosition;
    private Marker uMarker;
    private Marker marker2;
    private HashMap<String, Owner> thisOwnerHashMap = new HashMap<>();

    private Location mLastLocation;
    // Check Permissions Now
    private static final int REQUEST_LOCATION = 2;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_near_foodtruck);

        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // GPS 프로바이더 사용가능여부
        Boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 네트워크 프로바이더 사용가능여부
        Boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        // Register the listener with the Location Manager to receive location updates
        // 3분마다 갱신
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 180000, 0, this);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 180000, 0, this);

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
            Log.d("Main", "longtitude=" + lng + ", latitude=" + lat);
        }


        mapFragment = (SupportMapFragment) this.getSupportFragmentManager().findFragmentById(R.id.map_user_near_foodtruck);
        mapFragment.getMapAsync(this);

        new LoadOwnersInfo().execute(1); // 모든 푸드트럭 사업자 정보를 불러온다.


    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        currentPosition = new LatLng(lat, lng);

        new LoadFoodScheduleInfo().execute(1);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 18));
        uMarker = googleMap.addMarker(new MarkerOptions().position(currentPosition));

        googleMap.setOnInfoWindowClickListener(this);
    }

    @Override
    public void onInfoWindowClick(Marker mMarker) {
        Intent intent = new Intent(getApplicationContext(), TruckInfoActivity.class);
        String s = mMarker.getTitle();
        intent.putExtra("foodtruckInfo", thisOwnerHashMap.get(s));
        startActivity(intent);
    }

    //1. 실시간 푸드트럭 위치 등록하기,
    //2. 쿼리해오기,
    //3. 쿼리결과 마커 보여주기.

    public class LoadOwnersInfo extends AsyncTask<Integer, Integer, HashMap<String, Owner>> {
        @Override
        protected HashMap<String, Owner> doInBackground(Integer... start) {
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
                    return null;
                }
            } catch (Exception e) {
                Log.d("PPJY", e.getLocalizedMessage());
                return null;
            }

            Owner owner;
            HashMap<String, Owner> ownerHashMap = new HashMap<>();
            Statement stmt = null;
            ResultSet rs = null;
            String sql = "select * from \"OWNER\";";
            try {
                stmt = conn.createStatement();
                rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    owner = new Owner();
                    owner.setEmail(rs.getString(2));
                    owner.setPw(rs.getString(3));
                    owner.setSex(rs.getBoolean(4));
                    owner.setBirth(rs.getDate(5));
                    owner.setName(rs.getString(6));
                    owner.setPhone(rs.getString(7));
                    owner.setReg_num(rs.getString(8));
                    owner.setMenu_category(rs.getString(9));
                    owner.setAdmition_status(rs.getBoolean(10));

                    ownerHashMap.put(owner.getName(), owner);
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                try {
                    rs.close();
                    stmt.close();
                    conn.close();
                } catch (SQLException e) {
                    Log.d("exception", e.getMessage());
                }
            }
            return ownerHashMap;
        }

        @Override
        protected void onPostExecute(HashMap<String, Owner> stringOwnerHashMap) {
            super.onPostExecute(stringOwnerHashMap);

            thisOwnerHashMap = stringOwnerHashMap;
        }
    }


    public class LoadFoodScheduleInfo extends AsyncTask<Integer, Integer, HashMap<Integer, TruckScheduleInfo>> {
        @Override
        protected HashMap<Integer, TruckScheduleInfo> doInBackground(Integer... start) {
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
                    return null;
                }
            } catch (Exception e){
                Log.d("PPJY", e.getLocalizedMessage());
                return null;
            }

            TruckScheduleInfo truckScheduleInfo = null;
            ScheduleVO scheduleVO = null;
            HashMap<Integer, TruckScheduleInfo> scheduleInfoHashMap = new HashMap<>();
            Statement stmt = null;
            ResultSet rs = null;
            String sql = "select \"SCHEDULE\".*, \"OWNER\".\"NAME\", \"OWNER\".\"EMAIL\", \"OWNER\".\"PHONE\"" +
                    ", \"OWNER\".\"MENU_CATEGORY\" from \"SCHEDULE\" INNER JOIN \"OWNER\" ON" +
                    " \"SCHEDULE\".\"OWNER_ID\"=\"OWNER\".\"ID\" ORDER BY \"SCHEDULE\".\"OWNER_ID\";";
            try {
                stmt = conn.createStatement();
                rs = stmt.executeQuery(sql);

                while(rs.next()){
                    truckScheduleInfo = new TruckScheduleInfo();
                    truckScheduleInfo.setSchedule_id(rs.getInt(1));
                    scheduleVO = new ScheduleVO();
                    PGpoint pGpoint = (PGpoint)rs.getObject(2);
                    scheduleVO.setLat(pGpoint.x);
                    scheduleVO.setLng(pGpoint.y);
                    scheduleVO.setStart_date(rs.getDate(3));
                    scheduleVO.setEnd_date(rs.getDate(4));
                    scheduleVO.setStart_time(rs.getTime(5));
                    scheduleVO.setEnd_time(rs.getTime(6));
                    scheduleVO.setDay(rs.getString(7));
                    scheduleVO.setRepeat(rs.getBoolean(8));
                    scheduleVO.setOwner_id(rs.getInt(9));
                    truckScheduleInfo.setScheduleVO(scheduleVO);
                    truckScheduleInfo.setName(rs.getString(10));
                    truckScheduleInfo.setEmail(rs.getString(11));
                    truckScheduleInfo.setPhone(rs.getString(12));
                    truckScheduleInfo.setMenu_category(rs.getString(13));

                    scheduleInfoHashMap.put(rs.getInt(1), truckScheduleInfo);

                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                try{
                    rs.close();
                    stmt.close();
                    conn.close();
                } catch (SQLException e){
                    Log.d("exception", e.getMessage());
                }
            }
            return scheduleInfoHashMap;
        }

        // 일정 다 불러온 후, 마커를 지도에 추가한다.
        @Override
        protected void onPostExecute(HashMap<Integer, TruckScheduleInfo> scheduleInfoHashMap) {
            super.onPostExecute(scheduleInfoHashMap);

            Iterator<Integer> iterator = scheduleInfoHashMap.keySet().iterator();

            while(iterator.hasNext()){
                Integer key = (Integer) iterator.next();
                TruckScheduleInfo tmpTruckScheduleInfo = scheduleInfoHashMap.get(key);
                ScheduleVO tmpScheduleVO = tmpTruckScheduleInfo.getScheduleVO();
                marker2 = googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(tmpScheduleVO.getLat(), tmpScheduleVO.getLng()))
                        .title(tmpTruckScheduleInfo.getName()));
            }
        }
    }

    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lng = location.getLongitude();

        currentPosition = null;
        uMarker.remove();
        onMapReady(googleMap);

        Log.d("latitude: ", String.valueOf(lat));
        Log.d("longitude: ", String.valueOf(lng));
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("onStatusChanged", String.valueOf(status));
    }

    public void onProviderEnabled(String provider) {
        Log.d("onProviderEnabled", provider);
    }

    public void onProviderDisabled(String provider) {
        Log.d("onProviderDisabled", provider);
    }


}
