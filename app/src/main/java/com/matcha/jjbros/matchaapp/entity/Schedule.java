package com.matcha.jjbros.matchaapp.entity;

import android.os.Parcel;
import android.os.Parcelable;

import org.postgresql.geometric.PGpoint;

import java.sql.Date;
import java.sql.Time;

/**
 * Created by hadoop on 16. 7. 20.
 */
public class Schedule {
    private int id;
    private int stat;
    private ScheduleVO scheduleVO;

    public Schedule() {
    }

    public Schedule(ScheduleVO scheduleVO) {
        this.scheduleVO = scheduleVO;
    }

    public Schedule(int id, int stat, ScheduleVO scheduleVO) {
        this.id = id;
        this.stat = stat;
        this.scheduleVO = scheduleVO;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStat() {
        return stat;
    }

    public void setStat(int stat) {
        this.stat = stat;
    }

    public ScheduleVO getScheduleVO() {
        return scheduleVO;
    }

    public void setScheduleVO(ScheduleVO scheduleVO) {
        this.scheduleVO = scheduleVO;
    }
}
