package com.matcha.jjbros.matchaapp.user;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.matcha.jjbros.matchaapp.R;
import com.matcha.jjbros.matchaapp.common.DBControl;
import com.matcha.jjbros.matchaapp.common.Values;
import com.matcha.jjbros.matchaapp.entity.MyCoupon;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Created by jylee on 2016-08-27.
 */
public class UserCouponActivity extends AppCompatActivity {
    private UserCouponAdapter userCouponAdapter;
    private ListView lvCoupon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_coupon_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_coupon_user);
        setSupportActionBar(toolbar);

        lvCoupon = (ListView) findViewById(R.id.lv_coupon_user);

        new LoadMyCoupons().execute(Values.GENUSER.getId());

    }

    public class LoadMyCoupons extends AsyncTask<Integer, Integer, ArrayList<MyCoupon>> {
        @Override
        protected ArrayList<MyCoupon> doInBackground(Integer... id) {
            Connection conn = null;

            ArrayList<MyCoupon> myCoupons = new ArrayList<>();

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
                sql = "select \"MYCOUPON\".*, \"COUPON\".\"NAME\", \"COUPON\".\"END_DATE\", \"COUPON\".\"DETAIL\", " +
                        "\"COUPON\".\"OWNER_ID\", (SELECT DISTINCT \"OWNER\".\"NAME\" FROM \"OWNER\", \"COUPON\" WHERE " +
                        "\"OWNER\".\"ID\"=\"COUPON\".\"OWNER_ID\") FROM \"MYCOUPON\" INNER JOIN \"COUPON\" ON " +
                        "\"MYCOUPON\".\"COUPON_ID\"=\"COUPON\".\"ID\" WHERE \"USER_ID\"=?;";
                pstm = conn.prepareStatement(sql);
                pstm.setInt(1, id[0]);
                rs = pstm.executeQuery();
                int cnt = 0;
                while (rs.next()) {
                    MyCoupon myCoupon = new MyCoupon();
                    myCoupon.setId(rs.getInt(1));
                    myCoupon.setUsed(rs.getBoolean(2));
                    myCoupon.setSerialNum(rs.getString(3));
                    myCoupon.setUser_id(rs.getInt(5));
                    myCoupon.setName(rs.getString(6));
                    myCoupon.setEnd_date(rs.getDate(7));
                    myCoupon.setDetail(rs.getString(8));
                    myCoupon.setOwner_id(rs.getInt(9));
                    myCoupon.setTruckname(rs.getString(10));

                    myCoupons.add(myCoupon);
                    Log.d("cnt", String.valueOf(cnt));
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
            return myCoupons;
        }

        @Override
        protected void onPostExecute(ArrayList<MyCoupon> myCoupons) {
            if(myCoupons!=null){
                super.onPostExecute(myCoupons);

                userCouponAdapter = new UserCouponAdapter(myCoupons, getLayoutInflater());
                lvCoupon.setAdapter(userCouponAdapter);
                lvCoupon.setOnItemClickListener(new ListView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    }
                });
            }
        }
    }


}
