package com.matcha.jjbros.matchaapp.owner;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.matcha.jjbros.matchaapp.R;
import com.matcha.jjbros.matchaapp.common.DBControl;
import com.matcha.jjbros.matchaapp.entity.Coupon;
import com.matcha.jjbros.matchaapp.entity.GenUser;
import com.matcha.jjbros.matchaapp.entity.Schedule;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;

/**
 * Created by jylee on 2016-08-17.
 */
public class AddCouponActivity extends AppCompatActivity {
    private EditText etCouponName;
    private EditText etExpirationDate;
    private EditText etContents;
    private Button btnAdd;

    private GenUser owner = new GenUser();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_coupon);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_add_coupon);
        setSupportActionBar(toolbar);

        owner = (GenUser) getIntent().getParcelableExtra("owner");

        etCouponName = (EditText) findViewById(R.id.et_coupon_name);
        etExpirationDate = (EditText) findViewById(R.id.et_expiration_date);
        etContents = (EditText) findViewById(R.id.et_contents);
        btnAdd = (Button) findViewById(R.id.btn_add);

        btnAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                etCouponName.getText();
                Coupon coupon = new Coupon();
                coupon.setName(etCouponName.getText().toString());
                coupon.setEnd_date(java.sql.Date.valueOf(etExpirationDate.getText().toString()));
                coupon.setDetail(etContents.getText().toString());
                coupon.setOwner_id(owner.getId());
                new InputCoupon().execute(coupon);
            }
        });
    }

    public class InputCoupon extends AsyncTask<Coupon, Integer, Integer>{
        @Override
        protected Integer doInBackground(Coupon... coupons) {
            Coupon c = coupons[0];

            Connection conn = null;
            int result = 0;
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
            int res = 0;
            String sql = "insert into \"COUPON\"(\"ID\", \"NAME\", \"END_DATE\"," +
                    "\"DETAIL\", \"OWNER_ID\") values (DEFAULT,?,?,?,?)";
            try {
                pstm = conn.prepareStatement(sql);
                pstm.setString(1, c.getName());
                pstm.setDate(2, java.sql.Date.valueOf(c.getEnd_date().toString()));
                pstm.setString(3, c.getDetail());
                pstm.setInt(4, c.getOwner_id());

                res = pstm.executeUpdate();
                if(res > 0){
                    result += 1;
                    conn.commit();
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                try{
                    pstm.close();
                    conn.close();
                } catch (SQLException e){
                    e.printStackTrace();
                    return null;
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            if(result > 0){
                Log.d("db insert","success");
                Toast.makeText(getApplicationContext(), "쿠폰이 성공적으로 추가 되었습니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getBaseContext(), OwnerCouponActivity.class);
                intent.putExtra("owner",owner);
                startActivity(intent);
                finish();
            } else {
                Log.d("db insert","fail");
                Toast.makeText(getApplicationContext(), "쿠폰을 추가하는 데 실패했습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}
