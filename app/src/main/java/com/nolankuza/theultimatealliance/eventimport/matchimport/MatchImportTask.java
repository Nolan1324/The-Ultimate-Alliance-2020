package com.nolankuza.theultimatealliance.eventimport.matchimport;

import android.os.AsyncTask;

import com.nolankuza.theultimatealliance.http.TheBlueAllianceClient;
import com.nolankuza.theultimatealliance.structure.Match;

import java.util.List;

public class MatchImportTask extends AsyncTask<Void, Void, List<Match>> {
    private String eventKey;
    private Listener listener;

    public MatchImportTask(String eventKey, Listener listener) {
        this.eventKey = eventKey;
        this.listener = listener;
    }

    public interface Listener {
        void onTaskCompleted(List<Match> events);
    }

    @Override
    public List<Match> doInBackground(Void... voids) {
        TheBlueAllianceClient client = new TheBlueAllianceClient();
        return client.getMatches(eventKey);
    }

    @Override
    public void onPostExecute(List<Match> matches) {
        listener.onTaskCompleted(matches);
    }
}