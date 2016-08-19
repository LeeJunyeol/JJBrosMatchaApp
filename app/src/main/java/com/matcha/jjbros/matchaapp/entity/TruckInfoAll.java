package com.matcha.jjbros.matchaapp.entity;

import java.util.ArrayList;

/**
 * Created by jylee on 2016-08-13.
 */
public class TruckInfoAll {
    private GenUser truckOwner;
    private ArrayList<Schedule> truckSchedule;
    private ArrayList<TruckMenu> truckMenu;

    public TruckInfoAll() {
    }

    public TruckInfoAll(GenUser truckOwner, ArrayList<Schedule> truckSchedule, ArrayList<TruckMenu> truckMenu) {
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

    public ArrayList<Schedule> getTruckSchedule() {
        return truckSchedule;
    }

    public void setTruckSchedule(ArrayList<Schedule> truckSchedule) {
        this.truckSchedule = truckSchedule;
    }

    public ArrayList<TruckMenu> getTruckMenu() {
        return truckMenu;
    }

    public void setTruckMenu(ArrayList<TruckMenu> truckMenu) {
        this.truckMenu = truckMenu;
    }
}
