package com.matcha.jjbros.matchaapp.entity;

/**
 * Created by jylee on 2016-08-13.
 */
public class TruckInfoAll {
    private GenUser truckOwner;
    private Schedule truckSchedule;
    private TruckMenu truckMenu;

    public TruckInfoAll() {
    }

    public TruckInfoAll(GenUser truckOwner, Schedule truckSchedule, TruckMenu truckMenu) {
        this.truckOwner = truckOwner;
        this.truckSchedule = truckSchedule;
        this.truckMenu = truckMenu;
    }

    public GenUser getTruckOwner() {
        return truckOwner;
    }

    public void setTruckOwner(GenUser truckOwner) {
        this.truckOwner = truckOwner;
    }

    public Schedule getTruckSchedule() {
        return truckSchedule;
    }

    public void setTruckSchedule(Schedule truckSchedule) {
        this.truckSchedule = truckSchedule;
    }

    public TruckMenu getTruckMenu() {
        return truckMenu;
    }

    public void setTruckMenu(TruckMenu truckMenu) {
        this.truckMenu = truckMenu;
    }
}
