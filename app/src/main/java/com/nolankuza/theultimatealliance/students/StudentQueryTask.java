package com.nolankuza.theultimatealliance.students;

import android.os.AsyncTask;

import com.nolankuza.theultimatealliance.room.StudentDao;
import com.nolankuza.theultimatealliance.structure.Student;

import java.util.List;

import static com.nolankuza.theultimatealliance.ApplicationState.database;

public class StudentQueryTask extends AsyncTask<Void, Void, List<Student>> {
    private Listener listener;

    public StudentQueryTask(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        void onTaskInit();
        void onTaskCompleted(List<Student> students);
    }

    @Override
    protected void onPreExecute() {
        listener.onTaskInit();
    }

    @Override
    public List<Student> doInBackground(Void... voids) {
        StudentDao studentDao = database.studentDao();
        return studentDao.getAll();
    }

    @Override
    public void onPostExecute(List<Student> students) {
        listener.onTaskCompleted(students);
    }
}