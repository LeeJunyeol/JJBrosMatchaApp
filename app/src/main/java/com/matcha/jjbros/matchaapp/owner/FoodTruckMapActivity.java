package com.matcha.jjbros.matchaapp.owner;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.matcha.jjbros.matchaapp.R;
import com.matcha.jjbros.matchaapp.common.DBControl;
import com.matcha.jjbros.matchaapp.entity.Schedule;
import com.matcha.jjbros.matchaapp.entity.ScheduleVO;
import com.matcha.jjbros.matchaapp.entity.TruckScheduleInfo;

import org.postgresql.geometric.PGpoint;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

public class FoodTruckMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private HashMap<Integer, TruckScheduleInfo> this_TruckScheduleInfoHashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this_TruckScheduleInfoHashMap = new HashMap<>();

        setContentView(R.layout.activity_food_truck_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
        .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        new LoadFoodScheduleInfo().execute(1); // 일정을 불러와 지도에 그린다.

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(37.541957, 126.988168))
                .zoom(10)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    // 일정에 등록된 모든 푸드트럭의 정보를 불러온다.
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
                mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(tmpScheduleVO.getLat(), tmpScheduleVO.getLng()))
                    .title(tmpTruckScheduleInfo.getName()));
            }
        }
    }
}
