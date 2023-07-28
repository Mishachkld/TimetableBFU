package com.example.timetablebfu.Components;

public class ScheduleList {
    private String nameOfWeek;
    private String homeWork;
    private String lessons;
    private int id;

    public ScheduleList(int id, String nameOfWeek, String homeWork){
        this.id = id;
        this.nameOfWeek = nameOfWeek;
        this.homeWork = homeWork;
    }

    public ScheduleList(int id, String nameOfWeek, String lessons, String homeWork){
        this.id = id;
        this.nameOfWeek = nameOfWeek;
        this.homeWork = homeWork;
        this.lessons = lessons;
    }


    public String getNameOfWeek() {
        return nameOfWeek;
    }

    public void setNameOfWeek(String nameOfWeek) {
        this.nameOfWeek = nameOfWeek;
    }

    public String getHomeWork() {
        return homeWork;
    }
    public String getLessons() {
        return lessons;
    }

    public void setHomeWork(String homeWork) {
        this.homeWork = homeWork;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
