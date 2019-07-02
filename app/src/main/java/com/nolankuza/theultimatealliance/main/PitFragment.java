package com.nolankuza.theultimatealliance.main;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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
import com.nolankuza.theultimatealliance.pit.PitActivity;
import com.nolankuza.theultimatealliance.structure.Match;
import com.nolankuza.theultimatealliance.structure.PitData;
import com.nolankuza.theultimatealliance.structure.Student;
import com.nolankuza.theultimatealliance.students.StudentQueryTask;
import com.nolankuza.theultimatealliance.tasks.MatchQueryTask;
import com.nolankuza.theultimatealliance.tasks.PitQueryTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.nolankuza.theultimatealliance.ApplicationState.database;
import static com.nolankuza.theultimatealliance.ApplicationState.locked;
import static com.nolankuza.theultimatealliance.ApplicationState.prefs;

public class PitFragment extends Fragment {
    PitAdapter pitAdapter;
    RecyclerView pitRecycler;
    Spinner studentSpinner;
    Switch showAll;

    public PitFragment() {

    }

    public static PitFragment newInstance() {
        PitFragment fragment = new PitFragment();
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
        return inflater.inflate(R.layout.fragment_pit_slave, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateSettings(view, true, true);
    }

    public void updateSettings(final View view, boolean allowStudentsToChangeNameChanged, boolean showAllChanged) {
        final Context context = getActivity();
        showAll = view.findViewById(R.id.show_all);
        //showAll.setEnabled(!locked);
        studentSpinner = view.findViewById(R.id.student_spinner);

        //TODO Combine with use in SlaveFragment
        if(allowStudentsToChangeNameChanged) {
            view.findViewById(R.id.student_spinner).setEnabled(prefs.getBoolean("allow_pref", false) || !locked);
            new StudentNameTask(new StudentNameTask.Listener() {
                @Override
                public void onTaskCompleted(List<String> students) {
                    ArrayAdapter adapter = new ArrayAdapter<>(context, R.layout.spinner_item, students);
                    studentSpinner.setAdapter(adapter);
                    int studentIndex = students.indexOf(prefs.getString("student_pref", "Anonymous"));
                    if (studentIndex != -1) {
                        studentSpinner.setSelection(studentIndex);
                    }
                    studentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            Object item = studentSpinner.getSelectedItem();
                            if(item != null) {
                                prefs.edit().putString("student_pref", item.toString()).apply();
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                }
            }).execute();
        }
        if(showAllChanged) {
            showAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    updatePits();
                }
            });
            showAll.setChecked(prefs.getBoolean("show_all_pref", false));
            pitRecycler = view.findViewById(R.id.pit_recycler);
            new PitQueryTask(false, new PitQueryTask.Listener() {
                @Override
                public void onTaskCompleted(List<PitData> pitDataList) {
                    pitRecycler.setLayoutManager(new LinearLayoutManager(context));
                    pitRecycler.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
                    pitAdapter = new PitAdapter(context.getApplicationContext(), pitDataList);
                    //TODO Finish pit main implementation
                    pitAdapter.setClickListener(new PitAdapter.ItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Intent intent = new Intent(getActivity().getApplicationContext(), PitActivity.class);
                            PitData pit = pitAdapter.getItem(position);
                            if (studentSpinner.getVisibility() == View.VISIBLE) {
                                pit.scouter = studentSpinner.getSelectedItem().toString();
                            }
                            intent.putExtra("pit", pit);
                            startActivity(intent);
                        }
                    });
                    pitRecycler.setAdapter(pitAdapter);
                }
            }).execute();
        }
    }

    public void updatePits() {
        new PitQueryTask(showAll.isChecked(), new PitQueryTask.Listener() {
            @Override
            public void onTaskCompleted(List<PitData> pitDataList) {
                if(pitAdapter != null) {
                    pitAdapter.setData(pitDataList);
                }
            }
        }).execute();
    }
}