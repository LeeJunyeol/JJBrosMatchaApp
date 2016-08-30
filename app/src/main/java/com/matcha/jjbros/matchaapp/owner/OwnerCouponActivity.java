package com.matcha.jjbros.matchaapp.owner;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.matcha.jjbros.matchaapp.R;
import com.matcha.jjbros.matchaapp.common.DBControl;
import com.matcha.jjbros.matchaapp.entity.Coupon;
import com.matcha.jjbros.matchaapp.entity.GenUser;
import com.matcha.jjbros.matchaapp.user.UserMainActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;

/**
 * Created by hadoop on 16. 7. 7.
 */
public class OwnerCouponActivity extends AppCompatActivity {

    private GenUser owner = new GenUser();
    private LayoutInflater mInflater;
    private CouponAdapter couponAdapter;
    private ListView lvCoupon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_coupon_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_coupon_owner);
        setSupportActionBar(toolbar);

        owner = (GenUser) getIntent().getParcelableExtra("owner");

        lvCoupon = (ListView) findViewById(R.id.lv_coupon_owner);

        new LoadCoupons().execute(owner.getId());


        FloatingActionButton fabAdd = (FloatingActionButton) findViewById(R.id.btn_add_coupon_owner);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddCouponActivity.class);
                intent.putExtra("owner", owner);
                startActivity(intent);
            }
        });

    }

    public class LoadCoupons extends AsyncTask<Integer, Integer, ArrayList<Coupon>> {
        @Override
        protected ArrayList<Coupon> doInBackground(Integer... id) {
            Connection conn = null;

            ArrayList<Coupon> coupons = new ArrayList<>();

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

            int res = 0;
            String sql = "";
            PreparedStatement pstm = null;
            ResultSet rs = null;
            String tbl_name = "";
            Log.d("SQL", sql);
            try {
                sql = "select * from \"COUPON\" where \"OWNER_ID\"=?;";
                pstm = conn.prepareStatement(sql);
                pstm.setInt(1, id[0]);
                rs = pstm.executeQuery();
                while (rs.next()) {
                    Coupon coupon = new Coupon();
                    coupon.setId(rs.getInt(1));
                    coupon.setName(rs.getString(2));
                    coupon.setEnd_date(rs.getDate(3));
                    coupon.setDetail(rs.getString(4));
                    coupon.setOwner_id(rs.getInt(5));
                    Log.d("couponname", rs.getString(2));
                    coupons.add(coupon);
                }
            } catch (Exception e) {
                Log.d("PPJY", e.getLocalizedMessage());
                return null;
            } finally {
                try {
                    pstm.close();
                    conn.close();
                } catch (SQLException se) {
                    Log.d("SQLException", se.getLocalizedMessage());
                    return null;
                }
            }
            return coupons;
        }

        @Override
        protected void onPostExecute(ArrayList<Coupon> coupons) {
            if(coupons!=null){
                super.onPostExecute(coupons);

                couponAdapter = new CouponAdapter(coupons, getLayoutInflater());
                lvCoupon.setAdapter(couponAdapter);
                lvCoupon.setOnItemClickListener(new ListView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Coupon c = (Coupon) parent.getItemAtPosition(position);
                        showCouponSendAlert(c.getId());
                    }
                });
            }
        }
    }

    public class SendCoupons extends AsyncTask<Integer, Integer, String> {
        @Override
        protected String doInBackground(Integer... coupon_id) {
            Connection conn = null;
            String result = null;
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

            PreparedStatement pstm = null;
            ResultSet rs = null;
            ArrayList<Integer> userIDs = new ArrayList<>();
            int res = 0;
            int check = 0;

            String sql = "SELECT \"USER_ID\" from \"USER_LOCATION\" where ST_Distance_Sphere(ST_SetSRID(ST_MakePoint(126.9983837, " +
                    "37.5853409), 4326),ST_SetSRID(ST_MakePoint(\"USER_LOCATION\".\"location\"[1],\"USER_LOCATION\".\"location\"[0]), " +
                    "4326)) < 3000;";
            try {
                pstm = conn.prepareStatement(sql);
                rs = pstm.executeQuery();
                while(rs.next()){
                    userIDs.add(rs.getInt(1));
                }
                rs.close();
                pstm.close();

                if(userIDs.isEmpty()){
                    result = "주변에 쿠폰을 전송 받을 고객이 존재하지 않습니다.";
                    return result;
                }

                sql = "INSERT INTO \"MYCOUPON\" VALUES(DEFAULT,FALSE,?,?,?)";
                pstm = conn.prepareStatement(sql);
                String serial_num = "";

                for(int i=0; i<userIDs.size(); i++){
                    serial_num = "";
                    serial_num = serial_num.concat(String.valueOf(new Random().nextInt(9)));
                    serial_num = serial_num.concat(String.valueOf(new Random().nextInt(9)));
                    serial_num = serial_num.concat(String.valueOf(new Random().nextInt(9)));
                    serial_num = serial_num.concat(String.valueOf(new Random().nextInt(9)));
                    pstm.setString(1, serial_num);
                    pstm.setInt(2, coupon_id[0]);
                    pstm.setInt(3, userIDs.get(i));

                    res = pstm.executeUpdate();
                    if (res > 0){
                        check ++;
                    }
                    pstm.clearParameters();
                }
                if(check > 0){
                    result = "쿠폰 전송이 완료 되었습니다.";
                }

            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                try{
                    pstm.close();
                    conn.close();
                } catch (SQLException e){
                    Log.d("exception", e.getMessage());
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if(result!=null){
                Toast.makeText(getBaseContext(), result, Toast.LENGTH_SHORT).show();
                super.onPostExecute(result);
            }
        }
    }

    public void showCouponSendAlert(final int coupon_id) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(OwnerCouponActivity.this);

        alertDialog.setTitle("알림");
        alertDialog.setMessage("쿠폰을 발송하겠습니까?");

        alertDialog.setPositiveButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                new SendCoupons().execute(coupon_id);
            }
        });

        alertDialog.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }



/*
    SELECT "location",ST_DWithin(ST_Transform(ST_GeomFromText('POINT(127.01937675476074 37.50916413798163)', 4326), 2097),
    ST_Transform(ST_GeomFromText('POINT(127.01937675476074 37.50916413798163)', 4326), 2097), 100) FROM "REALTIME_LOCATION";
*/

}
