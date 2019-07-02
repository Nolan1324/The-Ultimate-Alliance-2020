package com.nolankuza.theultimatealliance.students;

import android.content.Context;
import android.os.AsyncTask;

import com.nolankuza.theultimatealliance.room.Database;
import com.nolankuza.theultimatealliance.room.StudentDao;
import com.nolankuza.theultimatealliance.structure.Student;

import java.lang.ref.WeakReference;

import static com.nolankuza.theultimatealliance.ApplicationState.database;

public class StudentAddTask extends AsyncTask<Void, Void, Integer> {
    private final Student student;
    private Listener listener;

    public StudentAddTask(Student student, Listener listener) {
        this.student = student;
        this.listener = listener;
    }

    public interface Listener {
        void onTaskInit();
        void onTaskCompleted(Integer status);
    }

    @Override
    protected void onPreExecute() {
        listener.onTaskInit();
    }

    @Override
    public Integer doInBackground(Void... voids) {
        StudentDao studentDao = database.studentDao();
        studentDao.insert(student);
        return 1;
    }

    @Override
    public void onPostExecute(Integer status) {
        listener.onTaskCompleted(status);
    }
}