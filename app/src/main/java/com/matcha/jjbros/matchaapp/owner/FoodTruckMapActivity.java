package com.matcha.jjbros.matchaapp.owner;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

public class FoodTruckMapActivity extends FragmentActivity implements OnMapReadyCallback {

        private GoogleMap mMap;
        private HashMap<String, Schedule> this_schedules;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);

                this_schedules = new HashMap<>();

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

                // Add a marker in Sydney and move the camera
                LatLng sydney = new LatLng(-34, 151);
                mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

                new loadSchedules().execute(1); // 일정을 불러와 지도에 그린다.
        }

        // 일정에 등록된 정보를 불러온다.
        public class loadSchedules extends AsyncTask<Integer, Integer, HashMap<String, Schedule>> {
                @Override
                protected HashMap<String, Schedule> doInBackground(Integer... owner_id) {
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

                        Schedule schedule = null;
                        ScheduleVO scheduleVO = null;
                        HashMap<String, Schedule> scheduleList = new HashMap<>();
                        PreparedStatement pstm = null;
                        ResultSet rs = null;
                        String tmp_schedule_key = "";
                        String sql = "select \"SCHEDULE\".*, \"OWNER\".\"NAME\", \"OWNER\".\"EMAIL\", \"OWNER\".\"PHONE\"" +
                                ", \"OWNER\".\"MENU_CATEGORY\" from \"SCHEDULE\" INNER JOIN \"OWNER\" ON" +
                                " \"SCHEDULE\".\"OWNER_ID\"=\"OWNER\".\"ID\"";
                        try {
                                pstm = conn.prepareStatement(sql);
                                rs = pstm.executeQuery();
                                while(rs.next()){
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

                                        schedule = new Schedule(rs.getInt(1), 0, scheduleVO);
                                        tmp_schedule_key = rs.getInt(1) +"_"+ 0;
                                        scheduleList.put(tmp_schedule_key, schedule);
                                }

                        } catch (SQLException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                        } finally {
                                try{
                                        rs.close();
                                        pstm.close();
                                        conn.close();
                                } catch (SQLException e){
                                        Log.d("exception", e.getMessage());
                                }
                        }
                        return scheduleList;
                }

                // 일정 다 불러온 후, 마커를 지도에 추가한다.
                @Override
                protected void onPostExecute(HashMap<String, Schedule> schedules) {
                        super.onPostExecute(schedules);

                        Iterator<String> iterator = schedules.keySet().iterator();

                        while(iterator.hasNext()){
                                String key = (String) iterator.next();
                                Schedule tmpSchedule = schedules.get(key);
                                ScheduleVO tmpScheduleVO = tmpSchedule.getScheduleVO();

                                int markerNo = tmpSchedule.getId();

                                mMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(tmpScheduleVO.getLat(), tmpScheduleVO.getLng()))
                                        .title(String.valueOf(markerNo)));

                                this_schedules.put(key, tmpSchedule);

                        }
                }
        }

}
