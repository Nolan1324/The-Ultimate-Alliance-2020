package com.nolankuza.theultimatealliance.eventimport.matchimport;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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
import com.nolankuza.theultimatealliance.structure.Match;

import java.util.ArrayList;
import java.util.List;

public class MatchImportFragment extends Fragment {

    private Event event;

    List<Match> matches;

    public MatchImportFragment() {

    }

    public static MatchImportFragment newInstance(Event event, ArrayList<Match> matches) {
        MatchImportFragment fragment = new MatchImportFragment();
        Bundle args = new Bundle();
        args.putParcelable("event", event);
        args.putParcelableArrayList("matches", matches);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (ViewGroup) inflater.inflate(R.layout.fragment_match_import, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            event = getArguments().getParcelable("event");
        }

        final ArrayList<Match> matches;
        if(getArguments() != null) {
            matches = getArguments().getParcelableArrayList("matches");
        } else {
            matches = null;
        }

        final BaseActivity baseActivity = (BaseActivity) getActivity();
        if(matches == null) {
            if (baseActivity != null) baseActivity.showProgressBar();
            new MatchImportTask(event.key, new MatchImportTask.Listener() {
                @Override
                public void onTaskCompleted(List<Match> matches) {
                    if (baseActivity != null) baseActivity.hideProgressBar();
                    setupRecycler(matches, view);
                }
            }).execute();
        } else {
            setupRecycler(matches, view);
        }
    }

    public List<Match> getMatches() {
        return matches;
    }

    private void setupRecycler(List<Match> matches, View view) {
        RecyclerView matchImportRecycler = view.findViewById(R.id.match_import_recycler);
        matchImportRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        matchImportRecycler.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        final MatchImportAdapter matchImportAdapter = new MatchImportAdapter(getActivity(), matches, true);
        matchImportRecycler.setAdapter(matchImportAdapter);
        MatchImportFragment.this.matches = matches;
        if (matches.size() == 0) {
            ViewPager pager = getActivity().findViewById(R.id.pager);
            pager.setCurrentItem(1);
        }
    }
}