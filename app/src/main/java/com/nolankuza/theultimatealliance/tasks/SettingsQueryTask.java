package com.nolankuza.theultimatealliance.tasks;

import android.os.AsyncTask;

import com.nolankuza.theultimatealliance.structure.Settings;

import static com.nolankuza.theultimatealliance.ApplicationState.database;

public class SettingsQueryTask extends AsyncTask<Void, Void, Settings> {
    private Listener listener;

    public SettingsQueryTask(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        void onTaskCompleted(Settings settings);
    }

    @Override
    public Settings doInBackground(Void... voids) {
        Settings settings = database.settingsDao().get();
        if(settings == null) {
            return new Settings();
        } else {
            return database.settingsDao().get();
        }
    }

    @Override
    public void onPostExecute(Settings settings) {
        listener.onTaskCompleted(settings);
    }
}