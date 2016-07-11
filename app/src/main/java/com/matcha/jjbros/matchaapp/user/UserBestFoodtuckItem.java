package com.matcha.jjbros.matchaapp.user;

import android.graphics.drawable.Drawable;

/**
 * Created by Yu on 2016-07-11.
 */
public class UserBestFoodtuckItem {

        private String rank;
        private Drawable img;
        private double star;
        private String name;
        private String category;
        private String city;
        private String phone;

    public UserBestFoodtuckItem(String rank, Drawable img, double star, String phone, String city, String category, String name) {
        this.rank = rank;
        this.img = img;
        this.star = star;
        this.phone = phone;
        this.city = city;
        this.category = category;
        this.name = name;
    }

    public String getRank() {
        return rank;
    }

    public Drawable getImg() {
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

    public String getCity() {
        return city;
    }

    public String getPhone() {
        return phone;
    }
}



}
