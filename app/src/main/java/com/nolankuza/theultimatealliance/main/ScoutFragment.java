package com.nolankuza.theultimatealliance.main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.nolankuza.theultimatealliance.R;
import com.nolankuza.theultimatealliance.scout.ScoutBasicActivity;
import com.nolankuza.theultimatealliance.model.gamedata.GameData;
import com.nolankuza.theultimatealliance.model.Match;
import com.nolankuza.theultimatealliance.tasks.MatchQueryTask;

import java.util.List;

import static com.nolankuza.theultimatealliance.ApplicationState.database;

public class ScoutFragment extends SlaveFragment {
    ScoutAdapter matchImportAdapter;

    public ScoutFragment() {
        setLayout(R.layout.fragment_slave_scout);
    }

    @Override
    public void loadData(final Context context, final View view) {
        final RecyclerView matchImportRecycler = view.findViewById(R.id.match_import_recycler);
        new MatchQueryTask(showAll.isChecked(), new MatchQueryTask.Listener() {
            @Override
            public void onTaskCompleted(List<Match> matches) {
                //progressBar.setVisibility(View.GONE);
                matchImportRecycler.setLayoutManager(new LinearLayoutManager(context));
                matchImportRecycler.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
                matchImportAdapter = new ScoutAdapter(context.getApplicationContext(), matches, showAll.isChecked());
                matchImportAdapter.setClickListener(new ScoutAdapter.ItemClickListener() {
                    @Override
                    public void onItemClick(View view, Match match) {
                        Intent intent = new Intent(context.getApplicationContext(), ScoutBasicActivity.class);
                        intent.putExtra("match", match);
                        startActivity(intent);
                    }

                    @Override
                    public void onRemoveClick(View view, final Match match) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                        dialog.setTitle("WARNING");
                        dialog.setMessage("This match will be removed. Only choose OK if this match was already played and/or canceled.");
                        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                new Thread() {
                                    @Override
                                    public void run() {
                                        match.scouted = true;
                                        database.matchDao().insert(match);
                                        GameData gameData = GameData.fromMatch(match);
                                        gameData.scouted = -1;
                                        database.gameDataDao().insert(gameData);
                                        updateData();
                                    }
                                }.start();
                            }
                        });
                        dialog.setNegativeButton("CANCEL", null);
                        dialog.show();
                    }

                    @Override
                    public void onUnscoutClick(View view, final Match match) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                        dialog.setTitle("WARNING");
                        dialog.setMessage("This will remove ALL data scouted in this match. Only choose OK if this match is being replayed.");
                        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                new Thread() {
                                    @Override
                                    public void run() {
                                        match.scouted = false;
                                        database.matchDao().insert(match);
                                        database.gameDataDao().delete(match.key);
                                        updateData();
                                    }
                                }.start();
                            }
                        });
                        dialog.setNegativeButton("CANCEL", null);
                        dialog.show();
                    }
                });
                matchImportRecycler.setAdapter(matchImportAdapter);
            }
        }).execute();
    }

    @Override
    public void updateData() {
        new MatchQueryTask(showAll.isChecked(), new MatchQueryTask.Listener() {
            @Override
            public void onTaskCompleted(List<Match> matches) {
                if(matchImportAdapter != null) {
                    matchImportAdapter.showAll = showAll.isChecked();
                    matchImportAdapter.setData(matches);
                }
            }
        }).execute();
    }
}
