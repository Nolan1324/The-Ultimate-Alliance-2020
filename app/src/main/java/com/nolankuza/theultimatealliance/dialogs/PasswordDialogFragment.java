package com.nolankuza.theultimatealliance.dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

import com.nolankuza.theultimatealliance.R;
import com.nolankuza.theultimatealliance.util.Sha256;

import java.util.Arrays;

public class PasswordDialogFragment extends DialogFragment {

    public interface Listener {
         void onPasswordCorrect(DialogFragment dialog);
    }

    Listener listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (Listener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement PasswordDialogFragment.Listener");
        }
    }

    @SuppressLint("InflateParams")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final byte[] hash = getArguments().getByteArray("hash");
        final byte[] salt = getArguments().getByteArray("salt");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.dialog_password, null));
        builder.setMessage("Please enter the password")
                .setPositiveButton("Submit", null)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        PasswordDialogFragment.this.getDialog().cancel();
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
                        final EditText password = dialog.findViewById(R.id.password);
                        if(password != null) {
                            if(Arrays.equals(Sha256.hash256(password.getText().toString(), salt), hash)) {
                                dialog.dismiss();
                                listener.onPasswordCorrect(PasswordDialogFragment.this);
                            } else {
                                if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                                    password.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                                }
                                Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
                                password.startAnimation(shake);
                            }
                        }
                    }
                });
            }
        });

        return dialog;
    }
}
