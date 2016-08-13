package com.matcha.jjbros.matchaapp.common;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by jylee on 2016-08-13.
 */
public class LocationConverter {
    public static String getAddress(Context mContext, double lat, double lng){
        String nowAddress = "미등록 주소";
        Geocoder geocoder = new Geocoder(mContext, Locale.KOREA);
        List<Address> address;
        try {
            if(geocoder != null ){
                address = geocoder.getFromLocation(lat, lng, 1);
                if(address != null && address.size() > 0){
                    // 주소 받아오기
                    String currentLocationAddress = address.get(0).getAddressLine(0).toString();
                    nowAddress = currentLocationAddress;
                }
            }
        } catch (IOException e){
            Toast.makeText(mContext, "주소를 가져올 수 없습니다.", Toast.LENGTH_LONG).show();
        }
        return nowAddress;
    }
}
