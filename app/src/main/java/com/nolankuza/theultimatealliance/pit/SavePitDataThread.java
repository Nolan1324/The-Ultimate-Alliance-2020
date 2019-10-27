package com.nolankuza.theultimatealliance.pit;

import com.nolankuza.theultimatealliance.model.PitData;

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
