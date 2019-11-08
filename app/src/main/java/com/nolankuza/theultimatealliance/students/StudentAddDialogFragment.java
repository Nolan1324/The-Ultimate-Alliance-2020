package com.nolankuza.theultimatealliance.students;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.nolankuza.theultimatealliance.R;
import com.nolankuza.theultimatealliance.model.Student;

public class StudentAddDialogFragment extends DialogFragment {
    public interface Listener {
        void onAddAccepted(DialogFragment dialog, Student student);
    }

    // Use this instance of the interface to deliver action events
    StudentAddDialogFragment.Listener listener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (StudentAddDialogFragment.Listener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement StudentAddDialogFragment.Listener");
        }
    }

    @SuppressLint("InflateParams")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.dialog_add_student, null));
        builder.setMessage("New Student")
                .setPositiveButton("Add", null)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        StudentAddDialogFragment.this.getDialog().cancel();
                    }
                });
        final Dialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String firstName = ((EditText)dialog.findViewById(R.id.first_name_input)).getText().toString();
                        final String lastName = ((EditText)dialog.findViewById(R.id.last_name_input)).getText().toString();
                        final String grade = ((EditText)dialog.findViewById(R.id.grade_input)).getText().toString();
                        if(!firstName.equals("") && !lastName.equals("") && !grade.equals("")) {
                            dialog.dismiss();
                            listener.onAddAccepted(StudentAddDialogFragment.this, new Student(firstName, lastName, Integer.parseInt(grade)));
                        }
                    }
                });
            }
        });

        return dialog;
    }
}
