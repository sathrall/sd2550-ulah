package com.heatedleek.android.ulah;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.UUID;


public class MainActivity extends AppCompatActivity
        implements CourseListFragment.OnCourseSelectedListener,
                   HomeFragment.OnCourseSelectedListener,
                   AddChoiceDialogFragment.OnAddTypeChosenListener,
                   AddCourseDialogFragment.OnAddCourseEndListener,
                   AddAssignmentDialogFragment.OnAddAssignmentEndListener,
                   ViewCourseFragment.OnAssignmentSelectedListener,
                   DrawerFragment.DrawerFragmentListener {

    private Toolbar toolbar;
    private DrawerFragment drawerFragment;
    private ArrayList<Course> courses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /*
         *  Set the Toolbar
         */

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(this.getTitle());


        /*
         *  Set the Nav Drawer
         */

        drawerFragment = (DrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_nav_drawer);
        drawerFragment.setUp(R.id.fragment_nav_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
        drawerFragment.setDrawerListener(this);

        // display the first view in the menu
        displayView(0);
    }


    /**
     *  onCourseSelected handler
     */

    public void onCourseSelected(Course course) {
        UUID courseId = course.getId();

        // create a new instance and pass in the course's id
        ViewCourseFragment viewCourseFragment = ViewCourseFragment.newInstance(courseId);

        // initialize a transaction
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // replace the current fragment in the fragment_container
        transaction.replace(R.id.container_body, viewCourseFragment);
        transaction.addToBackStack(null);

        // finally, commit the transaction
        transaction.commit();

        // Set the toolbar title
        getSupportActionBar().setTitle(course.getCourseNumber());
    } // eof onCourseSelected


    /**
     *  onAssignmentSelected handler
     */

    public void onAssignmentSelected(Assignment assignment) {
        UUID assignmentId = assignment.getId();

        // create a new instance and pass in the course's id
        ViewAssignmentFragment viewAssignmentFragment = ViewAssignmentFragment.newInstance(assignmentId);

        // initialize a transaction
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // replace the current fragment in the fragment_container
        transaction.replace(R.id.container_body, viewAssignmentFragment);
        transaction.addToBackStack(null);

        // finally, commit the transaction
        transaction.commit();
    }


    /**
     *  onAddTypeChosen handler
     */

    public void onAddTypeChosen(String choice) {
        // set the options to strings to check against
        String course = (String) getResources().getText(R.string.add_course_option);
        String assignment = (String) getResources().getText(R.string.add_assignment_option);

        Fragment fragment = null;
        String tag = null;
        String title = getString(R.string.app_name);

        courses = CourseLab.get(this).getCourses();
        boolean hasCourses = false;
        if (courses.size() > 0) {
            for (Course c : courses) {
                if (c != null) {
                    hasCourses = true;
                }
            }
        }

        if (choice.equals(course)) {
            fragment = new AddCourseDialogFragment();
            tag ="AddCourseDialogFragment";
            title = "Add Course";
        } else if (choice.equals(assignment)) {
            if (hasCourses) {
                fragment = new AddAssignmentDialogFragment();
                tag ="AddAssignmentDialogFragment";
                title = "Add Assignment";
            } else {
                fragment = new AddCourseDialogFragment();
                tag ="AddCourseDialogFragment";
                title = "Add Course";
                Toast.makeText(this, "You must first add a course.", Toast.LENGTH_LONG).show();
            }
        }

        if (fragment != null) {
            // create a new fragment manager
            FragmentManager fm = getSupportFragmentManager();

            // initialize a transaction
            FragmentTransaction transaction = fm.beginTransaction();

            // replace the current fragment in the fragment_container
            transaction.replace(R.id.container_body, fragment);
            transaction.addToBackStack(tag);

            // finally, commit the transaction
            transaction.commit();

            // Set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }


    /**
     *  onAdd End handlers
     */

    public void onAddCourseEnd() {
        hideKeyboard();

        Fragment fragment = getSupportFragmentManager()
                .findFragmentByTag("AddCourseDialogFragment");

        if(fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .remove(fragment).commit();
        }

        courseListReturn();
    }

    public void onAddAssignmentEnd() {
        hideKeyboard();

        Fragment fragment = getSupportFragmentManager()
                .findFragmentByTag("AddAssignmentDialogFragment");

        if(fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .remove(fragment).commit();
        }

        courseListReturn();
    }


    /**
     *  Show the add dialogs
     */

    public void showAddChoiceDialog() {
        AddChoiceDialogFragment dialog = new AddChoiceDialogFragment();
        dialog.show(getSupportFragmentManager(), "AddChoiceDialogFragment");
    }


    /**
     *  Hide the keyboard
     */

    public void hideKeyboard() {
        View v = this.getCurrentFocus();
        if (v != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }


    /**
     *  Return to the course list
     */

    public void courseListReturn() {
        FragmentManager fm = getSupportFragmentManager();
        boolean fragment = fm.popBackStackImmediate("CourseListFragment", 0);

        if (! fragment) {
            // if not found, create a new list
            CourseListFragment courseListFragment = new CourseListFragment();

            // initialize a transaction
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            // replace the current fragment in the fragment_container
            transaction.replace(R.id.container_body, courseListFragment);
            transaction.addToBackStack(null);

            // finally, commit the transaction
            transaction.commit();
        }
    }



    /**
     *  Options Menu
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_search) {
            return true;
        }

        if (id == R.id.action_add) {
            showAddChoiceDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }


    /**
     *  displayView Handler
     */

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);

        switch (position) {
            case 0:
                fragment = new HomeFragment();
                title = getString(R.string.title_home);
                break;
            case 1:
                fragment = new CourseListFragment();
                title = getString(R.string.title_courses);
                break;
            case 2:
                fragment = new SettingsFragment();
                title = getString(R.string.title_settings);
                break;
        }

        if (fragment != null) {
            // create a new fragment manager
            FragmentManager fm = getSupportFragmentManager();

            // initialize a transaction
            FragmentTransaction transaction = fm.beginTransaction();

            // replace the current fragment in the fragment_container
            transaction.replace(R.id.container_body, fragment);
            transaction.addToBackStack(null);

            // finally, commit the transaction
            transaction.commit();

            // Set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }

}
