package com.matcha.jjbros.matchaapp.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Yu on 2016-07-28.
 */
public class TruckLocations{


    int owner_id;
    String name;
    String phone;
    String menu_category;
    double lat;
    double lng;

    public TruckLocations(int owner_id, String name, String phone, String menu_category, double lat, double lng) {
        this.owner_id = owner_id;
        this.name = name;
        this.phone = phone;
        this.menu_category = menu_category;
        this.lat = lat;
        this.lng = lng;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public int getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(int owner_id) {
        this.owner_id = owner_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMenu_category() {
        return menu_category;
    }

    public void setMenu_category(String menu_category) {
        this.menu_category = menu_category;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
}
