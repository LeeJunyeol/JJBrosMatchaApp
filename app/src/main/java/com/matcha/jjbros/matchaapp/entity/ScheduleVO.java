package com.matcha.jjbros.matchaapp.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Date;
import java.sql.Time;

/**
 * Created by hadoop on 16. 7. 20.
 */
public class ScheduleVO implements Parcelable {
    private double lat;
    private double lng;
    private Date start_date;
    private Date end_date;
    private Time start_time;
    private Time end_time;
    private String day;
    private boolean repeat;
    private int owner_id;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.lat);
        dest.writeDouble(this.lng);
        dest.writeValue(this.start_date);
        dest.writeValue(this.end_date);
        dest.writeValue(this.start_time);
        dest.writeValue(this.end_time);
        dest.writeString(this.day);
        dest.writeValue(this.repeat);
        dest.writeInt(this.owner_id);
    }

    public ScheduleVO(Parcel in){
        this.lat = in.readDouble();
        this.lng = in.readDouble();
        this.start_date = Date.class.cast(in.readValue(Date.class.getClassLoader()));
        this.end_date = Date.class.cast(in.readValue(Date.class.getClassLoader()));
        this.start_time = Time.class.cast(in.readValue(Time.class.getClassLoader()));
        this.end_time = Time.class.cast(in.readValue(Time.class.getClassLoader()));
        this.day = in.readString();
        this.repeat = Boolean.class.cast(in.readValue(Boolean.class.getClassLoader()));
        this.owner_id = in.readInt();
    }

    public static final Creator<ScheduleVO> CREATOR = new Creator<ScheduleVO>(){
        public ScheduleVO createFromParcel(Parcel in){ return new ScheduleVO(in); }
        public ScheduleVO[] newArray (int size) { return new ScheduleVO[size]; }
    };

    public ScheduleVO() {
    }

    public ScheduleVO(double lat, double lng, Date start_date, Date end_date, Time start_time, Time end_time, String day, boolean repeat, int owner_id) {
        this.lat = lat;
        this.lng = lng;
        this.start_date = start_date;
        this.end_date = end_date;
        this.start_time = start_time;
        this.end_time = end_time;
        this.day = day;
        this.repeat = repeat;
        this.owner_id = owner_id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public Time getStart_time() {
        return start_time;
    }

    public void setStart_time(Time start_time) {
        this.start_time = start_time;
    }

    public Time getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Time end_time) {
        this.end_time = end_time;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public boolean isRepeat() {
        return repeat;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    public int getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(int owner_id) {
        this.owner_id = owner_id;
    }
}
