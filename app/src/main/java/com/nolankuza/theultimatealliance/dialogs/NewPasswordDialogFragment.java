package com.nolankuza.theultimatealliance.dialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.nolankuza.theultimatealliance.R;
import com.nolankuza.theultimatealliance.settings.SaveSettingsThread;
import com.nolankuza.theultimatealliance.structure.Settings;
import com.nolankuza.theultimatealliance.tasks.SettingsQueryTask;
import com.nolankuza.theultimatealliance.util.Sha256;

public class NewPasswordDialogFragment extends DialogFragment {

    @SuppressLint("InflateParams")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.dialog_setpassword, null));
        builder.setMessage("Please enter a new password")
                .setPositiveButton("Submit", null)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        NewPasswordDialogFragment.this.getDialog().cancel();
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
                        final EditText password = dialog.findViewById(R.id.new_password);
                        if(password != null) {
                            if(!password.getText().toString().equals("")) {
                                new SettingsQueryTask(new SettingsQueryTask.Listener() {
                                    @Override
                                    public void onTaskCompleted(Settings settings) {
                                        settings.passwordSalt = Sha256.generateSalt(8);
                                        settings.passwordHash = Sha256.hash256(password.getText().toString(), settings.passwordSalt);
                                        new SaveSettingsThread(settings, new SaveSettingsThread.Listener() {
                                            @Override
                                            public void onTaskCompleted() {
                                                dialog.dismiss();
                                            }
                                        }).start();
                                    }
                                }).execute();
                            }
                        }
                    }
                });
            }
        });

        return dialog;
    }
}
