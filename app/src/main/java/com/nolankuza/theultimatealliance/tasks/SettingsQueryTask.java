package com.nolankuza.theultimatealliance.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.nolankuza.theultimatealliance.room.Database;
import com.nolankuza.theultimatealliance.room.SettingsDao;
import com.nolankuza.theultimatealliance.structure.Settings;

import java.lang.ref.WeakReference;

import static com.nolankuza.theultimatealliance.ApplicationState.database;

public class SettingsQueryTask extends AsyncTask<Void, Void, Settings> {
    private Listener listener;

    public SettingsQueryTask(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        void onTaskInit();
        void onTaskCompleted(Settings settings);
    }

    @Override
    protected void onPreExecute() {
        listener.onTaskInit();
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