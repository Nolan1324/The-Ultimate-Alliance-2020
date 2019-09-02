package com.nolankuza.theultimatealliance.main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.nolankuza.theultimatealliance.R;
import com.nolankuza.theultimatealliance.playoffs.PlayoffActivity;
import com.nolankuza.theultimatealliance.structure.PlayoffData;

import java.util.List;

import static com.nolankuza.theultimatealliance.ApplicationState.database;
import static com.nolankuza.theultimatealliance.ApplicationState.prefs;

public class PlayoffFragment extends SlaveFragment {
    PlayoffAdapter playoffAdapter;

    public PlayoffFragment() {
        setLayout(R.layout.fragment_slave_playoff);
    }

    public static PlayoffFragment newInstance() {
        return new PlayoffFragment();
    }

    @Override
    public void loadData(final Context context, final View view) {
        final RecyclerView matchImportRecycler = view.findViewById(R.id.match_import_recycler);
        new PlayoffQueryTask(showAll.isChecked(), new PlayoffQueryTask.Listener() {
            @Override
            public void onTaskCompleted(PlayoffDataAll playoffDataAll) {
                //progressBar.setVisibility(View.GONE);
                matchImportRecycler.setLayoutManager(new LinearLayoutManager(context));
                matchImportRecycler.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
                playoffAdapter = new PlayoffAdapter(context.getApplicationContext(), playoffDataAll.playoffDataList, playoffDataAll.nextIndex, showAll.isChecked());
                playoffAdapter.setClickListener(new PlayoffAdapter.ItemClickListener() {
                    @Override
                    public void onItemClick(View view, final PlayoffData playoffData) {
                        if(playoffData.scouted == 0) {
                            Log.d("HELLO", "CLICK");
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle("Team # of " + getResources().getStringArray(R.array.driverStations)[Integer.parseInt(prefs.getString("driver_pref", "0"))]);
                            final EditText input = new EditText(getActivity());
                            input.setInputType(InputType.TYPE_CLASS_NUMBER);
                            builder.setView(input);
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    playoffData.teamNumber = Integer.parseInt(input.getText().toString());
                                    Intent intent = new Intent(context.getApplicationContext(), PlayoffActivity.class);
                                    intent.putExtra("playoffdata", playoffData);
                                    startActivity(intent);
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                            builder.show();
                        } else {
                            Intent intent = new Intent(context.getApplicationContext(), PlayoffActivity.class);
                            intent.putExtra("playoffdata", playoffData);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onRemoveClick(View view, final PlayoffData playoffData) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                        dialog.setTitle("WARNING");
                        dialog.setMessage("This match will be removed. Only choose OK if this match was already played and/or canceled.");
                        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                new Thread() {
                                    @Override
                                    public void run() {
                                        playoffData.scouted = -1;
                                        database.playoffDataDao().insert(playoffData);
                                        updateData();
                                    }
                                }.start();
                            }
                        });
                        dialog.setNegativeButton("CANCEL", null);
                        dialog.show();
                    }

                    @Override
                    public void onUnscoutClick(View view, final PlayoffData playoffData) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                        dialog.setTitle("WARNING");
                        dialog.setMessage("This will remove ALL data scouted in this match. Only choose OK if this match is being replayed.");
                        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                new Thread() {
                                    @Override
                                    public void run() {
                                        playoffData.scouted = 0;
                                        database.playoffDataDao().insert(playoffData);
                                        updateData();
                                    }
                                }.start();
                            }
                        });
                        dialog.setNegativeButton("CANCEL", null);
                        dialog.show();
                    }
                });
                matchImportRecycler.setAdapter(playoffAdapter);
            }
        }).execute();
    }

    @Override
    public void updateData() {
        new PlayoffQueryTask(showAll.isChecked(), new PlayoffQueryTask.Listener() {
            @Override
            public void onTaskCompleted(PlayoffDataAll playoffDataAll) {
                if(playoffAdapter != null) {
                    playoffAdapter.showAll = showAll.isChecked();
                    playoffAdapter.setData(playoffDataAll.playoffDataList, playoffDataAll.nextIndex);
                }
            }
        }).execute();
    }

    public static class PlayoffQueryTask extends AsyncTask<Void, Void, PlayoffDataAll> {
        private boolean getAll;
        private Listener listener;

        PlayoffQueryTask(boolean getAll, Listener listener) {
            this.listener = listener;
            this.getAll = getAll;
        }

        public interface Listener {
            void onTaskCompleted(PlayoffDataAll playoffDataAll);
        }

        @Override
        public PlayoffDataAll doInBackground(Void... voids) {
            List<PlayoffData> playoffDataList;
            List<PlayoffData> playoffDataListAll = database.playoffDataDao().getAll();
            if(getAll) {
                playoffDataList = playoffDataListAll;
            } else {
                playoffDataList = database.playoffDataDao().getUnscouted();
            }
            if(playoffDataListAll.size() == 0) {
                return new PlayoffDataAll(playoffDataList, 1);
            } else {
                return new PlayoffDataAll(playoffDataList, playoffDataListAll.get(playoffDataListAll.size() - 1).index + 1);
            }
        }

        @Override
        public void onPostExecute(PlayoffDataAll playoffDataAll) {
            listener.onTaskCompleted(playoffDataAll);
        }
    }

    static class PlayoffDataAll {
        List<PlayoffData> playoffDataList;
        int nextIndex;

        PlayoffDataAll(List<PlayoffData> playoffDataList, int nextIndex) {
            this.playoffDataList = playoffDataList;
            this.nextIndex = nextIndex;
        }
    }
}
