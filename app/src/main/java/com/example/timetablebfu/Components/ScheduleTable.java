package com.example.timetablebfu.Components;

import java.util.List;

public class ScheduleTable {

    private int id;
    private String date;
    private List<String> lessonsList;
    private List<String> homeworksList;

    public ScheduleTable(int id, String date, List<String> lessonsList, List<String> homeworksList) {
        this.id = id;
        this.date = date;
        this.lessonsList = lessonsList;
        this.homeworksList = homeworksList;
    }

    public String getDate() {
        return date;
    }

    public List<String> getLessonsList() {
        return lessonsList;
    }

    public List<String> getHomeworksList() {
        return homeworksList;
    }
}
