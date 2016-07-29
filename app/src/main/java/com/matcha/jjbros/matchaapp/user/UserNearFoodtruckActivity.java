package com.matcha.jjbros.matchaapp.user;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.matcha.jjbros.matchaapp.R;
import com.matcha.jjbros.matchaapp.common.DBControl;
import com.matcha.jjbros.matchaapp.entity.ScheduleVO;
import com.matcha.jjbros.matchaapp.entity.TruckScheduleInfo;
import org.postgresql.geometric.PGpoint;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

public class UserNearFoodtruckActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    SupportMapFragment mapFragment;
    GoogleMap googleMap;
    LocationManager locationManager;
    double lat;
    double lng;
    LatLng currentPosition;
    Marker marker;
    Marker marker2;


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
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, this);


        mapFragment = (SupportMapFragment) this.getSupportFragmentManager().findFragmentById(R.id.map_user_near_foodtruck);
        mapFragment.getMapAsync(this);


    }


    @Override
    public void onMapReady(GoogleMap map) {


        googleMap = map;
        currentPosition = new LatLng(lat, lng);
        new LoadFoodScheduleInfo().execute(1);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 18));
        marker = googleMap.addMarker(new MarkerOptions().position(currentPosition));


    }


    @Override
    public void onLocationChanged(Location location) {


        lat = location.getLatitude();
        lng = location.getLatitude();

        currentPosition = null;
        marker.remove();
        onMapReady(googleMap);


        }


        //1. 실시간 푸드트럭 위치 등록하기,
        //2. 쿼리해오기,
        //3. 쿼리결과 마커 보여주기.







    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

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
}







