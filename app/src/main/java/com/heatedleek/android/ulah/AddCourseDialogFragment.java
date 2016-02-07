package com.heatedleek.android.ulah;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Created by fexofenadine180mg on 8/22/15.
 */
public class AddCourseDialogFragment extends Fragment {

    private EditText titleField;
    private EditText courseNumberField;
    private Spinner spinner;
    private Button submitButton;
    private Button cancelButton;

    private Course course;
    private String title = "";
    private String courseNumber = "";
    private String dayOfWeek = "";

    OnAddCourseEndListener listener;

    public interface OnAddCourseEndListener {
        public void onAddCourseEnd();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // check to see if OnAddCourseEndListener
        // is implemented by host activity
        try {
            listener = (OnAddCourseEndListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement OnAddCourseEndListener"
            );
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_course_form, parent, false);

        // listen for the text change
        titleField = (EditText) v.findViewById(R.id.course_title);
        titleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                title = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //
            }

            @Override
            public void afterTextChanged(Editable s) {
                title = s.toString();
            }
        });

        // listen for the text change
        courseNumberField = (EditText) v.findViewById(R.id.course_number);
        courseNumberField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                courseNumber = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //
            }

            @Override
            public void afterTextChanged(Editable s) {
                courseNumber = s.toString();
            }
        });

        // populate the spinner
        spinner = (Spinner) v.findViewById(R.id.course_day);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.course_days, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dayOfWeek = parent.getItemAtPosition(position).toString();
                dayOfWeek = dayOfWeek.substring(0, 1);
            }

            public void onNothingSelected(AdapterView<?> parent) {
                dayOfWeek = "M";
            }
        });

        // capture cancel action
        cancelButton = (Button) v.findViewById(R.id.cancel);
        cancelButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                listener.onAddCourseEnd();
            }
        });

        // capture submit action
        submitButton = (Button) v.findViewById(R.id.submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CourseLab courseLab = CourseLab.get(getActivity());

                if (title.equals("")) {
                    Toast.makeText(getActivity(), "You must enter a course name.", Toast.LENGTH_SHORT).show();
                } else if (courseNumber.equals("")) {
                    Toast.makeText(getActivity(), "You must enter a course number.", Toast.LENGTH_SHORT).show();
                } else if (dayOfWeek.equals("")) {
                    Toast.makeText(getActivity(), "You must set the day of the week.", Toast.LENGTH_SHORT).show();
                } else {
                    courseLab.addCourse(title, courseNumber, dayOfWeek);
                    listener.onAddCourseEnd();
                }
            }
        });

        return v;
    }

}
