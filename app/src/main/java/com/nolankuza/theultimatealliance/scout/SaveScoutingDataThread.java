package com.nolankuza.theultimatealliance.scout;

import com.nolankuza.theultimatealliance.model.GameData;
import com.nolankuza.theultimatealliance.model.Match;

import static com.nolankuza.theultimatealliance.ApplicationState.database;

public class SaveScoutingDataThread extends Thread {
    private GameData gameData;
    private Listener listener;

    public SaveScoutingDataThread(GameData gameData, Listener listener) {
        this.gameData = gameData;
        this.listener = listener;
    }

    public interface Listener {
        void onTaskComplete();
    }

    public void run() {
        Match newMatch = database.matchDao().getByKey(gameData.matchKey);
        newMatch.scouted = true;
        database.matchDao().insert(newMatch);
        gameData.synced = false;
        database.gameDataDao().insert(gameData);

        listener.onTaskComplete();
    }
}
