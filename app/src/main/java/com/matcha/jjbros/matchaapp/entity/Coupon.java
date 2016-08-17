package com.matcha.jjbros.matchaapp.entity;

import java.util.Date;

/**
 * Created by jylee on 2016-08-17.
 */
public class Coupon {
    private int id;
    private String name;
    private Date start_date;
    private Date end_date;
    private String detail;
    private int owner_id;

    public Coupon() {
    }

    public Coupon(String name, Date start_date, Date end_date, String detail, int owner_id) {
        this.name = name;
        this.start_date = start_date;
        this.end_date = end_date;
        this.detail = detail;
        this.owner_id = owner_id;
    }

    public Coupon(int id, String name, Date start_date, Date end_date, String detail, int owner_id) {
        this.id = id;
        this.name = name;
        this.start_date = start_date;
        this.end_date = end_date;
        this.detail = detail;
        this.owner_id = owner_id;
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

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
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

    public int getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(int owner_id) {
        this.owner_id = owner_id;
    }
}
