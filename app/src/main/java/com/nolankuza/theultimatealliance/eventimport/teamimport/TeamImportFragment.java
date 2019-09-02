package com.nolankuza.theultimatealliance.eventimport.teamimport;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.nolankuza.theultimatealliance.BaseActivity;
import com.nolankuza.theultimatealliance.R;
import com.nolankuza.theultimatealliance.structure.Event;
import com.nolankuza.theultimatealliance.structure.Team;

import java.util.ArrayList;
import java.util.List;

public class TeamImportFragment extends Fragment {

    private Event event;

    List<Team> teams;

    public TeamImportFragment() {

    }

    public static TeamImportFragment newInstance(Event event, ArrayList<Team> teams) {
        TeamImportFragment fragment = new TeamImportFragment();
        Bundle args = new Bundle();
        args.putParcelable("event", event);
        args.putParcelableArrayList("teams", teams);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (ViewGroup) inflater.inflate(R.layout.fragment_team_import, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState); //TODO Remove?
        //Originally in onCreate
        if (getArguments() != null) {
            event = getArguments().getParcelable("event");
        }

        final List<Team> teams;
        if(getArguments() != null) {
            teams = getArguments().getParcelableArrayList("teams");
        } else {
            teams = null;
        }

        final BaseActivity baseActivity = (BaseActivity) getActivity();
        if(teams == null) {
            if (baseActivity != null) baseActivity.showProgressBar();
            new TeamImportTask(event.key, new TeamImportTask.Listener() {
                @Override
                public void onTaskCompleted(List<Team> teams) {
                    if (baseActivity != null) baseActivity.hideProgressBar();
                    setupRecycler(teams, view);
                }
            }).execute();
        } else {
            setupRecycler(teams, view);
        }
    }

    public List<Team> getTeams() {
        return teams;
    }

    private void setupRecycler(List<Team> teams, View view) {
        RecyclerView matchImportRecycler = view.findViewById(R.id.team_import_recycler);
        matchImportRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        matchImportRecycler.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        final TeamImportAdapter matchImportAdapter = new TeamImportAdapter(getActivity(), teams);
        matchImportRecycler.setAdapter(matchImportAdapter);
        TeamImportFragment.this.teams = teams;
    }
}