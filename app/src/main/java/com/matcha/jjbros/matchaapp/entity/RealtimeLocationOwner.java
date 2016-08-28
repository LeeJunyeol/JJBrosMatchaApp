package com.matcha.jjbros.matchaapp.entity;

/**
 * Created by jylee on 2016-08-28.
 */
public     class RealtimeLocationOwner {
    private double lat;
    private double lng;
    private int owner_id;
    private String truck_name;

    public RealtimeLocationOwner() {
    }

    public RealtimeLocationOwner(double lat, double lng, int owner_id, String truck_name) {
        this.lat = lat;
        this.lng = lng;
        this.owner_id = owner_id;
        this.truck_name = truck_name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
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

    public String getTruck_name() {
        return truck_name;
    }

    public void setTruck_name(String truck_name) {
        this.truck_name = truck_name;
    }
}
