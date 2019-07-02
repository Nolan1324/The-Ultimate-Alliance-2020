package com.nolankuza.theultimatealliance.tasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.nolankuza.theultimatealliance.room.Database;
import com.nolankuza.theultimatealliance.room.PitDataDao;
import com.nolankuza.theultimatealliance.room.SettingsDao;
import com.nolankuza.theultimatealliance.structure.PitData;
import com.nolankuza.theultimatealliance.structure.Settings;
import com.nolankuza.theultimatealliance.structure.Team;
import com.nolankuza.theultimatealliance.util.DataUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static com.nolankuza.theultimatealliance.ApplicationState.database;
import static com.nolankuza.theultimatealliance.ApplicationState.prefs;

public class PitQueryTask extends AsyncTask<Void, Void, List<PitData>> {
    private boolean getAll;
    private Listener listener;

    public PitQueryTask(boolean getAll, Listener listener) {
        this.getAll = getAll;
        this.listener = listener;
    }

    public interface Listener {
        void onTaskCompleted(List<PitData> pitDataList);
    }

    @Override
    public List<PitData> doInBackground(Void... voids) {
        //TODO Do not query scouted pits if getAll is false
        PitDataDao pitDataDao = database.pitDataDao();
        List<PitData> pitDataList = pitDataDao.getAll();
        if(pitDataList.size() == 0) {
            List<Team> teams = database.teamDao().getAll();
            for(Team team : teams) {
                PitData pitData = new PitData();
                pitData.eventKey = DataUtil.clean(database.settingsDao().get().eventKey);
                pitData.teamNumber = team.teamNumber;
                pitData.teamName = team.nickname;
                pitDataDao.insert(pitData);
            }
            pitDataList = pitDataDao.getAll();
        }
        double teamPortion = prefs.getInt("team_portion", 0);
        double totalPitScouters = prefs.getInt("total_pit_scouters", 0);
        if(teamPortion != 0 && totalPitScouters != 0 && !getAll) {
            double listSize = pitDataList.size();
            int start = (int) Math.floor(listSize * ((teamPortion - 1) / totalPitScouters));
            int end = (int) Math.floor(listSize * (teamPortion / totalPitScouters));
            pitDataList = pitDataList.subList(start, end);
        }
        if(getAll) {
            return pitDataList;
        } else {
            List<PitData> pitDataListOut = new ArrayList<>();
            for (PitData pitData : pitDataList) {
                if(!pitData.scouted) {
                    pitDataListOut.add(pitData);
                }
            }
            return pitDataListOut;
        }
    }

    @Override
    public void onPostExecute(List<PitData> pitDataList) {
        listener.onTaskCompleted(pitDataList);
    }
}