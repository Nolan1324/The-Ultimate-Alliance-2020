package com.nolankuza.theultimatealliance.assignment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.nolankuza.theultimatealliance.R;
import com.nolankuza.theultimatealliance.model.Assignment;
import com.nolankuza.theultimatealliance.Constants;

import java.lang.ref.WeakReference;
import java.util.List;

import static com.nolankuza.theultimatealliance.ApplicationState.database;
import static com.nolankuza.theultimatealliance.ApplicationState.prefs;

public class AssignmentAdapter extends RecyclerView.Adapter<AssignmentAdapter.ViewHolder> {

    private WeakReference<Context> context;
    private List<Assignment> assignments;
    private List<String> students;
    private LayoutInflater inflater;

    public AssignmentAdapter(Context context, List<Assignment> assignments, List<String> students) {
        this.inflater = LayoutInflater.from(context);
        this.context = new WeakReference<>(context);
        this.assignments = assignments;
        this.students = students;
    }

    @NonNull
    @Override
    public AssignmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_assignment, parent, false);
        return new AssignmentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AssignmentAdapter.ViewHolder holder, int position) {
        Assignment assignment = assignments.get(position);
        View itemView = holder.itemView;
        ((TextView)(itemView.findViewById(R.id.item_assignment_name))).setText(assignment.name);
        ((Spinner)(itemView.findViewById(R.id.item_assignment_role))).setSelection(assignment.role);
        ArrayAdapter adapter = new ArrayAdapter<>(context.get(), R.layout.spinner_item, students);
        ((Spinner)(itemView.findViewById(R.id.item_assignment_student))).setAdapter(adapter);
        int studentIndex = students.indexOf(assignment.student);
        if(studentIndex != -1) {
            ((Spinner)(itemView.findViewById(R.id.item_assignment_student))).setSelection(studentIndex);
        }
        ((CheckBox) (itemView.findViewById(R.id.item_assignment_playoffs))).setChecked(assignment.playoffs);
    }

    @Override
    public int getItemCount() {
        return assignments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements Spinner.OnItemSelectedListener, CheckBox.OnCheckedChangeListener {
        View itemView;
        int count;

        ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ((Spinner)(itemView.findViewById(R.id.item_assignment_role))).setOnItemSelectedListener(this);
            ((Spinner)(itemView.findViewById(R.id.item_assignment_student))).setOnItemSelectedListener(this);
            ((CheckBox)(itemView.findViewById(R.id.item_assignment_playoffs))).setOnCheckedChangeListener(this);
        }

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            if(count >= 2) {
                update();
            } else {
                count++;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            update();
        }

        private void update() {
            final Assignment assignment = new Assignment();
            assignment.name = ((TextView) (itemView.findViewById(R.id.item_assignment_name))).getText().toString();
            assignment.role = (byte) ((Spinner) (itemView.findViewById(R.id.item_assignment_role))).getSelectedItemPosition();
            try {
                assignment.student = ((Spinner) (itemView.findViewById(R.id.item_assignment_student))).getSelectedItem().toString();
            } catch (NullPointerException e) {
                assignment.student = "";
            }
            assignment.playoffs = ((CheckBox) (itemView.findViewById(R.id.item_assignment_playoffs))).isChecked();
            if (assignment.role == 6) {
                prefs.edit().putBoolean(Constants.PREF_PIT_ASSIGN_CHANGED, true).apply();
            } else {
                prefs.edit().putBoolean(Constants.PREF_SCOUT_ASSIGN_CHANGED, true).apply();
            }
            new Thread() {
                @Override
                public void run() {
                    database.assignmentDao().insert(assignment);
                }
            }.start();
        }
    }

    public List<Assignment> getAssignments() {
        return assignments;
    }

    public Assignment getItem(int id) {
        return assignments.get(id);
    }
}
