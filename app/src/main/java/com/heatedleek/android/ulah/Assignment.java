package com.heatedleek.android.ulah;

import java.util.Date;
import java.util.UUID;

/**
 * Created by fexofenadine180mg on 8/22/15.
 */
public class Assignment {

    private UUID id;
    private String title;
    private UUID courseId;
    private Date dueDate;
    private String details;
    private boolean completed;

    public Assignment() {
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

    public void setCourseId (UUID c) {
        courseId = c;
    }

    public UUID getCourseId() {
        return courseId;
    }

    public void setDueDate(Date d) {
        dueDate = d;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDetails(String d) {
        details = d;
    }

    public String getDetails() {
        return details;
    }

    public void setCompleted(boolean c) {
        completed = c;
    }

    public boolean isCompleted() {
        return completed;
    }

}
