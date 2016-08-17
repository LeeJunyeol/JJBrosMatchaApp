package com.matcha.jjbros.matchaapp.user;

import android.graphics.drawable.Drawable;

/**
 * Created by Yu on 2016-07-11.
 */
public class UserBestFoodtuckItem {
    private int rank;
    private int img;
    private double star;
    private String name;
    private String category;

    public UserBestFoodtuckItem() {
    }

    public UserBestFoodtuckItem(int rank, int img, double star, String category, String name) {
        this.rank = rank;
        this.img = img;
        this.star = star;
        this.category = category;
        this.name = name;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public void setStar(double star) {
        this.star = star;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getRank() {
        return rank;
    }

    public int getImg() {
        return img;
    }

    public double getStar() {
        return star;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }


}

