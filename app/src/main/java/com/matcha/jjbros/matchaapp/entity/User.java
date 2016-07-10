package com.matcha.jjbros.matchaapp.entity;

import java.sql.Date;

/**
 * Created by jylee on 2016-07-10.
 */
public class User {
    private String email;
    private String pw;
    private boolean sex;
    private Date birth;

    public User() {
    }

    public User(String email, String pw, boolean sex, Date birth){
        this.email = email;
        this.pw = pw;
        this.sex = sex;
        this.birth = birth;
    }

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
