package com.nolankuza.theultimatealliance.main.master;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nolankuza.theultimatealliance.R;
import com.nolankuza.theultimatealliance.datasync.DataSyncActivity;
import com.nolankuza.theultimatealliance.settings.SettingsActivity;
import com.nolankuza.theultimatealliance.students.StudentsActivity;
import com.nolankuza.theultimatealliance.util.Prefs;

import java.util.concurrent.ExecutionException;

import static com.nolankuza.theultimatealliance.ApplicationState.database;

public class MasterSetupFragment extends Fragment {

    MasterListener listener;

    public MasterSetupFragment() {

    }

    public static MasterSetupFragment newInstance() {
        MasterSetupFragment fragment = new MasterSetupFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_master_setup, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpActions(view);
    }

    private void setUpActions(View view) {
        String teamNumber = Prefs.getTeam(null);
        ActionTextView actionTeamNumber = view.findViewById(R.id.action_team_number);
        ActionImageView actionStudents = view.findViewById(R.id.action_students);
        ActionImageView actionPassword = view.findViewById(R.id.action_password);
        ActionImageView actionSyncPassword = view.findViewById(R.id.action_sync_password);

        //region Team number
        (getCard(actionTeamNumber)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
            }
        });
        if(teamNumber == null) {
            actionTeamNumber.setText("None");
            actionTeamNumber.setWarn(true);
        } else {
            actionTeamNumber.setText("#" + teamNumber);
            actionTeamNumber.setDone(true);
        }
        //endregion

        //region Students
        (getCard(actionStudents)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), StudentsActivity.class));
            }
        });
        int studentCount = 0;
        try {
            studentCount = new StudentCountTask().execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        if(studentCount == 0) {
            actionStudents.setWait(true);
        } else {
            actionStudents.setDone(true);
        }
        //endregion

        //region Password
        (getCard(actionPassword)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
            }
        });
        boolean isPassword = false;
        try {
            isPassword = new PasswordTask().execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        if(!isPassword) {
            actionPassword.setImageDrawable(getResources().getDrawable(R.drawable.ic_lock_open));
            actionPassword.setWarn(true);
            return;
        } else {
            actionPassword.setImageDrawable(getResources().getDrawable(R.drawable.ic_lock_closed));
            actionPassword.setDone(true);
        }
        //endregion

        //region Sync password
        getCard(actionSyncPassword).setVisibility(View.VISIBLE);
        (getCard(actionSyncPassword)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), DataSyncActivity.class));
            }
        });
        if(!Prefs.getSyncedSettings(false)) {
            actionSyncPassword.setImageDrawable(getResources().getDrawable(R.drawable.ic_lock_open));
            actionSyncPassword.setWarn(true);
        } else {
            actionSyncPassword.setImageDrawable(getResources().getDrawable(R.drawable.ic_lock_closed));
            actionSyncPassword.setDone(true);
            if(teamNumber != null) {
                listener.setupDone();
            }
        }
        //endregion
    }

    private ViewGroup getCard(View view) {
        return (ViewGroup) view.getParent().getParent();
    }

    private static class StudentCountTask extends AsyncTask<Void, Void, Integer> {
        @Override
        protected Integer doInBackground(Void... voids) {
            return database.studentDao().getStudentCount();
        }
    }

    private static class PasswordTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            return database.settingsDao().get().passwordHash != null;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getParentFragment() instanceof MasterListener) {
            listener = (MasterListener) getParentFragment();
        } else {
            throw new RuntimeException(getParentFragment().toString()
                    + " must implement MasterListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}