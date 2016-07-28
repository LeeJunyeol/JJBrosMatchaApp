package com.matcha.jjbros.matchaapp.entity;

/**
 * Created by hadoop on 16. 7. 27.
 */
public class TruckScheduleInfo {
    private int schedule_id;
    private ScheduleVO scheduleVO;
    private int owner_id;
    private String name;
    private String email;
    private String phone;
    private String menu_category;

    public TruckScheduleInfo() {
    }

    public TruckScheduleInfo(int schedule_id, ScheduleVO scheduleVO, int owner_id, String name, String email, String phone, String menu_category) {
        this.schedule_id = schedule_id;
        this.scheduleVO = scheduleVO;
        this.owner_id = owner_id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.menu_category = menu_category;
    }

    public int getSchedule_id() {
        return schedule_id;
    }

    public void setSchedule_id(int schedule_id) {
        this.schedule_id = schedule_id;
    }

    public ScheduleVO getScheduleVO() {
        return scheduleVO;
    }

    public void setScheduleVO(ScheduleVO scheduleVO) {
        this.scheduleVO = scheduleVO;
    }

    public int getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(int owner_id) {
        this.owner_id = owner_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMenu_category() {
        return menu_category;
    }

    public void setMenu_category(String menu_category) {
        this.menu_category = menu_category;
    }
}
