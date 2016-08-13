package com.matcha.jjbros.matchaapp.entity;

/**
 * Created by jylee on 2016-08-13.
 */
public class TruckMenu {
    private int id;
    private String name;
    private int price;
    private String ingredients;
    private String detail;
    private int status;
    private int ownerid;

    public TruckMenu() {
    }

    public TruckMenu(int id, String name, int price, String ingredients, String detail, int status, int ownerid) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.ingredients = ingredients;
        this.detail = detail;
        this.status = status;
        this.ownerid = ownerid;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getOwnerid() {
        return ownerid;
    }

    public void setOwnerid(int ownerid) {
        this.ownerid = ownerid;
    }
}
