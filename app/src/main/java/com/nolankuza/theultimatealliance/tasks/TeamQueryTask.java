package com.nolankuza.theultimatealliance.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.nolankuza.theultimatealliance.room.TeamDao;
import com.nolankuza.theultimatealliance.structure.Team;

import java.util.List;

import static com.nolankuza.theultimatealliance.ApplicationState.database;

public class TeamQueryTask extends AsyncTask<Void, Void, List<Team>> {
    private Listener listener;

    public TeamQueryTask(Context context, Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        void onTaskInit();
        void onTaskCompleted(List<Team> teams);
    }

    @Override
    protected void onPreExecute() {
        listener.onTaskInit();
    }

    @Override
    public List<Team> doInBackground(Void... voids) {
        TeamDao teamDao = database.teamDao();
        return teamDao.getAll();
    }

    @Override
    public void onPostExecute(List<Team> matches) {
        listener.onTaskCompleted(matches);
    }
}