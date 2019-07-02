package com.nolankuza.theultimatealliance.pit;

import android.content.Context;

import com.nolankuza.theultimatealliance.room.AppDatabase;
import com.nolankuza.theultimatealliance.room.Database;
import com.nolankuza.theultimatealliance.room.MatchDao;
import com.nolankuza.theultimatealliance.structure.GameData;
import com.nolankuza.theultimatealliance.structure.Match;
import com.nolankuza.theultimatealliance.structure.PitData;

import java.lang.ref.WeakReference;

import static com.nolankuza.theultimatealliance.ApplicationState.database;

public class SavePitDataThread extends Thread {
    private PitData pitData;
    private Listener listener;

    public SavePitDataThread(PitData pitData, Listener listener) {
        this.pitData = pitData;
        this.listener = listener;
    }

    public interface Listener {
        void onTaskComplete();
    }

    public void run() {
        database.pitDataDao().insert(pitData);

        listener.onTaskComplete();
    }
}
