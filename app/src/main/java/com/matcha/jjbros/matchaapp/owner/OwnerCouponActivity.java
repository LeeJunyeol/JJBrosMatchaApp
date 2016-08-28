package com.matcha.jjbros.matchaapp.owner;

import android.content.Context;
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
import android.widget.ListView;
import android.widget.Toast;

import com.matcha.jjbros.matchaapp.R;
import com.matcha.jjbros.matchaapp.common.DBControl;
import com.matcha.jjbros.matchaapp.entity.Coupon;
import com.matcha.jjbros.matchaapp.entity.GenUser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
                        Toast.makeText(getApplicationContext(), c.getName(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

}
