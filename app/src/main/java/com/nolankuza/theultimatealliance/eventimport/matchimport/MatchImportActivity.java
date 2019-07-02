package com.nolankuza.theultimatealliance.eventimport.matchimport;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.nolankuza.theultimatealliance.BaseActivity;
import com.nolankuza.theultimatealliance.R;
import com.nolankuza.theultimatealliance.structure.Event;
import com.nolankuza.theultimatealliance.structure.Match;

import java.util.List;

public class MatchImportActivity extends BaseActivity {

    List<Match> matches;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setToolBarMenu(R.menu.toolbar_match_import);
        setContentView(R.layout.activity_match_import);
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        Event event = new Event();
        if(bundle != null) {
            Event tEvent = bundle.getParcelable("event");
            if(tEvent != null) {
                event = tEvent;
            }
        }

        //Action bar
        actionBar.setSubtitle("Matches for " + event.name);

        progressBar = findViewById(R.id.progressBar);
        new MatchImportTask(event.key, new MatchImportTask.Listener() {
            @Override
            public void onTaskInit() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTaskCompleted(List<Match> matches) {
                progressBar.setVisibility(View.GONE);
                RecyclerView matchImportRecycler = findViewById(R.id.match_import_recycler);
                matchImportRecycler.setLayoutManager(new LinearLayoutManager(MatchImportActivity.this));
                matchImportRecycler.addItemDecoration(new DividerItemDecoration(MatchImportActivity.this, DividerItemDecoration.VERTICAL));
                final MatchImportAdapter matchImportAdapter = new MatchImportAdapter(MatchImportActivity.this, matches, true);
                matchImportRecycler.setAdapter(matchImportAdapter);
                MatchImportActivity.this.matches = matches;
            }
        }).execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_button:
                if(matches != null) {
                    new MatchImportSaveTask(matches, new MatchImportSaveTask.Listener() {
                        @Override
                        public void onTaskInit() {
                            progressBar.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onTaskCompleted(Integer size) {
                            progressBar.setVisibility(View.GONE);
                            AlertDialog.Builder builder = new AlertDialog.Builder(MatchImportActivity.this);
                            builder.setTitle("Success");
                            builder.setMessage(size + " matches saved");
                            builder.create().show();
                        }
                    }).execute();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
