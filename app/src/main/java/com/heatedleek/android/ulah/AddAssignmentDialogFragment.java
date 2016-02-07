package com.heatedleek.android.ulah;

import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Created by fexofenadine180mg on 8/22/15.
 */
public class AddAssignmentDialogFragment extends Fragment {

    private EditText titleField;
    private Spinner courseField;
    private Button dueDateField;
    private EditText detailsField;
    private CheckBox completedField;
    private Button submitButton;
    private Button cancelButton;

    private java.text.DateFormat dateFormat;
    private DatePickerDialog datePicker;

    private ArrayList<Course> courses;
    private ArrayList<String> courseNumbers;

    private Assignment assignment;
    private String title = "";
    private UUID course = null;
    private String courseNumber = "";
    private Date dueDate = null;
    private String details = "";
    private boolean isCompleted = false;

    OnAddAssignmentEndListener listener;

    public interface OnAddAssignmentEndListener {
        public void onAddAssignmentEnd();
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);

        try{
            listener = (OnAddAssignmentEndListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement OnAddAssignmentEndListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setDateField();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_assignment_form, parent, false);

        titleField = (EditText) v.findViewById(R.id.assignment_title);
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

        // grab all the courses
        courses = CourseLab.get(getActivity()).getCourses();

        // populate the courseNumbers array
        courseNumbers = new ArrayList<String>();
        for (Course c : courses) {
            courseNumbers.add(c.getCourseNumber());
        }

        // populate the spinner
        courseField = (Spinner) v.findViewById(R.id.assignment_course);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, courseNumbers);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseField.setAdapter(adapter);

        // initialize the spinner item selected listener
        courseField.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // grab the selected item string
                courseNumber = parent.getItemAtPosition(position).toString();

                // loop through courses and find a match
                for (Course c : courses) {
                    if (c.getCourseNumber().equals(courseNumber)) {
                        // when a match is found, grab the id
                        course = c.getId();
                    }
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
                // required empty method
            }
        });

        dueDateField = (Button) v.findViewById(R.id.assignment_due_date);
        dateFormat = new SimpleDateFormat("LLL d, yyyy");
        dueDateField.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                datePicker.show();
            }
        });

        detailsField = (EditText) v.findViewById(R.id.assignment_details);
        detailsField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                details = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //
            }

            @Override
            public void afterTextChanged(Editable s) {
                details = s.toString();
            }
        });

        completedField = (CheckBox) v.findViewById(R.id.assignment_completed);
        completedField.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isCompleted = isChecked;
            }
        });

        cancelButton = (Button) v.findViewById(R.id.cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                listener.onAddAssignmentEnd();
            }
        });

        submitButton = (Button) v.findViewById(R.id.submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AssignmentLab assignmentLab = AssignmentLab.get(getActivity());

                if (title.equals("")) {
                    Toast.makeText(getActivity(), "You must enter a title.", Toast.LENGTH_SHORT).show();
                } else if (course.equals(null)) {
                    Toast.makeText(getActivity(), "You must select a course.", Toast.LENGTH_SHORT).show();
                } else if (dueDate.equals(null)) {
                    Toast.makeText(getActivity(), "You must set the due date.", Toast.LENGTH_SHORT).show();
                } else {
                    assignmentLab.addAssignment(title, course, dueDate, details, isCompleted);
                    listener.onAddAssignmentEnd();
                }
            }
        });

        return v;
    }

    /**
     *  Date field handling
     */

    public void setDateField() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                Calendar c = Calendar.getInstance();
                c.set(year, month, day);
                dueDate = c.getTime();
                updateDate();
            }
        }, year, month, day);
    }

    private void updateDate() {
        if (dueDate != null)
            dueDateField.setText(dateFormat.format(dueDate));
    }
}
