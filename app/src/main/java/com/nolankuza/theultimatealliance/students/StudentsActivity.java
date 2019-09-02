package com.nolankuza.theultimatealliance.students;

import android.animation.Animator;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.ProgressBar;

import com.nolankuza.theultimatealliance.BaseActivity;
import com.nolankuza.theultimatealliance.R;
import com.nolankuza.theultimatealliance.dialogs.AddStudentDialogFragment;
import com.nolankuza.theultimatealliance.structure.Student;
import com.nolankuza.theultimatealliance.util.CSVReader;

import java.util.List;

import static com.nolankuza.theultimatealliance.util.Constants.CHOOSE_STUDENTS_FILE_CODE;

public class StudentsActivity extends BaseActivity implements AddStudentDialogFragment.Listener {

    ProgressBar progressBar;

    StudentAdapter studentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setToolBarMenu(R.menu.toolbar_students);
        setContentView(R.layout.activity_students);
        super.onCreate(savedInstanceState);

        actionBar.setSubtitle("Edit Students");

        progressBar = findViewById(R.id.progressBar);
        new StudentQueryTask(new StudentQueryTask.Listener() {
            @Override
            public void onTaskInit() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTaskCompleted(List<Student> students) {
                progressBar.setVisibility(View.GONE);
                RecyclerView eventRecycler = findViewById(R.id.student_recycler);
                eventRecycler.setLayoutManager(new LinearLayoutManager(StudentsActivity.this));
                eventRecycler.addItemDecoration(new DividerItemDecoration(StudentsActivity.this, DividerItemDecoration.VERTICAL));
                studentAdapter = new StudentAdapter(StudentsActivity.this, students);
                studentAdapter.setClickListener(new StudentAdapter.ItemClickListener() {
                    @Override
                    public void onRemoveClick(View view, final int position) {
                        new StudentRemoveTask(studentAdapter.getItem(position), new StudentRemoveTask.Listener() {
                            @Override
                            public void onTaskInit() {
                                progressBar.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onTaskCompleted(Integer status) {
                                progressBar.setVisibility(View.GONE);
                                studentAdapter.removeItem(position);
                            }
                        }).execute();
                    }
                });
                eventRecycler.setAdapter(studentAdapter);
            }
        }).execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_student_button:
                new AddStudentDialogFragment().show(getFragmentManager(), "AddStudent");
                return true;
            case R.id.from_csv_button:
                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.setType("*/*");
                chooseFile = Intent.createChooser(chooseFile, "Choose a CSV");
                startActivityForResult(chooseFile, CHOOSE_STUDENTS_FILE_CODE);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAddAccepted(DialogFragment dialog, final Student student) {
        new StudentAddTask(student, new StudentAddTask.Listener() {
            @Override
            public void onTaskInit() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTaskCompleted(Integer status) {
                progressBar.setVisibility(View.GONE);
                studentAdapter.addItem(student);
            }
        }).execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CHOOSE_STUDENTS_FILE_CODE) {
            if (resultCode == RESULT_OK) {
                new CSVReader(data, getContentResolver(), new CSVReader.Listener() {
                    @Override
                    public void onReadLine(String[] data) {
                        final Student student = new Student(data[0], data[1], Integer.parseInt(data[2]));
                        new StudentAddTask(student, new StudentAddTask.Listener() {
                            @Override
                            public void onTaskInit() {

                            }

                            @Override
                            public void onTaskCompleted(Integer status) {
                                studentAdapter.addItem(student);
                            }
                        }).execute();
                    }

                    @Override
                    public void onFinish() {

                    }
                }).execute();
            }
        }
    }
}