package com.matcha.jjbros.matchaapp.truck;

import android.content.res.Resources;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.matcha.jjbros.matchaapp.R;
import com.matcha.jjbros.matchaapp.common.DBControl;
import com.matcha.jjbros.matchaapp.entity.GenUser;
import com.matcha.jjbros.matchaapp.entity.Owner;
import com.matcha.jjbros.matchaapp.entity.Schedule;
import com.matcha.jjbros.matchaapp.entity.ScheduleVO;
import com.matcha.jjbros.matchaapp.entity.TruckInfoAll;
import com.matcha.jjbros.matchaapp.entity.TruckMenu;
import com.matcha.jjbros.matchaapp.entity.User;

import org.postgresql.geometric.PGpoint;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class FoodTruckViewActivity extends AppCompatActivity {
    private GenUser user = new GenUser();
    private ImageView truckImgView;
    private RatingBar ratingBar;
    private Button btnSchedule;
    private Button btnMenu;
    private ImageButton imgBtnTel;
    private ListView lvSchedule;
    private ListView lvMenu;
    private TextView tvWelcome;
    private ListView lvReview;
    private Button btnAddReview;
    private TruckInfoAll thisTruckInfoAll;

    private String truckName;
    private Toolbar toolbar;

    private int truckOwnerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_truck_view);
        toolbar = (Toolbar) findViewById(R.id.tb_truck_view);
        setSupportActionBar(toolbar);

        truckOwnerID = getIntent().getExtras().getInt("ownerID");
        user = getIntent().getParcelableExtra("GenUser");

        truckImgView = (ImageView) findViewById(R.id.iv_foodtruck_view);
        ratingBar = (RatingBar) findViewById(R.id.rb_truck_view);
        btnSchedule = (Button) findViewById(R.id.btn_schedule_truck);
        btnMenu = (Button) findViewById(R.id.btn_menu_truck);
        imgBtnTel = (ImageButton) findViewById(R.id.imgbtn_tel_truck);
        lvSchedule = (ListView) findViewById(R.id.lv_schedule_truck);
        lvMenu = (ListView) findViewById(R.id.lv_menu_truck);
        tvWelcome = (TextView) findViewById(R.id.tv_welcome_truck);
        lvReview = (ListView) findViewById(R.id.lv_user_review_truck);
        btnAddReview = (Button) findViewById(R.id.btn_add_review_truck);

        // 푸드트럭 기본정보, 일정, 메뉴 불러옴
        new LoadTruckInfoAll().execute(truckOwnerID);

    }

    public class LoadTruckInfoAll extends AsyncTask<Integer, Integer, TruckInfoAll> {

        @Override
        protected TruckInfoAll doInBackground(Integer... id) {
            Connection conn = null;

            TruckInfoAll TruckInfoAll = new TruckInfoAll();
            TruckMenu truckMenu = new TruckMenu();
            Schedule schedule = new Schedule();
            ScheduleVO scheduleVO = new ScheduleVO();
            GenUser genUser = new GenUser();
            genUser.setId(id[0]);

            Log.d("LoadTruckInfoAll", "in LoadTruckInfoAll");
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

            int res = 0;
            String sql = "";
            PreparedStatement pstm = null;
            ResultSet rs = null;
            String tbl_name = "";
            Log.d("SQL", sql);
            try {
                sql = "select * from \"OWNER\" where \"ID\"=?;";
                pstm = conn.prepareStatement(sql);
                pstm.setInt(1, id[0]);
                rs = pstm.executeQuery();
                if(rs.next()) {
                    Owner owner = new Owner();
                    owner.setEmail(rs.getString(2));
                    owner.setPw(rs.getString(3));
                    owner.setSex(rs.getBoolean(4));
                    owner.setBirth(rs.getDate(5));
                    owner.setName(rs.getString(6));
                    owner.setPhone(rs.getString(7));
                    owner.setReg_num(rs.getString(8));
                    owner.setMenu_category(rs.getString(9));
                    owner.setAdmition_status(rs.getBoolean(10));
                    genUser.setOwner(owner);
                }
                pstm.close();
                rs.close();
                sql = "select * from \"SCHEDULE\" where \"OWNER_ID\"=?;";
                pstm = conn.prepareStatement(sql);
                pstm.setInt(1, id[0]);
                rs = pstm.executeQuery();
                if(rs.next()) {
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

                    schedule.setId(rs.getInt(1));
                    schedule.setScheduleVO(scheduleVO);
                }
                pstm.close();
                rs.close();
                sql = "select * from \"MENU\" where \"OWNER_ID\"=?;";
                pstm = conn.prepareStatement(sql);
                pstm.setInt(1, id[0]);
                rs = pstm.executeQuery();
                if(rs.next()) {
                    truckMenu.setId(rs.getInt(1));
                    truckMenu.setName(rs.getString(2));
                    truckMenu.setPrice(rs.getInt(3));
                    truckMenu.setIngredients(rs.getString(4));
                    truckMenu.setDetail(rs.getString(5));
                    truckMenu.setStatus(rs.getInt(6));
                    truckMenu.setOwnerid(rs.getInt(7));
                }
                TruckInfoAll.setTruckOwner(genUser);
                TruckInfoAll.setTruckSchedule(schedule);
                TruckInfoAll.setTruckMenu(truckMenu);
            } catch (Exception e) {
                Log.d("PPJY", e.getLocalizedMessage());
                return null;
            } finally {
                try {
                    pstm.close();
                    conn.close();
                } catch (SQLException se){
                    Log.d("SQLException", se.getLocalizedMessage());
                }
            }
            return TruckInfoAll;
        }

        @Override
        protected void onPostExecute(TruckInfoAll truckInfoAll) {
            if(truckInfoAll != null){
                thisTruckInfoAll = truckInfoAll;

                truckName = thisTruckInfoAll.getTruckOwner().getOwner().getName();
                // toolbar 타이틀 푸드트럭 이름으로 저장
                toolbar.setTitle(truckName);

                Resources resources = getApplicationContext().getResources();
                int truckImgRes = resources.getIdentifier(truckName, "drawable",
                        getApplicationContext().getPackageName());

                truckImgView.setImageResource(truckImgRes);
            }
        }
    }
}
