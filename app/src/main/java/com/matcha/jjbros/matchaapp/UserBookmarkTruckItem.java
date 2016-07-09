package com.matcha.jjbros.matchaapp;

/**
 * Created by Yu on 2016-07-08.
 */
public class UserBookmarkTruckItem {

    private int photo;
    private String name;
    private String category;
    private String city;
    private String phone;

    public int getPhoto() {
        return photo;
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

    public UserBookmarkTruckItem(int photo, String phone, String city, String category, String name) {
        this.photo = photo;
        this.phone = phone;
        this.city = city;
        this.category = category;
        this.name = name;
    }
}



