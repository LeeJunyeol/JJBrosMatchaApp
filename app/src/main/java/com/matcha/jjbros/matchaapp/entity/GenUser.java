package com.matcha.jjbros.matchaapp.entity;

/**
 * Created by hadoop on 16. 7. 13.
 */
public class GenUser {
    private int id;
    private User user = null;
    private Owner owner = null;

    public GenUser() {
    }

    public GenUser(int id, Owner owner) {
        this.id = id;
        this.owner = owner;
    }

    public GenUser(int id, User user) {
        this.id = id;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

}
