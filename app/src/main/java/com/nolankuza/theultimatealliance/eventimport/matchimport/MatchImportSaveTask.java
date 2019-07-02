package com.nolankuza.theultimatealliance.eventimport.matchimport;

import android.content.Context;
import android.os.AsyncTask;

import com.nolankuza.theultimatealliance.room.Database;
import com.nolankuza.theultimatealliance.room.MatchDao;
import com.nolankuza.theultimatealliance.structure.Match;

import java.lang.ref.WeakReference;
import java.util.List;

import static com.nolankuza.theultimatealliance.ApplicationState.database;

public class MatchImportSaveTask extends AsyncTask<Void, Void, Integer> {
    private List<Match> matches;
    private Listener listener;

    public MatchImportSaveTask(List<Match> matches, Listener listener) {
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