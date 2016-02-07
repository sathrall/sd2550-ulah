package com.heatedleek.android.ulah;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

/**
 * Created by fexofenadine180mg on 8/22/15.
 */
public class AddChoiceDialogFragment extends DialogFragment {

    public interface OnAddTypeChosenListener {
        public void onAddTypeChosen(String choice);
    }

    OnAddTypeChosenListener listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            listener = (OnAddTypeChosenListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement AddChoiceListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final String[] choices = getResources().getStringArray(R.array.add_dialog_choices);

        builder.setItems(choices, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // grab the selected item
                String choice = choices[which];
                listener.onAddTypeChosen(choice);
            }
        });

        return builder.create();
    }

}
