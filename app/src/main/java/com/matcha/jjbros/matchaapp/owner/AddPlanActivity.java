package com.matcha.jjbros.matchaapp.owner;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
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
import com.matcha.jjbros.matchaapp.common.DBControl;
import com.matcha.jjbros.matchaapp.entity.GenUser;
import com.matcha.jjbros.matchaapp.entity.Schedule;
import com.matcha.jjbros.matchaapp.entity.ScheduleVO;

import org.postgresql.geometric.PGpoint;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
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

    //입력 레이아웃 속성
    private TextView tv_lat_plan;
    private TextView tv_lng_plan;
    private EditText et_start_date;
    private EditText et_end_date;
    private EditText et_start_time;
    private EditText et_end_time;
    private CheckBox cbx_mon;
    private CheckBox cbx_tue;
    private CheckBox cbx_wed;
    private CheckBox cbx_thur;
    private CheckBox cbx_fri;
    private CheckBox cbx_sat;
    private CheckBox cbx_sun;
    private CheckBox cbx_repeat_stat;
    private Button btn_add_plan;
    private Button btn_update_plan;
    private Button btn_delete_plan;

    private HashMap<String, Schedule> this_schedules = new HashMap<String, Schedule>();
    private String schedule_key = "";
    private LatLng clicked_latlng; // 지도 클릭했을 때 위치 받는 변수
    private int last_marker_no = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_add_plan);

        Toolbar tb_add_plan = (Toolbar) findViewById(R.id.tb_add_plan);
        setSupportActionBar(tb_add_plan);

        btn_add_plan = (Button) findViewById(R.id.btn_add_plan);
        btn_update_plan = (Button) findViewById(R.id.btn_update_plan);
        btn_delete_plan = (Button) findViewById(R.id.btn_delete_plan);
        tv_lat_plan = (TextView) findViewById(R.id.tv_lat_plan);
        tv_lng_plan = (TextView) findViewById(R.id.tv_lng_plan);

        et_start_date = (EditText) findViewById(R.id.et_start_date);
        et_end_date = (EditText) findViewById(R.id.et_end_date);
        et_start_time = (EditText) findViewById(R.id.et_start_time);
        et_end_time = (EditText) findViewById(R.id.et_end_time);
        cbx_mon = (CheckBox) findViewById(R.id.cbx_mon);
        cbx_tue = (CheckBox) findViewById(R.id.cbx_tue);
        cbx_wed = (CheckBox) findViewById(R.id.cbx_wed);
        cbx_thur = (CheckBox) findViewById(R.id.cbx_thur);
        cbx_fri = (CheckBox) findViewById(R.id.cbx_fri);
        cbx_sat = (CheckBox) findViewById(R.id.cbx_sat);
        cbx_sun = (CheckBox) findViewById(R.id.cbx_sun);
        cbx_repeat_stat = (CheckBox) findViewById(R.id.cbx_repeat_stat);

        // 지도 셋팅
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.plan_map);
        mapFragment.getMapAsync(this);

        // 입력 버튼 클릭 이벤트
        btn_add_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 입력 레이아웃의 정보 불러오기
                if(stat != 1){
                    Toast.makeText(getApplicationContext(), "먼저 위치를 등록하세요.", Toast.LENGTH_LONG).show();
                    return;
                }
                double lat = Double.valueOf(tv_lat_plan.getText().toString());
                double lng = Double.valueOf(tv_lng_plan.getText().toString());
                String str_day = "";
                Date start_date = Date.valueOf(et_start_date.getText().toString());
                Date end_date = Date.valueOf(et_end_date.getText().toString());
                Time start_time = Time.valueOf(et_start_time.getText().toString()+":00");
                Time end_time = Time.valueOf(et_end_time.getText().toString()+":00");

                if(cbx_mon.isChecked()){
                    str_day.concat("월,");
                } else if(cbx_tue.isChecked()){
                    str_day.concat("화,");
                } else if(cbx_wed.isChecked()){
                    str_day.concat("수,");
                } else if(cbx_thur.isChecked()){
                    str_day.concat("목,");
                } else if(cbx_fri.isChecked()){
                    str_day.concat("금,");
                } else if(cbx_sat.isChecked()){
                    str_day.concat("토,");
                } else if(cbx_sun.isChecked()){
                    str_day.concat("일,");
                }
                // 마지막 콤마 제가
                Log.d("str_day : ", str_day);
                if(str_day.endsWith(",")){
                    StringBuffer sb = new StringBuffer(str_day);
                    sb.delete(sb.lastIndexOf(",")-1, sb.lastIndexOf(","));
                    str_day = sb.toString();
                }
                Log.d("After str_day : ", str_day);

                Boolean repeat_stat = cbx_repeat_stat.isChecked();

                //ScheduleVO(double lat, double lng, Date start_date, Date end_date, Time start_time, Time end_time, String day, boolean repeat, int owner_id)
                ScheduleVO newScheduleVO = new ScheduleVO(lat, lng,
                        start_date, end_date,
                        start_time, end_time,
                        str_day,
                        repeat_stat,
                        owner_id);

                //Schedule(int id, int stat, ScheduleVO scheduleVO)
                // 처음 만들어진 것은 1, 수정된 것은 2, 삭제된 것은 3, 변하지 않은 것은 0
                Schedule newSchedule = new Schedule(last_marker_no, 1, newScheduleVO);
                schedule_key = newSchedule.getId() + "_" + newSchedule.getStat();
                this_schedules.put(schedule_key,newSchedule);

                // 주어진 위치에 마크를 그림
                addMarkersToMap(new LatLng(lat, lng));

                //입력이 끝나면, 입력단추는 사라지고 수정/삭제 버튼 등장
                btn_add_plan.setVisibility(View.GONE);
                btn_update_plan.setVisibility(View.VISIBLE);
                btn_delete_plan.setVisibility(View.VISIBLE);
            }
        });

        btn_update_plan.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });

        // 형식에 맞게 입력하도록
        DateTextWatcher startDateTextWatcher = new DateTextWatcher(et_start_date);
        DateTextWatcher endDateTextWatcher = new DateTextWatcher(et_end_date);
        TimeTextWatcher startTimeTextWatcher = new TimeTextWatcher(et_start_time);
        TimeTextWatcher endTimeTextWatcher = new TimeTextWatcher(et_end_time);

        et_start_date.addTextChangedListener(startDateTextWatcher);
        et_end_date.addTextChangedListener(endDateTextWatcher);
        et_start_time.addTextChangedListener(startTimeTextWatcher);
        et_end_time.addTextChangedListener(endTimeTextWatcher);
    }

    // 형식에 맞게 시간 입력받는다.
    public class TimeTextWatcher implements TextWatcher {
        private EditText et;
        private String beforeText;

        public TimeTextWatcher(EditText et) {
            this.et = et;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            beforeText = s.toString();
            if (beforeText.endsWith(":")) {
                beforeText = beforeText.substring(0, beforeText.length()-1);
            }
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            switch (s.length()){
                case 2:
                    s = s.toString().concat(":");
                    et.setText(s);
                    et.setSelection(et.length());
                    break;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            String msg = "형식에 맞지 않습니다.";
            if (s.length() > 5){
                et.setText(beforeText);
                et.setSelection(et.length());
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
            if (s.length() > 3){
                if(s.charAt(2) != ':'){
                    et.setText(beforeText);
                    et.setSelection(et.length());
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    // 형식에 맞게 날짜 입력받는다.
    public class DateTextWatcher implements TextWatcher {
        private EditText et;
        private String beforeText;

        public DateTextWatcher(EditText et) {
            this.et = et;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            beforeText = s.toString();
            if (beforeText.endsWith(":")) {
                beforeText = beforeText.substring(0, beforeText.length()-1);
            }
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            switch (s.length()){
                case 4:
                    s = s.toString().concat("-");
                    et.setText(s);
                    et.setSelection(et.length());
                    break;
                case 7:
                    s = s.toString().concat("-");
                    et.setText(s);
                    et.setSelection(et.length());
                    break;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            String msg = "형식에 맞지 않습니다.";
            if (s.length() > 10){
                et.setText(beforeText);
                et.setSelection(et.length());
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
            if (s.length() > 4){
                if (s.charAt(4) != '-' ){
                    et.setText(beforeText);
                    et.setSelection(et.length());
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                }
            }
            if (s.length() > 7){
                if(s.charAt(7) != '-'){
                    et.setText(beforeText);
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
                }
            }
        }
    }

    // 일정에 등록된 정보를 불러온다.
    public class loadSchedules extends AsyncTask<Integer, Integer, HashMap<String, Schedule>>{
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

                    schedule = new Schedule(rs.getInt(1), 0, scheduleVO);
                    tmp_schedule_key = rs.getInt(1) +"_"+ 0;
                    scheduleList.put(tmp_schedule_key, schedule);
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

                int tmp = last_marker_no;
                if(tmp < markerNo){
                    last_marker_no = markerNo;
                }
            }
        }
    }

    // 일정디비에 지금까지 입력, 수정, 삭제한 결과를 적용한다.
    public class inputSchedules extends AsyncTask<Integer, Integer, HashMap<Integer, Schedule>>{
        @Override
        protected HashMap<Integer, Schedule> doInBackground(Integer... owner_id) {
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
            HashMap<Integer, Schedule> scheduleList = new HashMap<>();
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

                    schedule = new Schedule(rs.getInt(1), 0, scheduleVO);
                    scheduleList.put(rs.getInt(1), schedule);
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
        protected void onPostExecute(HashMap<Integer, Schedule> schedules) {
            super.onPostExecute(schedules);
            Collection<Schedule> scheduleCollection = schedules.values();
            Iterator<Schedule> scheduleIterator = scheduleCollection.iterator();
            while(scheduleIterator.hasNext()){
                ScheduleVO tmpScheduleVO = scheduleIterator.next().getScheduleVO();
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(tmpScheduleVO.getLat(), tmpScheduleVO.getLng()))
                        .title("일정"));
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

        new loadSchedules().execute(owner_id); // 일정을 불러와 지도에 그린다.

        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());

        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);
        mMap.setOnInfoWindowClickListener(this); // 마커 클릭하면 정보창 보이게
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
        stat = 1; // 마커 입력 가능
        Toast.makeText(getApplicationContext(), "위치를 선택하셨습니다.", Toast.LENGTH_LONG).show();
        tv_lat_plan.setText(String.valueOf(latLng.latitude));
        tv_lng_plan.setText(String.valueOf(latLng.longitude));
        Log.d("lat",tv_lat_plan.getText().toString());
        Log.d("lng",tv_lng_plan.getText().toString());
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        addMarkersToMap(latLng);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        stat = 0; // 수정 전

        return false;
    }

    public void addMarkersToMap(LatLng latlng) {
        last_marker_no += 1;
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(latlng)
                .title(String.valueOf(last_marker_no)));
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

/*
        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.cbx_mon:
                if (checked)
                // Put some meat on the sandwich
                else
                // Remove the meat
                break;
            case R.id.cbx_tue:
                if (checked)
                // Cheese me
                else
                // I'm lactose intolerant
                break;
            // TODO: Veggie sandwich
        }
*/
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
