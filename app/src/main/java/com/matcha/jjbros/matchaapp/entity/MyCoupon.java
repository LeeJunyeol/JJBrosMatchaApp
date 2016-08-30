package com.matcha.jjbros.matchaapp.entity;

import java.util.Date;

/**
 * Created by jylee on 2016-08-17.
 */
public class MyCoupon {
    private int id;
    private String name;
    private Date end_date;
    private String detail;
    private String serialNum;
    private Boolean isUsed;
    private int owner_id;
    private int user_id;
    private String truckname;

    public MyCoupon() {
    }

    public MyCoupon(int id, String name, Date end_date, String detail, String serialNum, Boolean isUsed, int owner_id, int user_id, String truckname) {
        this.id = id;
        this.name = name;
        this.end_date = end_date;
        this.detail = detail;
        this.serialNum = serialNum;
        this.isUsed = isUsed;
        this.owner_id = owner_id;
        this.user_id = user_id;
        this.truckname = truckname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(Boolean used) {
        isUsed = used;
    }

    public int getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(int owner_id) {
        this.owner_id = owner_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getTruckname() {
        return truckname;
    }

    public void setTruckname(String truckname) {
        this.truckname = truckname;
    }
}
