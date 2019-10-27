package com.nolankuza.theultimatealliance.datasync;

import android.content.Context;
import android.os.AsyncTask;

import com.nolankuza.theultimatealliance.model.Settings;

import java.lang.ref.WeakReference;

import static com.nolankuza.theultimatealliance.ApplicationState.database;

public class DeleteTask extends AsyncTask<Void, Void, Integer> {
    private final WeakReference<Context> context;
    private SyncOptions options;

    public DeleteTask(Context context, SyncOptions syncOptions) {
        this.context = new WeakReference<>(context);
        this.options = syncOptions;
    }

    @Override
    public Integer doInBackground(Void... voids) {
        if(options.game) {
            database.gameDataDao().deleteAll();
            database.matchDao().unscoutAll();
        }
        if(options.pit) {
            database.pitDataDao().deleteAll();
        }
        if(options.playoffs) {
            database.playoffDataDao().deleteAll();
        }
        if(options.event) {
            database.matchDao().deleteAll();
        }
        if(options.teams) {
            database.teamDao().deleteAll();
        }
        if(options.assignments) {
            database.assignmentDao().deleteAll();
        }
        if(options.students) {
            database.studentDao().deleteAll();
        }
        if(options.settings) {
            database.settingsDao().update(new Settings());
        }
        return 1;
    }
}
