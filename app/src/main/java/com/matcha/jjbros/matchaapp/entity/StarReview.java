package com.matcha.jjbros.matchaapp.entity;

import java.util.Date;

/**
 * Created by jylee on 2016-08-14.
 */
public class StarReview {
    private int id;
    private int userid;
    private double star;
    private String review;
    private Date date;
    private int ownerid;

    public StarReview() {
    }

    public StarReview(int id, int userid, double star, String review, Date date, int ownerid) {
        this.id = id;
        this.userid = userid;
        this.star = star;
        this.review = review;
        this.date = date;
        this.ownerid = ownerid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getStar() {
        return star;
    }

    public void setStar(double star) {
        this.star = star;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getOwnerid() {
        return ownerid;
    }

    public void setOwnerid(int ownerid) {
        this.ownerid = ownerid;
    }
}
