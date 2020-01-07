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
import android.widget.TextView;

import com.nolankuza.theultimatealliance.R;
import com.nolankuza.theultimatealliance.assignment.AssignmentActivity;
import com.nolankuza.theultimatealliance.datasync.DataSyncActivity;
import com.nolankuza.theultimatealliance.eventimport.EventImportActivity;
import com.nolankuza.theultimatealliance.room.GameDataDao;
import com.nolankuza.theultimatealliance.util.Prefs;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import static com.nolankuza.theultimatealliance.ApplicationState.database;

public class MasterMatchFragment extends Fragment {
    public MasterMatchFragment() {

    }

    public static MasterMatchFragment newInstance() {
        MasterMatchFragment fragment = new MasterMatchFragment();
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
        return inflater.inflate(R.layout.fragment_main_master_match, container, false);
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
        ActionImageView actionMatchesImport = view.findViewById(R.id.action_matches_import);
        ActionImageView actionMatchAssign = view.findViewById(R.id.action_match_assign);
        ActionImageView actionSend = view.findViewById(R.id.action_sync_matches_and_assignments);
        ActionTextView actionSync = view.findViewById(R.id.action_sync_game);

        //region Event matches
        getCard(actionMatchesImport).setVisibility(View.VISIBLE);
        getCard(actionMatchesImport).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), EventImportActivity.class));
            }
        });
        int matchCount = 0;
        try {
            matchCount = new MatchCountTask().execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        if(matchCount == 0) {
            actionMatchesImport.setWarn(true);
            return;
        } else {
            actionMatchesImport.setDone(true);
        }
        //endregion

        //region Match assign
        getCard(actionMatchAssign).setVisibility(View.VISIBLE);
        getCard(actionMatchAssign).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AssignmentActivity.class));
            }
        });
        int matchAssignmentsCount = 0;
        try {
            matchAssignmentsCount = new MatchAssignmentsCountTask().execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        if(matchAssignmentsCount == 0) {
            actionMatchAssign.setWarn(true);
            return;
        } else {
            actionMatchAssign.setDone(true);
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
        int scoutCount = 0;
        try {
            scoutCount = new GameCountTask().execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        if(Prefs.getScoutAssignChanged(false)) {
            actionSend.setWarn(true);
            if(scoutCount == 0) return;
        } else {
            actionSend.setDone(true);
        }
        //endregion

        //region Sync
        getCard(actionSync).setVisibility(View.VISIBLE);
        getCard(actionSync).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DataSyncActivity.class);
                intent.putExtra("gamedata", true);
                startActivity(intent);
            }
        });
        actionSync.setText(String.format(Locale.US, "%d/%d", scoutCount, matchCount));
        if(scoutCount == 0) {
            actionSync.setWarn(true);
        } else if(scoutCount == matchCount) {
            actionSync.setDone(true);
        } else {
            actionSync.setWait(true);
        }
        try {
            List<Integer> scoutCounts = new ScoutCountTask().execute().get();
            ((TextView)view.findViewById(R.id.red1)).setText(scoutCounts.get(0).toString());
            ((TextView)view.findViewById(R.id.red2)).setText(scoutCounts.get(1).toString());
            ((TextView)view.findViewById(R.id.red3)).setText(scoutCounts.get(2).toString());
            ((TextView)view.findViewById(R.id.blue1)).setText(scoutCounts.get(3).toString());
            ((TextView)view.findViewById(R.id.blue2)).setText(scoutCounts.get(4).toString());
            ((TextView)view.findViewById(R.id.blue3)).setText(scoutCounts.get(5).toString());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        //endregion
    }

    private ViewGroup getCard(View view) {
        return (ViewGroup) view.getParent().getParent();
    }

    private static class MatchCountTask extends AsyncTask<Void, Void, Integer> {
        @Override
        protected Integer doInBackground(Void... voids) {
            return database.matchDao().getMatchCount();
        }
    }

    private static class MatchAssignmentsCountTask extends AsyncTask<Void, Void, Integer> {
        @Override
        protected Integer doInBackground(Void... voids) {
            return database.assignmentDao().getTotalMatchScouters();
        }
    }

    private static class GameCountTask extends AsyncTask<Void, Void, Integer> {
        @Override
        protected Integer doInBackground(Void... voids) {
            return (int) Math.floor(database.gameDataDao().getScoutCount() / 6f);
        }
    }

    private static class ScoutCountTask extends AsyncTask<Void, Void, List<Integer>> {
        @Override
        protected List<Integer> doInBackground(Void... voids) {
            GameDataDao gameDataDao = database.gameDataDao();
            List<Integer> out = new ArrayList<>();
            out.add(gameDataDao.getRed1Count());
            out.add(gameDataDao.getRed2Count());
            out.add(gameDataDao.getRed3Count());
            out.add(gameDataDao.getBlue1Count());
            out.add(gameDataDao.getBlue2Count());
            out.add(gameDataDao.getBlue3Count());
            return out;
        }
    }
}