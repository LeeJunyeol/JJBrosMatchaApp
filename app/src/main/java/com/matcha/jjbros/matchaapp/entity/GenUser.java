package com.matcha.jjbros.matchaapp.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hadoop on 16. 7. 13.
 */
public class GenUser implements Parcelable{
    private int id;
    private User user = null;
    private Owner owner = null;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeParcelable(user, flags);
        dest.writeParcelable(owner, flags);
    }

    public GenUser(Parcel in){
        this.id = in.readInt();
        this.user = (User) in.readParcelable(User.class.getClassLoader());
        this.owner = (Owner) in.readParcelable(Owner.class.getClassLoader());
    }

    public static final Parcelable.Creator<GenUser> CREATOR = new Parcelable.Creator<GenUser>(){
        @Override
        public GenUser createFromParcel(Parcel in) {
            return new GenUser(in);
        }

        @Override
        public GenUser[] newArray(int size) {
            return new GenUser[size];
        }
    };

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
