package com.nolankuza.theultimatealliance.playoffs;

import com.nolankuza.theultimatealliance.structure.PlayoffData;

import static com.nolankuza.theultimatealliance.ApplicationState.database;

public class SavePlayoffDataThread extends Thread {
    private PlayoffData playoffData;
    private Listener listener;

    public SavePlayoffDataThread(PlayoffData playoffData, Listener listener) {
        this.playoffData = playoffData;
        this.listener = listener;
    }

    public interface Listener {
        void onTaskComplete();
    }

    public void run() {
        playoffData.synced = false;
        database.playoffDataDao().insert(playoffData);
        listener.onTaskComplete();
    }
}
