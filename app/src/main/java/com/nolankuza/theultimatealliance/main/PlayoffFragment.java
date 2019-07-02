package com.nolankuza.theultimatealliance.main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import com.nolankuza.theultimatealliance.R;
import com.nolankuza.theultimatealliance.playoffs.PlayoffActivity;
import com.nolankuza.theultimatealliance.scout.ScoutBasicActivity;
import com.nolankuza.theultimatealliance.structure.GameData;
import com.nolankuza.theultimatealliance.structure.Match;
import com.nolankuza.theultimatealliance.structure.PlayoffData;
import com.nolankuza.theultimatealliance.tasks.MatchQueryTask;

import java.util.ArrayList;
import java.util.List;

import static com.nolankuza.theultimatealliance.ApplicationState.database;
import static com.nolankuza.theultimatealliance.ApplicationState.locked;
import static com.nolankuza.theultimatealliance.ApplicationState.prefs;

public class PlayoffFragment extends Fragment {
    PlayoffAdapter playoffAdapter;
    Spinner studentSpinner;
    Switch showAll;

    public PlayoffFragment() {

    }

    public static PlayoffFragment newInstance() {
        PlayoffFragment fragment = new PlayoffFragment();
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
        return inflater.inflate(R.layout.fragment_playoff_slave, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateSettings(view, true, true);
    }

    public void updateSettings(final View view, boolean allowStudentsToChangeNameChanged, boolean showAllChanged) {
        final Context context = getActivity();

        showAll = view.findViewById(R.id.show_all);
        showAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updatePlayoffs();
            }
        });
        //showAll.setEnabled(!locked);

        if(allowStudentsToChangeNameChanged) {
            view.findViewById(R.id.student_spinner).setEnabled(prefs.getBoolean("allow_pref", false) || !locked);
            new StudentNameTask(new StudentNameTask.Listener() {
                @Override
                public void onTaskCompleted(List<String> students) {
                    ArrayAdapter adapter = new ArrayAdapter<>(context, R.layout.spinner_item, students);
                    studentSpinner = view.findViewById(R.id.student_spinner);
                    studentSpinner.setAdapter(adapter);
                    int studentIndex = students.indexOf(prefs.getString("student_pref", "Anonymous"));
                    if (studentIndex != -1) {
                        studentSpinner.setSelection(studentIndex);
                    }
                    studentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            prefs.edit().putString("student_pref", studentSpinner.getSelectedItem().toString()).apply();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                }
            }).execute();
        }
        if(showAllChanged) {
            showAll.setChecked(prefs.getBoolean("show_all_pref", false));

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
                                        Intent intent = new Intent(getActivity().getApplicationContext(), PlayoffActivity.class);
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
                                Intent intent = new Intent(getActivity().getApplicationContext(), PlayoffActivity.class);
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
                                            updatePlayoffs();
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
                                            updatePlayoffs();
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
    }

    public void updatePlayoffs() {
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

        public PlayoffQueryTask(boolean getAll, Listener listener) {
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

    public static class PlayoffDataAll {
        public List<PlayoffData> playoffDataList;
        public int nextIndex;

        public PlayoffDataAll(List<PlayoffData> playoffDataList, int nextIndex) {
            this.playoffDataList = playoffDataList;
            this.nextIndex = nextIndex;
        }
    }
}
