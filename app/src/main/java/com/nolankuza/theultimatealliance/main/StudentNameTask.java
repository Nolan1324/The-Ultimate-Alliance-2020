package com.nolankuza.theultimatealliance.main;

import android.os.AsyncTask;

import java.util.List;

import static com.nolankuza.theultimatealliance.ApplicationState.database;

public class StudentNameTask extends AsyncTask<Void, Void, List<String>> {
    Listener listener;

    interface Listener {
        void onTaskCompleted(List<String> students);
    }

    public StudentNameTask(Listener listener) {
        this.listener = listener;
    }

    @Override
    protected List<String> doInBackground(Void... voids) {
        return database.studentDao().getNames();
    }

    @Override
    protected void onPostExecute(List<String> students) {
        listener.onTaskCompleted(students);
    }
}