package com.nolankuza.theultimatealliance.settings;

import android.content.Context;

import com.nolankuza.theultimatealliance.room.Database;
import com.nolankuza.theultimatealliance.structure.Settings;

import java.lang.ref.WeakReference;

import static com.nolankuza.theultimatealliance.ApplicationState.database;

public class SaveSettingsThread extends Thread {

    private Settings settings;
    private Listener listener;

    public interface Listener {
        void onTaskCompleted();
    }

    public SaveSettingsThread(Settings settings, Listener listener) {
        this.settings = settings;
        this.listener = listener;
    }

    @Override
    public void run() {
        database.settingsDao().update(settings);
        listener.onTaskCompleted();
    }
}
