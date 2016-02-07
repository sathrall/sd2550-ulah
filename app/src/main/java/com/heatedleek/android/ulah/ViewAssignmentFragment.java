package com.heatedleek.android.ulah;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Created by fexofenadine180mg on 8/23/15.
 */
public class ViewAssignmentFragment extends Fragment {

    private Assignment assignment;
    private EditText title;
    private Button dueDate;
    private EditText details;
    private CheckBox completed;

    private java.text.DateFormat dateFormat;
    private DatePickerDialog datePicker;
    private Date date;

    private ArrayList<Course> courses;
    private ArrayList<String> courseNumbers;

    public static final String EXTRA_ASSIGNMENT_ID = "com.heatedleek.android.ulah.assignmentId";

    public static ViewAssignmentFragment newInstance(UUID assignmentId) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_ASSIGNMENT_ID, assignmentId);

        ViewAssignmentFragment fragment = new ViewAssignmentFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UUID assignmentId = (UUID)getArguments().getSerializable(EXTRA_ASSIGNMENT_ID);
        assignment = AssignmentLab.get(getActivity()).getAssignment(assignmentId);

        setDateField();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.assignment_details_view, parent, false);

        title = (EditText) v.findViewById(R.id.assignment_title);
        title.setText(assignment.getTitle());
        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                assignment.setTitle(s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //
            }

            @Override
            public void afterTextChanged(Editable s) {
                assignment.setTitle(s.toString());
            }
        });

        dueDate = (Button) v.findViewById(R.id.assignment_due_date);
        dateFormat = new SimpleDateFormat("m/d/yyyy");
        dueDate.setText(dateFormat.format(assignment.getDueDate()));
        dueDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                datePicker.show();
            }
        });

        completed = (CheckBox) v.findViewById(R.id.assignment_completed);
        completed.setChecked(assignment.isCompleted());
        completed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                assignment.setCompleted(isChecked);
            }
        });

        details = (EditText) v.findViewById(R.id.assignment_details);
        details.setText(assignment.getDetails());
        details.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                assignment.setDetails(s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //
            }

            @Override
            public void afterTextChanged(Editable s) {
                assignment.setDetails(s.toString());
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
                date = c.getTime();
                updateDate();
            }
        }, year, month, day);
    }

    private void updateDate() {
        if (date != null) {
            assignment.setDueDate(date);
            dueDate.setText(dateFormat.format(date));
        }
    }

}
