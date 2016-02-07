package com.heatedleek.android.ulah;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by fexofenadine180mg on 8/13/15.
 */
public class ViewCourseFragment extends Fragment {

    private ListView assignmentListView;
    private ArrayList<Assignment> assignments;
    private ArrayList<Assignment> courseAssignments;
    private java.text.DateFormat dateFormat;

    private Course course;
    private TextView titleField;
    private TextView dayOfWeekField;

    public static final String EXTRA_COURSE_ID = "com.heatedleek.android.ulah.courseId";

    OnAssignmentSelectedListener listener;

    public interface OnAssignmentSelectedListener {
        public void onAssignmentSelected(Assignment assignment);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // make sure that the container activity has implemented
        // the callback interface; if not, throw an exception
        try {
            listener = (OnAssignmentSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnAssignmentSelectedListener");
        }
    }

    public static ViewCourseFragment newInstance(UUID courseId) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_COURSE_ID, courseId);

        ViewCourseFragment fragment = new ViewCourseFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UUID courseId = (UUID)getArguments().getSerializable(EXTRA_COURSE_ID);

        course = CourseLab.get(getActivity()).getCourse(courseId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.course_details_view, parent, false);

        // populate the course header
        titleField = (TextView)v.findViewById(R.id.course_title);
        titleField.setText(course.getTitle());
        dayOfWeekField = (TextView)v.findViewById(R.id.course_day);
        dayOfWeekField.setText(course.getDayOfWeek());

        // grab the assignments
        assignments = AssignmentLab.get(getActivity()).getAssignments();
        courseAssignments = new ArrayList<Assignment>();
        for (Assignment a : assignments) {
            if (a.getCourseId().equals(course.getId())) {
                courseAssignments.add(a);
            }
        }

        if (courseAssignments.size() > 0) {
            assignmentListView = (ListView) v.findViewById(R.id.assignments_list);

            AssignmentAdapter adapter = new AssignmentAdapter(courseAssignments);

            assignmentListView.setAdapter(adapter);

            assignmentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Assignment a = (Assignment) assignmentListView.getAdapter().getItem(position);
                    listener.onAssignmentSelected(a);
                }
            });
        }

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set the toolbar title
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(course.getCourseNumber());
    }

    private class AssignmentAdapter extends ArrayAdapter<Assignment> {

        public AssignmentAdapter(ArrayList<Assignment> assignments) {
            super(getActivity(), 0, assignments);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // If we weren't given a view, we'll inflate one!
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.assignment_list_item, null);
            }

            // Configure the view for this assignment
            Assignment a = getItem(position);
            dateFormat = new SimpleDateFormat("LLL d, yyyy");

            TextView assignmentDueDateField = (TextView) convertView.findViewById(R.id.assignment_due_date);
            assignmentDueDateField.setText(dateFormat.format(a.getDueDate()).toString());
            TextView assignmentTextField = (TextView) convertView.findViewById(R.id.assignment_title);
            assignmentTextField.setText(a.getTitle());
            CheckBox assignmentIsCompleted = (CheckBox) convertView.findViewById(R.id.assignment_completed);
            assignmentIsCompleted.setChecked(a.isCompleted());

            return convertView;
        }

    }

}
