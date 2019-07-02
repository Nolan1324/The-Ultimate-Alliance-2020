package com.nolankuza.theultimatealliance.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.nolankuza.theultimatealliance.room.Database;
import com.nolankuza.theultimatealliance.room.MatchDao;
import com.nolankuza.theultimatealliance.structure.Match;
import com.nolankuza.theultimatealliance.util.Constants;

import java.lang.ref.WeakReference;
import java.util.List;

import static com.nolankuza.theultimatealliance.ApplicationState.database;
import static com.nolankuza.theultimatealliance.ApplicationState.prefs;

public class MatchQueryTask extends AsyncTask<Void, Void, List<Match>> {
    private boolean getAll;
    private Listener listener;

    public MatchQueryTask(boolean getAll, Listener listener) {
        this.listener = listener;
        this.getAll = getAll;
    }

    public interface Listener {
        void onTaskInit();
        void onTaskCompleted(List<Match> matches);
    }

    @Override
    protected void onPreExecute() {
        listener.onTaskInit();
    }

    @Override
    public List<Match> doInBackground(Void... voids) {
        MatchDao matchDao = database.matchDao();
        if(getAll) {
            return matchDao.getAll();
        } else {
            return matchDao.getNotScouted(/*prefs.getInt(Constants.PREF_NEXT_MATCH, 1)*/);
        }
    }

    @Override
    public void onPostExecute(List<Match> matches) {
        listener.onTaskCompleted(matches);
    }
}