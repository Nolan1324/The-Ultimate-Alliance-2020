package com.nolankuza.theultimatealliance.eventimport.teamimport;

import android.os.AsyncTask;

import com.nolankuza.theultimatealliance.room.MatchDao;
import com.nolankuza.theultimatealliance.structure.Match;

import java.util.List;

import static com.nolankuza.theultimatealliance.ApplicationState.database;

public class TeamImportSaveTask extends AsyncTask<Void, Void, Integer> {
    private List<Match> matches;
    private Listener listener;

    public TeamImportSaveTask(List<Match> matches, Listener listener) {
        this.matches = matches;
        this.listener = listener;
    }

    public interface Listener {
        void onTaskInit();
        void onTaskCompleted(Integer size);
    }

    @Override
    protected void onPreExecute() {
        listener.onTaskInit();
    }

    @Override
    public Integer doInBackground(Void... voids) {
        MatchDao matchDao = database.matchDao();
        matchDao.deleteAll();
        matchDao.insertAll(matches);
        return matches.size();
    }

    @Override
    public void onPostExecute(Integer size) {
        listener.onTaskCompleted(size);
    }
}