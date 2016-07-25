package com.matcha.jjbros.matchaapp.owner;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.matcha.jjbros.matchaapp.R;
import com.matcha.jjbros.matchaapp.entity.GenUser;
import com.matcha.jjbros.matchaapp.entity.Schedule;
import com.matcha.jjbros.matchaapp.entity.ScheduleVO;

import org.postgresql.geometric.PGpoint;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

/**
 * Created by hadoop on 16. 7. 13.
 */
/*https://github.com/googlemaps/android-samples.git*/
public class AddPlanActivity extends AppCompatActivity implements OnMapReadyCallback, OnMapClickListener, OnMapLongClickListener,
        OnMarkerClickListener, OnInfoWindowClickListener {

    private GoogleMap mMap;
    private double lat = 0.0;
    private double lng = 0.0;

    private GenUser owner;
    private int owner_id = 0;
    // 처음 만들어진 것은 1, 수정된 것은 2, 삭제된 것은 3, 변하지 않은 것은 0
    private int stat = 0;
    private EditText et_start_date;
    private EditText et_end_date;
    private EditText et_start_time;
    private EditText et_end_time;
    private String str_day;
    private CheckBox cbx_mon;
    private CheckBox cbx_tue;
    private CheckBox cbx_wed;
    private CheckBox cbx_thur;
    private CheckBox cbx_fri;
    private CheckBox cbx_sat;
    private CheckBox cbx_sun;
    private CheckBox cbx_repeat_stat;
    private Button btn_add_plan;
    private Button btn_delete_plan;
    private Schedule[] schedules;
    private LatLng clicked_latlng;
    private int clicked_markerno;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_add_plan);

        Toolbar tb_add_plan = (Toolbar) findViewById(R.id.tb_add_plan);
        setSupportActionBar(tb_add_plan);
        btn_add_plan = (Button) findViewById(R.id.btn_add_plan);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.plan_map);
        mapFragment.getMapAsync(this);

        btn_add_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_start_date = (EditText) findViewById(R.id.et_start_date);
                et_end_date = (EditText) findViewById(R.id.et_end_date);
                et_start_time = (EditText) findViewById(R.id.et_start_time);
                et_end_time = (EditText) findViewById(R.id.et_end_date);
                cbx_mon = (CheckBox) findViewById(R.id.cbx_mon);
                cbx_tue = (CheckBox) findViewById(R.id.cbx_tue);
                cbx_wed = (CheckBox) findViewById(R.id.cbx_wed);
                cbx_thur = (CheckBox) findViewById(R.id.cbx_thur);
                cbx_fri = (CheckBox) findViewById(R.id.cbx_fri);
                cbx_sat = (CheckBox) findViewById(R.id.cbx_sat);
                cbx_sun = (CheckBox) findViewById(R.id.cbx_sun);

                if(cbx_mon.isChecked()){
                    str_day.concat("월,");
                } else if(cbx_tue.isChecked()){
                    str_day.concat("화,");
                } else if(cbx_wed.isChecked()){
                    str_day.concat("수,");
                } else if(cbx_tue.isChecked()){
                    str_day.concat("목,");
                } else if(cbx_tue.isChecked()){
                    str_day.concat("금,");
                } else if(cbx_tue.isChecked()){
                    str_day.concat("토,");
                } else if(cbx_tue.isChecked()){
                    str_day.concat("일,");
                }
                Log.d("str_day : ", str_day);
                if(str_day.endsWith(",")){
                    StringBuffer sb = new StringBuffer(str_day);
                    sb.delete(sb.lastIndexOf(",")-1, sb.lastIndexOf(","));
                    str_day = sb.toString();
                }
                Log.d("After str_day : ", str_day);

                cbx_repeat_stat.isChecked();




                addMarkersToMap(clicked_latlng);
            }
        });
    }

    // 일정에 등록된 정보를 불러온다.
    public class loadSchedules extends AsyncTask<Integer, Integer, ArrayList<Schedule>>{
        @Override
        protected ArrayList<Schedule> doInBackground(Integer... owner_id) {
            Connection conn = null;
            try {
                Class.forName("org.postgresql.Driver").newInstance();
                String url = "jdbc:postgresql://192.168.0.79:5432/matcha";
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
            ArrayList<Schedule> scheduleList = new ArrayList<>();
            PreparedStatement pstm = null;
            ResultSet rs = null;
            String sql = "select * from \"SCHEDULE\" where \"OWNER_ID\"=?";
            try {
                pstm = conn.prepareStatement(sql);
                pstm.setInt(1, owner_id[0]);
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

                    schedule = new Schedule();
                    schedule.setId(rs.getInt(1));
                    schedule.setStat(0);
                    schedule.setScheduleVO(scheduleVO);
                    scheduleList.add(schedule);
                }

            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                Close(rs);
                Close(pstm);
                Close(conn);
            }
            return scheduleList;
        }

        // 일정 다 불러온 후, 마커를 지도에 추가한다.
        @Override
        protected void onPostExecute(ArrayList<Schedule> schedules) {
            super.onPostExecute(schedules);
            for ( int i = 0; i < schedules.size(); i++ ){
                ScheduleVO tmpScheduleVO = schedules.get(i).getScheduleVO();
                int markerNo = i + 1;
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(tmpScheduleVO.getLat(), tmpScheduleVO.getLng()))
                        .title(String.valueOf(markerNo)));
            }
        }
    }

    // 일정디비에 지금까지 입력, 수정, 삭제한 결과를 적용한다.
    public class inputSchedules extends AsyncTask<Integer, Integer, ArrayList<Schedule>>{
        @Override
        protected ArrayList<Schedule> doInBackground(Integer... owner_id) {
            Connection conn = null;
            try {
                Class.forName("org.postgresql.Driver").newInstance();
                String url = "jdbc:postgresql://192.168.0.79:5432/matcha";
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
            ArrayList<Schedule> scheduleList = new ArrayList<>();
            PreparedStatement pstm = null;
            ResultSet rs = null;
            String sql = "select * from \"SCHEDULE\" where \"OWNER_ID\"=?";
            try {
                pstm = conn.prepareStatement(sql);
                pstm.setInt(1, owner_id[0]);
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

                    schedule = new Schedule();
                    schedule.setId(rs.getInt(1));
                    schedule.setStat(0);
                    schedule.setScheduleVO(scheduleVO);
                    scheduleList.add(schedule);
                }

            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                Close(rs);
                Close(pstm);
                Close(conn);
            }
            return scheduleList;
        }

        // 일정 다 불러온 후, 마커를 지도에 추가한다.
        @Override
        protected void onPostExecute(ArrayList<Schedule> schedules) {
            super.onPostExecute(schedules);
            for ( int i = 0; i < schedules.size(); i++ ){
                ScheduleVO tmpScheduleVO = schedules.get(i).getScheduleVO();
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(tmpScheduleVO.getLat(), tmpScheduleVO.getLng()))
                        .title("일정")
                        .snippet("내용"));
            }
        }
    }

    // 마커 정보창 클릭했을 때
    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    // 처음 맵 로딩될 때
    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        owner = (GenUser)getIntent().getParcelableExtra("owner");
        owner_id = owner.getId();
        new loadSchedules().execute(owner_id);

        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .title("Marker"))
                .setDraggable(true);

        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
    }

    class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        CheckBox cbx_input_mode = (CheckBox)findViewById(R.id.cbx_input_mode);
        clicked_latlng = new LatLng(latLng.latitude, latLng.longitude);
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        addMarkersToMap(latLng);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        clicked_markerno = Integer.parseInt(this.getTitle().toString());

        return false;
    }

    private Marker addMarkersToMap(LatLng latlng) {
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(latlng));

        return marker;
    }

    private boolean checkReady() {
        if (mMap == null) {
            Toast.makeText(this, R.string.map_not_ready, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_plan, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.mi_reset_plan_map){
            if (checkReady())
                mMap.clear();
        } else if (id == R.id.mi_save_plan_map){
            // 지금까지 수행한 결과를 디비에 저장한다.
        }
        return super.onOptionsItemSelected(item);
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.cbx_mon:
                if (checked) {
                    // Put some meat on the sandwich
                }
                else
                // Remove the meat
                break;
            case R.id.cbx_tue:
                if (checked) {
                    // Cheese me
                }
                else
                // I'm lactose intolerant
                break;
            // TODO: Veggie sandwich
        }
    }

    public void Close(Connection con){
        if(con != null){
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public void Close(Statement stmt){
        if(stmt != null){
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public void Close(ResultSet rs){
        if(rs != null){
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public boolean isConnected(Connection con){
        boolean validConnection = true;
        try {
            if(con==null||con.isClosed())
                validConnection = false;
        } catch (SQLException e) {
            validConnection = false;
            e.printStackTrace();
        }
        return validConnection;
    }
    public void Commit(Connection con){
        try {
            if(isConnected(con)){
                con.commit();
                Log.d("JdbcTemplate.Commit", "DB Successfully Committed!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void Rollback(Connection con){
        try {
            if(isConnected(con)){
                con.rollback();
                Log.d("JdbcTemplate.rollback", "DB Successfully Rollbacked!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
