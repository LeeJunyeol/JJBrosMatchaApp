package com.matcha.jjbros.matchaapp.truck;


import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.matcha.jjbros.matchaapp.R;
import com.matcha.jjbros.matchaapp.common.DBControl;
import com.matcha.jjbros.matchaapp.common.LocationConverter;
import com.matcha.jjbros.matchaapp.common.PermissionUtils;
import com.matcha.jjbros.matchaapp.entity.GenUser;
import com.matcha.jjbros.matchaapp.entity.Owner;
import com.matcha.jjbros.matchaapp.entity.RealtimeLocationOwner;
import com.matcha.jjbros.matchaapp.entity.ScheduleVO;
import com.matcha.jjbros.matchaapp.entity.TruckScheduleInfo;
import com.matcha.jjbros.matchaapp.truck.FoodTruckMapActivity;
import com.matcha.jjbros.matchaapp.truck.FoodTruckViewActivity;

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

public class UserNearFoodtruckActivity extends AppCompatActivity implements LocationListener, OnMapReadyCallback, OnInfoWindowClickListener, OnMarkerClickListener,
        GoogleMap.OnMyLocationButtonClickListener{

    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    double lat = 0.0;
    double lng = 0.0;
    private LatLng currentPosition;
    private Marker uMarker;
    private GenUser user;

    private HashMap<Marker, TruckScheduleInfo> msHashMap;
    private HashMap<Marker, RealtimeLocationOwner> truckRealtimeLocationMap = new HashMap<>();

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean mPermissionDenied = false;

    // Check Permissions Now
    private static final int REQUEST_LOCATION = 2;

    // 마커 커스텀 이미지
    protected class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        // These a both viewgroups containing an ImageView with id "badge" and two TextViews with id
        // "title" and "snippet".
        private final View mWindow;

        CustomInfoWindowAdapter() {
            mWindow = getLayoutInflater().inflate(R.layout.custom_info_window, null);
        }

        @Override
        public View getInfoWindow(Marker marker) {
            if(msHashMap.containsKey(marker)){
                render(marker, mWindow);
                return mWindow;
            }
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }

        private void render(Marker marker, View view) {
            if(msHashMap.containsKey(marker)){
                // Use the equals() method on a Marker to check for equals.  Do not use ==.
                String truckImgName =  "truck" + msHashMap.get(marker).getScheduleVO().getOwner_id();
                Log.d("truckImgName", truckImgName);

                Resources resources = getApplicationContext().getResources();
                final int badge = resources.getIdentifier(truckImgName, "drawable",
                        getApplicationContext().getPackageName());

                ((ImageView) view.findViewById(R.id.badge)).setImageResource(badge);

                String title = marker.getTitle();
                TextView titleUi = ((TextView) view.findViewById(R.id.title));
                if (title != null) {
                    // Spannable string allows us to edit the formatting of the text.
                    SpannableString titleText = new SpannableString(title);
                    titleText.setSpan(new ForegroundColorSpan(Color.RED), 0, titleText.length(), 0);
                    titleUi.setText(titleText);
                } else {
                    titleUi.setText("");
                }

                String snippet = marker.getSnippet();
                TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
                if (snippet != null && snippet.length() > 12) {
                    SpannableString snippetText = new SpannableString(snippet);
                    snippetText.setSpan(new ForegroundColorSpan(Color.MAGENTA), 0, 10, 0);
                    snippetText.setSpan(new ForegroundColorSpan(Color.BLUE), 12, snippet.length(), 0);
                    snippetUi.setText(snippetText);
                } else {
                    snippetUi.setText("");
                }
            }
        }
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_near_foodtruck);

        user = (GenUser)getIntent().getParcelableExtra("user");

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

        mapFragment = (SupportMapFragment) this.getSupportFragmentManager().findFragmentById(R.id.map_user_near_foodtruck);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMarkerClickListener(this);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        new LoadFoodScheduleInfo().execute(1); // 일정을 불러와 지도에 그린다.

        if(currentPosition == null){
            currentPosition = new LatLng(37.541957, 126.988168);
        }
        uMarker = googleMap.addMarker(new MarkerOptions().position(currentPosition)
        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 15));

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


    //1. 실시간 푸드트럭 위치 등록하기,
    //2. 쿼리해오기,
    //3. 쿼리결과 마커 보여주기.

    // 일정에 등록된 모든 푸드트럭의 정보를 불러온다.
    public class LoadFoodScheduleInfo extends AsyncTask<Integer, Integer, ArrayList<TruckScheduleInfo>> {
        @Override
        protected ArrayList<TruckScheduleInfo> doInBackground(Integer... start) {
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
            ArrayList<TruckScheduleInfo> scheduleInfoList = new ArrayList<>();
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

                    scheduleInfoList.add(truckScheduleInfo);
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
            return scheduleInfoList;
        }

        // 일정 다 불러온 후, 마커를 지도에 추가한다.
        @Override
        protected void onPostExecute(ArrayList<TruckScheduleInfo> scheduleInfoList) {
            super.onPostExecute(scheduleInfoList);

            msHashMap = new HashMap<>();

            Iterator iterator = scheduleInfoList.iterator();

            while(iterator.hasNext()){
                TruckScheduleInfo tmpTruckScheduleInfo = (TruckScheduleInfo) iterator.next();
                ScheduleVO tmpScheduleVO = tmpTruckScheduleInfo.getScheduleVO();
                Log.d("tmsSchedule", String.valueOf(tmpScheduleVO.getOwner_id()));
                int numColor = tmpTruckScheduleInfo.getOwner_id()%10;
                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(tmpScheduleVO.getLat(), tmpScheduleVO.getLng()))
                        .title(tmpTruckScheduleInfo.getName())
                );
                msHashMap.put(marker, tmpTruckScheduleInfo);
            }
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if(msHashMap.containsKey(marker)) {
            Intent intent = new Intent(getBaseContext(), FoodTruckViewActivity.class);
            intent.putExtra("ownerID", msHashMap.get(marker).getScheduleVO().getOwner_id());
            intent.putExtra("GenUser", user);
            startActivity(intent);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if(msHashMap.containsKey(marker)){
            TruckScheduleInfo tsi = msHashMap.get(marker);
            ScheduleVO svo = tsi.getScheduleVO();
            String address = LocationConverter.getAddress(getApplicationContext(), marker.getPosition().latitude, marker.getPosition().longitude);
            String startTime = svo.getStart_time().getHours() + ":" + svo.getStart_time().getMinutes();
            String endTime = svo.getEnd_time().getHours() + ":" + svo.getEnd_time().getMinutes();

            String context = "대표메뉴: " + tsi.getMenu_category() + "\n"
                    + "주소: " + address + "\n"
                    + "기간: " + svo.getStart_date() + "~" + svo.getEnd_date() + "\n"
                    + "시간: " + startTime + "~" + endTime;
            context.concat("\n요일: ");
            if(svo.isRepeat()){
                context.concat("매주 ");
            }
            context.concat(svo.getDay());
            marker.setSnippet(context);
        }

        return false;
    }

    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lng = location.getLongitude();

        currentPosition = null;
        uMarker.remove();
        currentPosition = new LatLng(lat, lng);
        uMarker = mMap.addMarker(new MarkerOptions().position(currentPosition)
        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

        Log.d("latitude: ", String.valueOf(lat));
        Log.d("longitude: ", String.valueOf(lng));

        new LoadOwnerRealtimeLocation().execute(1);

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

    public class LoadOwnerRealtimeLocation extends AsyncTask<Integer, Integer, ArrayList<RealtimeLocationOwner>> {
        @Override
        protected ArrayList<RealtimeLocationOwner> doInBackground(Integer... command) {
            Connection conn = null;
            int result = 0;
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

            Statement stmt = null;
            ResultSet rs = null;
            int res = 0;

            ArrayList<RealtimeLocationOwner> locationOwnerArrayList = new ArrayList<>();

            String sql = "SELECT \"REALTIME_LOCATION\".\"location\", \"REALTIME_LOCATION\".\"OWNER_ID\", \"OWNER\".\"NAME\" "
            + "FROM \"REALTIME_LOCATION\" INNER JOIN \"OWNER\" ON \"REALTIME_LOCATION\".\"OWNER_ID\"=\"OWNER\".\"ID\" "
            + "WHERE \"REALTIME_LOCATION\".\"STATUS\"=TRUE;";
            try {
                stmt = conn.createStatement();
                rs = stmt.executeQuery(sql);

                while(rs.next()){
                    RealtimeLocationOwner rlo = new RealtimeLocationOwner();
                    PGpoint pGpoint = (PGpoint)rs.getObject(1);
                    rlo.setLat(pGpoint.x);
                    rlo.setLng(pGpoint.y);
                    rlo.setOwner_id(rs.getInt(2));
                    rlo.setTruck_name(rs.getString(3));

                    locationOwnerArrayList.add(rlo);
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                try {
                    stmt.close();
                    conn.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return locationOwnerArrayList;
        }

        @Override
        protected void onPostExecute(ArrayList<RealtimeLocationOwner> locationOwnerArrayList) {
            super.onPostExecute(locationOwnerArrayList);
            if(locationOwnerArrayList!=null){
                if(!truckRealtimeLocationMap.isEmpty()){
                    Iterator itr = truckRealtimeLocationMap.keySet().iterator();
                    while(itr.hasNext()){
                        Marker marker = (Marker) itr.next();
                        marker.remove();
                    }
                    truckRealtimeLocationMap.clear();
                    itr = locationOwnerArrayList.iterator();
                    while(itr.hasNext()){
                        RealtimeLocationOwner rlo = (RealtimeLocationOwner) itr.next();
                        Marker marker = mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(rlo.getLat(), rlo.getLng()))
                                .title(rlo.getTruck_name())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_truck))
                        );
                        truckRealtimeLocationMap.put(marker, rlo);
                    }
                } else {
                    Iterator itr = locationOwnerArrayList.iterator();
                    while(itr.hasNext()){
                        RealtimeLocationOwner rlo = (RealtimeLocationOwner) itr.next();
                        Marker marker = mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(rlo.getLat(), rlo.getLng()))
                                .title(rlo.getTruck_name())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_truck))
                        );
                        truckRealtimeLocationMap.put(marker, rlo);
                    }
                }
            }
        }

    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }



    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

}
