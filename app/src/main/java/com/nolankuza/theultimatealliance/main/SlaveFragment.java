package com.nolankuza.theultimatealliance.main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;

import com.nolankuza.theultimatealliance.R;
import com.nolankuza.theultimatealliance.scout.ScoutBasicActivity;
import com.nolankuza.theultimatealliance.structure.GameData;
import com.nolankuza.theultimatealliance.structure.Match;
import com.nolankuza.theultimatealliance.tasks.MatchQueryTask;
import com.nolankuza.theultimatealliance.util.Constants;

import java.util.List;

import static com.nolankuza.theultimatealliance.ApplicationState.database;
import static com.nolankuza.theultimatealliance.ApplicationState.locked;
import static com.nolankuza.theultimatealliance.ApplicationState.prefs;

public class SlaveFragment extends Fragment {
    SlaveAdapter matchImportAdapter;
    Spinner studentSpinner;
    Switch showAll;

    public SlaveFragment() {

    }

    public static SlaveFragment newInstance() {
        SlaveFragment fragment = new SlaveFragment();
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
        return inflater.inflate(R.layout.fragment_main_slave, container, false);
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
                updateMatches();
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
            new MatchQueryTask(showAll.isChecked(), new MatchQueryTask.Listener() {
                @Override
                public void onTaskInit() {
                    //progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onTaskCompleted(List<Match> matches) {
                    //progressBar.setVisibility(View.GONE);
                    matchImportRecycler.setLayoutManager(new LinearLayoutManager(context));
                    matchImportRecycler.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
                    matchImportAdapter = new SlaveAdapter(context.getApplicationContext(), matches, showAll.isChecked());
                    matchImportAdapter.setClickListener(new SlaveAdapter.ItemClickListener() {
                        @Override
                        public void onItemClick(View view, Match match) {
                            Intent intent = new Intent(getActivity().getApplicationContext(), ScoutBasicActivity.class);
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
                                            updateMatches();
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
                                            updateMatches();
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
    }

    public void updateMatches() {
        new MatchQueryTask(showAll.isChecked(), new MatchQueryTask.Listener() {
            @Override
            public void onTaskInit() {

            }

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
