package com.matcha.jjbros.matchaapp.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Date;

/**
 * Created by jylee on 2016-07-10.
 */
public class User implements Parcelable {
    private String email;
    private String pw;
    private Boolean sex;
    private Date birth;

    public User(){}

    public User(String email, String pw, Boolean sex, Date birth){
        this.email = email;
        this.pw = pw;
        this.sex = sex;
        this.birth = birth;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.email);
        dest.writeString(this.pw);
        dest.writeValue(this.sex);
        dest.writeValue(this.birth);
    }

    public User(Parcel in) {
        this.email = in.readString();
        this.pw = in.readString();
        this.sex = Boolean.class.cast(in.readValue(Boolean.class.getClassLoader()));
        this.birth = Date.class.cast(in.readValue(Date.class.getClassLoader()));
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }
        public User[] newArray (int size) {
            return new User[size];
        }
    };

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }
}
