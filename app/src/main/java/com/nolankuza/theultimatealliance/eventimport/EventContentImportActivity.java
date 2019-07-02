package com.nolankuza.theultimatealliance.eventimport;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.nolankuza.theultimatealliance.BaseActivity;
import com.nolankuza.theultimatealliance.eventimport.teamimport.TeamImportSaveTask;
import com.nolankuza.theultimatealliance.main.MainActivity;
import com.nolankuza.theultimatealliance.R;
import com.nolankuza.theultimatealliance.eventimport.matchimport.MatchImportFragment;
import com.nolankuza.theultimatealliance.eventimport.teamimport.TeamImportFragment;
import com.nolankuza.theultimatealliance.structure.Event;
import com.nolankuza.theultimatealliance.structure.Match;
import com.nolankuza.theultimatealliance.structure.Team;

import java.util.ArrayList;
import java.util.List;

public class EventContentImportActivity extends BaseActivity {

    private ProgressBar progressBar;

    private Event event = new Event();

    private MatchImportFragment matchImportFragment;
    private TeamImportFragment teamImportFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setToolBarMenu(R.menu.toolbar_match_import);
        setContentView(R.layout.activity_event_content_import);
        super.onCreate(savedInstanceState);

        final ArrayList<Match> matches;
        final ArrayList<Team> teams;
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            Event tEvent = bundle.getParcelable("event");
            if(tEvent != null) {
                event = tEvent;
            }
            matches = bundle.getParcelableArrayList("matches");
            teams = bundle.getParcelableArrayList("teams");
        } else {
            matches = null;
            teams = null;
        }

        actionBar.setSubtitle(event.name);
        progressBar = findViewById(R.id.progressBar);

        ViewPager pager = findViewById(R.id.pager);
        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch(position) {
                    case 0:
                        matchImportFragment = MatchImportFragment.newInstance(event, matches);
                        return matchImportFragment;
                    case 1:
                        teamImportFragment = TeamImportFragment.newInstance(event, teams);
                        return teamImportFragment;
                    default:
                        matchImportFragment = MatchImportFragment.newInstance(event, matches);
                        return matchImportFragment;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch(position) {
                    case 0:
                        return "Matches";
                    case 1:
                        return "Teams";
                    default:
                        return "Matches";
                }
            }
        };
        pager.setAdapter(adapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(pager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_button:
                final List<Match> matches = matchImportFragment.getMatches();
                final List<Team> teams = teamImportFragment.getTeams();
                new EventImportSaveTask(event, matches, teams, new EventImportSaveTask.Listener() {
                    @Override
                    public void onTaskInit() {
                        progressBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onTaskCompleted(Integer status) {
                        progressBar.setVisibility(View.GONE);
                        AlertDialog.Builder builder = new AlertDialog.Builder(EventContentImportActivity.this);
                        builder.setTitle("Success");
                        builder.setMessage(matches.size() + " matches and " + teams.size() + " teams saved");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(EventContentImportActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        });
                        builder.setCancelable(false);
                        builder.create().show();
                    }
                }).execute();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
