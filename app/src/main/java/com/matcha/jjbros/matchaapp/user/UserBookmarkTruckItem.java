package com.matcha.jjbros.matchaapp.user;

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



    public UserBookmarkTruckItem(int photo, String category, String name) {
        this.photo = photo;
        this.category = category;
        this.name = name;
    }
}



