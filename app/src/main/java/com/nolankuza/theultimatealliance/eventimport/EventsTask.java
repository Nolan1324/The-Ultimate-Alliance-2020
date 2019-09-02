package com.nolankuza.theultimatealliance.eventimport;

import android.os.AsyncTask;

import com.nolankuza.theultimatealliance.http.TheBlueAllianceClient;
import com.nolankuza.theultimatealliance.structure.Event;

import java.util.List;

public class EventsTask extends AsyncTask<Void, Void, List<Event>> {
    private int teamNumber;
    private int year;
    private Listener listener;

    EventsTask(int teamNumber, int year, Listener listener) {
        this.teamNumber = teamNumber;
        this.year = year;
        this.listener = listener;
    }

    public interface Listener{
        void onTaskCompleted(List<Event> events);
    }

    @Override
    public List<Event> doInBackground(Void... voids) {
        TheBlueAllianceClient client = new TheBlueAllianceClient();
        return client.getEvents(teamNumber, year);
    }

    @Override
    public void onPostExecute(List<Event> events) {
        listener.onTaskCompleted(events);
    }
}