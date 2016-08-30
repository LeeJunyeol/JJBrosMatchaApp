package com.matcha.jjbros.matchaapp.user;

import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.matcha.jjbros.matchaapp.R;
import com.matcha.jjbros.matchaapp.entity.Coupon;
import com.matcha.jjbros.matchaapp.entity.MyCoupon;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by jylee on 2016-08-26.
 */
public class UserCouponAdapter extends BaseAdapter {
    private ArrayList<MyCoupon> myCoupons = new ArrayList<>();
    private LayoutInflater inflater;

    public UserCouponAdapter(ArrayList<MyCoupon> myCoupons, LayoutInflater inflater) {
        this.myCoupons = myCoupons;
        this.inflater = inflater;
    }



    @Override
    public int getCount() {
        return myCoupons.size();
    }

    @Override
    public Object getItem(int position) {
        return myCoupons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.custom_user_coupon_item, null);
        }

        MyCoupon myCoupon = myCoupons.get(position);

        ImageView ivCouponImg = (ImageView)convertView.findViewById(R.id.iv_coupon_img);
        TextView tvCouponName = (TextView)convertView.findViewById(R.id.tv_coupon_name);
        TextView tvExpirationDate = (TextView)convertView.findViewById(R.id.tv_expiration_date);
        TextView tvContents = (TextView)convertView.findViewById(R.id.tv_contents);
        TextView btnUseCoupon = (TextView)convertView.findViewById(R.id.btn_use_coupon_user);
        String truckImgName =  "truck" + myCoupon.getOwner_id();
        Log.d("truckImgName", truckImgName);

        Resources resources = parent.getContext().getResources();
        final int badge = resources.getIdentifier(truckImgName, "drawable",
                parent.getContext().getPackageName());

        ivCouponImg.setImageResource(badge);

        tvCouponName.setText(myCoupon.getTruckname() + ": " + myCoupon.getName());
        SimpleDateFormat formatter = new SimpleDateFormat("yy년 MM월 dd일", Locale.KOREA);
        tvExpirationDate.setText("유효기간: " + formatter.format(myCoupon.getEnd_date()) + "까지");
        tvContents.setText("상세: " + myCoupon.getDetail());
        if(myCoupon.isUsed()){
            btnUseCoupon.setText("사용함");
            btnUseCoupon.setBackgroundResource(R.drawable.rectangle_green_a400);
        }

        return convertView;
    }


}
