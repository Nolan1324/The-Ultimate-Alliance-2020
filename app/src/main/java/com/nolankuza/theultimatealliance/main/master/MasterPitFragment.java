package com.nolankuza.theultimatealliance.main.master;

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
import com.nolankuza.theultimatealliance.assignment.AssignmentActivity;
import com.nolankuza.theultimatealliance.datasync.DataSyncActivity;
import com.nolankuza.theultimatealliance.eventimport.EventImportActivity;
import com.nolankuza.theultimatealliance.util.Prefs;

import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import static com.nolankuza.theultimatealliance.ApplicationState.database;

public class MasterPitFragment extends Fragment {

    public MasterPitFragment() {

    }

    public static MasterPitFragment newInstance() {
        MasterPitFragment fragment = new MasterPitFragment();
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
        return inflater.inflate(R.layout.fragment_main_master_pit, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpActions(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpActions(Objects.requireNonNull(getView()));
    }

    private void setUpActions(View view) {
        ActionImageView actionEventImport = view.findViewById(R.id.action_event_import);
        ActionImageView actionPitAssign = view.findViewById(R.id.action_pit_assign);
        ActionImageView actionSend = view.findViewById(R.id.action_sync_teams_and_assignments);
        ActionTextView actionSync = view.findViewById(R.id.action_sync);

        //region Event teams
        getCard(actionEventImport).setVisibility(View.VISIBLE);
        getCard(actionEventImport).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), EventImportActivity.class));
            }
        });
        int teamCount = 0;
        try {
            teamCount = new TeamCountTask().execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        if(teamCount == 0) {
            actionEventImport.setWarn(true);
            return;
        } else {
            actionEventImport.setDone(true);
        }
        //endregion

        //region Pit assign
        getCard(actionPitAssign).setVisibility(View.VISIBLE);
        getCard(actionPitAssign).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AssignmentActivity.class));
            }
        });
        int pitAssignmentsCount = 0;
        try {
            pitAssignmentsCount = new PitAssignmentsCountTask().execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        if(pitAssignmentsCount == 0) {
            actionPitAssign.setWarn(true);
            return;
        } else {
            actionPitAssign.setDone(true);
        }
        //endregion

        //region Send
        getCard(actionSend).setVisibility(View.VISIBLE);
        getCard(actionSend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), DataSyncActivity.class));
            }
        });
        int pitCount = 0;
        try {
            pitCount = new PitCountTask().execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        if(Prefs.getPitAssignChanged(false)) {
            actionSend.setWarn(true);
            if(pitCount == 0) return;
        } else {
            actionSend.setDone(true);
        }
        //endregion

        //region Sync
        getCard(actionSync).setVisibility(View.VISIBLE);
        getCard(actionSync).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), DataSyncActivity.class));
            }
        });
        actionSync.setText(String.format(Locale.US, "%d/%d", pitCount, teamCount));
        if(pitCount == 0) {
            actionSync.setWarn(true);
        } else if(pitCount == teamCount) {
            actionSync.setDone(true);
        } else {
            actionSync.setWait(true);
        }
        //endregion
    }

    private ViewGroup getCard(View view) {
        return (ViewGroup) view.getParent().getParent();
    }

    private static class TeamCountTask extends AsyncTask<Void, Void, Integer> {
        @Override
        protected Integer doInBackground(Void... voids) {
            return database.teamDao().getTeamCount();
        }
    }

    private static class PitAssignmentsCountTask extends AsyncTask<Void, Void, Integer> {
        @Override
        protected Integer doInBackground(Void... voids) {
            return database.assignmentDao().getTotalPitScouters();
        }
    }

    private static class PitCountTask extends AsyncTask<Void, Void, Integer> {
        @Override
        protected Integer doInBackground(Void... voids) {
            return database.pitDataDao().getPitCount();
        }
    }
}