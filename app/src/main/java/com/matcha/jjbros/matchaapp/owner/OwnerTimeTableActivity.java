package com.matcha.jjbros.matchaapp.owner;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.matcha.jjbros.matchaapp.R;
import com.matcha.jjbros.matchaapp.common.DBControl;
import com.matcha.jjbros.matchaapp.entity.GenUser;
import com.matcha.jjbros.matchaapp.entity.Schedule;
import com.matcha.jjbros.matchaapp.entity.ScheduleVO;

import org.postgresql.geometric.PGpoint;

import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * Created by hadoop on 16. 7. 7.
 */
public class OwnerTimeTableActivity extends AppCompatActivity {
    private GenUser owner;
    private ListView lv_owner_tmtbl;
    private DrawerLayout drawerLayout;
    private Toolbar tb_owner_tmtbl;
    private ActionBarDrawerToggle dtToggle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_timetable);

        tb_owner_tmtbl = (Toolbar) findViewById(R.id.tb_owner_tmtbl);
        setSupportActionBar(tb_owner_tmtbl);

        // drawerLayout setting         //////////////////////////////////////////////
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_owner_tmtbl);

        dtToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(dtToggle);

        ActionBar ab = getSupportActionBar();
        if (null != ab) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
        ///////////////////////////////////////////////////////////////////////////////

        owner = (GenUser)getIntent().getParcelableExtra("owner");
        lv_owner_tmtbl = (ListView) findViewById(R.id.lv_owner_tmtbl);
        lv_owner_tmtbl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(),"Click ListItem", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timetbl, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (dtToggle.onOptionsItemSelected(item)) {
            return true;
        }
        int id = item.getItemId();
        if (id == R.id.mi_add_plan){
            Intent intent = new Intent(getApplicationContext(),AddPlanActivity.class);
            intent.putExtra("owner", owner);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

// Sync the toggle state after onRestoreInstanceState has occurred.
        dtToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        dtToggle.onConfigurationChanged(newConfig);
    }



    // 일정에 등록된 정보를 불러온다.
    public class LoadSchedulesAll extends AsyncTask<Integer, Integer, ArrayList<Schedule>> {
        @Override
        protected ArrayList<Schedule> doInBackground(Integer... owner_id) {
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
            ArrayList<Schedule> scheduleList = new ArrayList<>();
            PreparedStatement pstm = null;
            ResultSet rs = null;
            ArrayList<Date> openDates = new ArrayList<Date>();
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
                    scheduleList.add(schedule);
                }

            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                try{
                    rs.close();
                    pstm.close();
                    conn.close();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
            return scheduleList;
        }

        // 일정 다 불러온 후, 마커를 지도에 추가한다.
        @Override
        protected void onPostExecute(ArrayList<Schedule> schedules) {
            super.onPostExecute(schedules);

            Iterator<Schedule> iterator = schedules.iterator();

            while(iterator.hasNext()){
/*
                String key = (String) iterator.next();
                Schedule tmpSchedule = schedules.get(key);
                ScheduleVO tmpScheduleVO = tmpSchedule.getScheduleVO();
*/
            }
        }
    }

    protected  ArrayList<Date> DateSplitor(String days, Date start_date, Date end_date){
        String[] strings = days.split(",");
        ArrayList<Integer> integers = new ArrayList<Integer>();
        ArrayList<Date> resultDates = new ArrayList<Date>();
        // 비교하기 위해 요일을 리스트로 만들어준다.
        for(int i = 0; i < strings.length; i++){
            switch (strings[i]){
                case "월":
                    integers.add(2);
                    break;
                case "화":
                    integers.add(3);
                    break;
                case "수":
                    integers.add(4);
                    break;
                case "목":
                    integers.add(5);
                    break;
                case "금":
                    integers.add(6);
                    break;
                case "토":
                    integers.add(7);
                    break;
                case "일":
                    integers.add(1);
                    break;
            }
        }

        Calendar cal = Calendar.getInstance();
        // 시간차이를 시간,분,초를 곱한 값으로 나누면 하루 단위가 나옴
        long diff = end_date.getTime() - start_date.getTime();
        long diffDays = diff / (24 * 60 * 60 * 1000);
        cal.setTime(start_date);

        for(int i = 0; i<=diffDays; i++){
            int chkday =cal.get(Calendar.DAY_OF_WEEK);
            if(integers.contains(chkday)){
                Date d = new Date(cal.getTimeInMillis());
                resultDates.add(d);
            }
            cal.add(Calendar.DAY_OF_YEAR, 1);
        }
        return resultDates;
    }
}
