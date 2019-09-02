package com.nolankuza.theultimatealliance.eventimport;

import android.os.AsyncTask;

import com.nolankuza.theultimatealliance.room.GameDataDao;
import com.nolankuza.theultimatealliance.room.MatchDao;
import com.nolankuza.theultimatealliance.room.SettingsDao;
import com.nolankuza.theultimatealliance.room.TeamDao;
import com.nolankuza.theultimatealliance.structure.Alliance;
import com.nolankuza.theultimatealliance.structure.Event;
import com.nolankuza.theultimatealliance.structure.GameData;
import com.nolankuza.theultimatealliance.structure.Match;
import com.nolankuza.theultimatealliance.structure.Settings;
import com.nolankuza.theultimatealliance.structure.Team;

import java.util.List;

import static com.nolankuza.theultimatealliance.ApplicationState.database;

public class EventImportSaveTask extends AsyncTask<Void, Void, Integer> {
    private Event event;
    private List<Match> matches;
    private List<Team> teams;
    private Listener listener;

    public EventImportSaveTask(Event event, List<Match> matches, List<Team> teams, Listener listener) {
        this.event = event;
        this.matches = matches;
        this.teams = teams;
        this.listener = listener;
    }

    public interface Listener {
        void onTaskInit();
        void onTaskCompleted(Integer status);
    }

    @Override
    protected void onPreExecute() {
        listener.onTaskInit();
    }

    @Override
    public Integer doInBackground(Void... voids) {
        SettingsDao settingsDao = database.settingsDao();
        Settings settings = settingsDao.get();
        settings.eventKey = event.key;
        settingsDao.update(settings);
        MatchDao matchDao = database.matchDao();
        matchDao.deleteAll();
        matchDao.insertAll(matches);
        TeamDao teamDao = database.teamDao();
        teamDao.deleteAll();
        teamDao.insertAll(teams);
        GameDataDao gameDataDao = database.gameDataDao();
        for(Match match : matches) {
            gameDataDao.insert(makeGameData(match.key, match.matchNumber, Alliance.Red, 1, Integer.parseInt(match.red1)));
            gameDataDao.insert(makeGameData(match.key, match.matchNumber, Alliance.Red, 2, Integer.parseInt(match.red2)));
            gameDataDao.insert(makeGameData(match.key, match.matchNumber, Alliance.Red, 3, Integer.parseInt(match.red3)));
            gameDataDao.insert(makeGameData(match.key, match.matchNumber, Alliance.Blue, 1, Integer.parseInt(match.blue1)));
            gameDataDao.insert(makeGameData(match.key, match.matchNumber, Alliance.Blue, 2, Integer.parseInt(match.blue2)));
            gameDataDao.insert(makeGameData(match.key, match.matchNumber, Alliance.Blue, 3, Integer.parseInt(match.blue3)));
        }
        return 1;
    }

    @Override
    public void onPostExecute(Integer status) {
        listener.onTaskCompleted(status);
    }

    private static GameData makeGameData(String matchKey, int matchNumber, Alliance alliance, int driverStation, int teamNumber) {
        GameData gameData = new GameData();
        gameData.matchKey = matchKey;
        gameData.matchNumber = matchNumber;
        gameData.alliance = alliance;
        gameData.driverStation = driverStation;
        gameData.teamNumber = teamNumber;
        return gameData;
    }
}