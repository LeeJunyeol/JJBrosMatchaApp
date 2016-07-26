package com.matcha.jjbros.matchaapp.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Date;

/**
 * Created by jylee on 2016-07-10.
 */
public class Owner extends User implements Parcelable {
    private String name;
    private String phone;
    private String reg_num;
    private String menu_category;
    private Boolean admition_status = false;

    public Owner() {
        super();
    }

    public Owner(String email, String pw, Boolean sex, Date birth, String name, String phone, String reg_num, String menu_category, Boolean admition_status) {
        super(email, pw, sex, birth);
        this.name = name;
        this.phone = phone;
        this.reg_num = reg_num;
        this.menu_category = menu_category;
        this.admition_status = admition_status;
    }

    @Override
    public int describeContents() {
        return super.describeContents();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.name);
        dest.writeString(this.phone);
        dest.writeString(this.reg_num);
        dest.writeString(this.menu_category);
        dest.writeValue(this.admition_status);
    }

    public Owner(Parcel in) {
        super.setEmail(in.readString());
        super.setPw(in.readString());
        super.setSex(Boolean.class.cast(in.readValue(Boolean.class.getClassLoader())));
        super.setBirth(Date.class.cast(in.readValue(Date.class.getClassLoader())));
        this.name = in.readString();
        this.phone = in.readString();
        this.reg_num = in.readString();
        this.menu_category = in.readString();
        this.admition_status = Boolean.class.cast(in.readValue(Boolean.class.getClassLoader()));
    }

    public static final Parcelable.Creator<Owner> CREATOR = new Parcelable.Creator<Owner>() {
        public Owner createFromParcel(Parcel in) {
            return new Owner(in);
        }
        public Owner[] newArray (int size) {
            return new Owner[size];
        }
    };

    @Override
    public String getEmail() {
        return super.getEmail();
    }

    @Override
    public void setEmail(String email) {
        super.setEmail(email);
    }

    @Override
    public String getPw() {
        return super.getPw();
    }

    @Override
    public void setPw(String pw) {
        super.setPw(pw);
    }

    @Override
    public boolean isSex() {
        return super.isSex();
    }

    @Override
    public void setSex(boolean sex) {
        super.setSex(sex);
    }

    @Override
    public Date getBirth() {
        return super.getBirth();
    }

    @Override
    public void setBirth(Date birth) {
        super.setBirth(birth);
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

    public String getReg_num() {
        return reg_num;
    }

    public void setReg_num(String reg_num) {
        this.reg_num = reg_num;
    }

    public String getMenu_category() {
        return menu_category;
    }

    public void setMenu_category(String menu_category) {
        this.menu_category = menu_category;
    }

    public Boolean getAdmition_status() {
        return admition_status;
    }

    public void setAdmition_status(Boolean admition_status) {
        this.admition_status = admition_status;
    }

    public static Creator<Owner> getCREATOR() {
        return CREATOR;
    }
}
