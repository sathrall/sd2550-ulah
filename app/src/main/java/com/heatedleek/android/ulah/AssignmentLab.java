package com.heatedleek.android.ulah;

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * Created by fexofenadine180mg on 8/22/15.
 */
public class AssignmentLab {

    private ArrayList<Assignment> Assignments;
    private ArrayList<Assignment> CourseAssignments;

    private static AssignmentLab AssignmentLab;

    private Context AppContext;

    private AssignmentLab(Context appContext){
        AppContext = appContext;
        Assignments = new ArrayList<Assignment>();
    }

    public static AssignmentLab get(Context c){
        if (AssignmentLab == null)
            AssignmentLab = new AssignmentLab(c.getApplicationContext());

        return AssignmentLab;
    }

    public ArrayList<Assignment> getAssignments(){
        return Assignments;
    }

    public Assignment getAssignment(UUID id) {
        for (Assignment a : Assignments) {
            if (a.getId().equals(id)) {
                return a;
            }
        }

        return null;
    }

    public void addAssignment(String title, UUID courseId, Date date,
                              String details, boolean completed) {
        Assignment a = new Assignment();
        a.setTitle(title);
        a.setCourseId(courseId);
        a.setDueDate(date);
        a.setDetails(details);
        a.setCompleted(completed);
        Assignments.add(a);
    }

}
