package com.nolankuza.theultimatealliance.eventimport.teamimport;

import android.os.AsyncTask;

import com.nolankuza.theultimatealliance.http.TheBlueAllianceClient;
import com.nolankuza.theultimatealliance.model.Team;

import java.util.List;

public class TeamImportTask extends AsyncTask<Void, Void, List<Team>> {
    private String eventKey;
    private Listener listener;

    public TeamImportTask(String eventKey, Listener listener) {
        this.eventKey = eventKey;
        this.listener = listener;
    }

    public interface Listener {
        void onTaskCompleted(List<Team> teams);
    }

    @Override
    public List<Team> doInBackground(Void... voids) {
        TheBlueAllianceClient client = new TheBlueAllianceClient();
        return client.getTeams(eventKey);
    }

    @Override
    public void onPostExecute(List<Team> teams) {
        listener.onTaskCompleted(teams);
    }
}