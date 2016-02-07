package com.heatedleek.android.ulah;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by fexofenadine180mg on 8/17/15.
 */
public class Course {

    private UUID id;
    private String title;
    private String courseNumber;
    private String dayOfWeek;

    public Course() {
        id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }

    public void setTitle(String t) {
        title = t;
    }

    public String getTitle() {
        return title;
    }

    public void setCourseNumber(String c) {
        courseNumber = c;
    }

    public String getCourseNumber() {
        return courseNumber;
    }

    public void setDayOfWeek(String d) {
        dayOfWeek = d;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

}
