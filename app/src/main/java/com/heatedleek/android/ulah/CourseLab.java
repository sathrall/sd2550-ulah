package com.heatedleek.android.ulah;

import android.content.Context;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by fexofenadine180mg on 8/17/15.
 */
public class CourseLab {

    private ArrayList<Course> Courses;

    private static CourseLab CourseLab;
    private Context AppContext;

    private CourseLab(Context appContext) {
        AppContext = appContext;
        Courses = new ArrayList<Course>();
    }

    public static CourseLab get(Context c) {
        if (CourseLab == null) {
            CourseLab = new CourseLab(c.getApplicationContext());
        }

        return CourseLab;
    }

    public ArrayList<Course> getCourses() {
        return Courses;
    }

    public Course getCourse(UUID id) {
        for (Course c : Courses) {
            if (c.getId().equals(id)) {
                return c;
            }
        }

        return null;
    }

    public void addCourse(String t, String c, String d) {
        Course course = new Course();
        course.setTitle(t);
        course.setCourseNumber(c);
        course.setDayOfWeek(d);
        Courses.add(course);
    }

}
