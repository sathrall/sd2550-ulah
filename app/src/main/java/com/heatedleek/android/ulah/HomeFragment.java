package com.heatedleek.android.ulah;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by fexofenadine180mg on 8/22/15.
 */
public class HomeFragment extends Fragment {

    private ListView courseListView;
    private ArrayList<Course> courses;

    OnCourseSelectedListener listener;

    public interface OnCourseSelectedListener {
        public void onCourseSelected(Course c);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // make sure that the container activity has implemented
        // the callback interface; if not, throw exception
        try {
            listener = (OnCourseSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnCourseSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstaceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        courses = CourseLab.get(getActivity()).getCourses();

        courseListView = (ListView) v.findViewById(R.id.home_courses_list);

        CourseAdapter adapter = new CourseAdapter(courses);

        courseListView.setAdapter(adapter);

        courseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CourseAdapter courseAdapter = (CourseAdapter) courseListView.getAdapter();
                Course c = courseAdapter.getItem(position);
                listener.onCourseSelected(c);
            }
        });

        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((CourseAdapter)courseListView.getAdapter()).notifyDataSetChanged();
        // Set the toolbar title
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(R.string.title_home);
    }

    private class CourseAdapter extends ArrayAdapter<Course> {

        public CourseAdapter(ArrayList<Course> courses) {
            super(getActivity(), 0, courses);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // If we weren't given a view, we'll inflate one!
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.course_list_item, null);
            }

            // Configure the view for this Crime
            Course c = getItem(position);

            TextView dayTextView = (TextView)convertView.findViewById(R.id.course_list_item_dayTextView);
            dayTextView.setText(c.getDayOfWeek());
            TextView courseNumberTextView = (TextView)convertView.findViewById(R.id.course_list_item_courseNumberTextView);
            courseNumberTextView.setText(c.getCourseNumber());
            TextView titleTextView = (TextView)convertView.findViewById(R.id.course_list_item_titleTextView);
            titleTextView.setText(c.getTitle());

            return convertView;
        }

    }

}
