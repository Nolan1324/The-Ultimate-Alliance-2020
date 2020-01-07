package com.nolankuza.theultimatealliance.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;

import com.nolankuza.theultimatealliance.R;
import com.nolankuza.theultimatealliance.util.Prefs;

import java.util.List;

import static com.nolankuza.theultimatealliance.ApplicationState.locked;
import static com.nolankuza.theultimatealliance.ApplicationState.prefs;

public abstract class SlaveFragment extends Fragment {
    protected Spinner studentSpinner;
    private Switch showAll;

    private int layout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(layout, container, false);
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
                updateData(); //Call abstract method
            }
        });
        //showAll.setEnabled(!locked);

        if(allowStudentsToChangeNameChanged && context != null) {
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
                            Prefs.setStudent(studentSpinner.getSelectedItem().toString());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                }
            }).execute();
        }
        if(showAllChanged && context != null) {
            showAll.setChecked(Prefs.getShowAll(false));
            loadData(context, view); //Call abstract method
        }
    }

    protected boolean isShowingAll() {
        return showAll.isChecked();
    }

    protected void setLayout(int layout) {
        this.layout = layout;
    }

    public abstract void loadData(final Context context, final View view);

    public abstract void updateData();
}
