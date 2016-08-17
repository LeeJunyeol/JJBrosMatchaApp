package com.matcha.jjbros.matchaapp.owner;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.matcha.jjbros.matchaapp.R;
import com.matcha.jjbros.matchaapp.entity.Coupon;

import java.util.Date;

/**
 * Created by jylee on 2016-08-17.
 */
public class AddCouponActivity extends AppCompatActivity {
/*
    <EditText android:id="@+id/et_coupon_name"
    <TextView android:id="@+id/tv_expiration_date"
    <DatePicker
    android:id="@+id/dp_start"
    <DatePicker
    android:id="@+id/dp_end"
    <EditText android:id="@+id/et_contents"
    <Button android:id="@+id/btn_add"
*/
    private EditText etCouponName;
    private TextView tvExpireDate;
    private DatePicker dpStartDate;
    private DatePicker dpEndDate;
    private EditText etContents;
    private Button btnAdd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_coupon);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etCouponName = (EditText) findViewById(R.id.et_coupon_name);
        tvExpireDate = (TextView) findViewById(R.id.tv_expiration_date);
        dpStartDate = (DatePicker) findViewById(R.id.dp_start);
        dpEndDate = (DatePicker) findViewById(R.id.dp_end);
        etContents = (EditText) findViewById(R.id.et_contents);
        btnAdd = (Button) findViewById(R.id.btn_add);

        btnAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                etCouponName.getText();
                Coupon coupon = new Coupon();
                coupon.setName(etCouponName.getText().toString());
                coupon.setStart_date(new Date(dpStartDate.getCalendarView().getDate()));
                coupon.setEnd_date(new Date(dpEndDate.getCalendarView().getDate()));
                coupon.setDetail(etContents.getText().toString());


            }
        });
    }

    public class InputCoupon extends AsyncTask<Coupon, Integer, Integer>{
        @Override
        protected Integer doInBackground(Coupon... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
        }
    }

}
