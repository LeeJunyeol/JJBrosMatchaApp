package com.matcha.jjbros.matchaapp.owner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.matcha.jjbros.matchaapp.R;
import com.matcha.jjbros.matchaapp.entity.Coupon;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Locale;

/**
 * Created by jylee on 2016-08-26.
 */
public class CouponAdapter  extends BaseAdapter {
    private ArrayList<Coupon> coupons = new ArrayList<>();
    private LayoutInflater inflater;

    public CouponAdapter(ArrayList<Coupon> coupons, LayoutInflater inflater) {
        this.coupons = coupons;
        this.inflater = inflater;
    }

    @Override
    public int getCount() {
        return coupons.size();
    }

    @Override
    public Object getItem(int position) {
        return coupons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.custom_coupon_item, null);
        }

        Coupon coupon = coupons.get(position);

        TextView tvCouponName = (TextView)convertView.findViewById(R.id.tv_coupon_name);
        TextView tvExpirationDate = (TextView)convertView.findViewById(R.id.tv_expiration_date);
        TextView tvContents = (TextView)convertView.findViewById(R.id.tv_contents);

        tvCouponName.setText(coupon.getName());
        SimpleDateFormat formatter = new SimpleDateFormat("yy년 MM월 dd일", Locale.KOREA);
        tvExpirationDate.setText("유효기간: " + formatter.format(coupon.getEnd_date()) + "까지");
        tvContents.setText("상세: " + coupon.getDetail());

        return convertView;
    }


}
