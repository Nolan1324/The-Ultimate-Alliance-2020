package com.nolankuza.theultimatealliance.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.nolankuza.theultimatealliance.model.Assignment;
import com.nolankuza.theultimatealliance.model.GameData;
import com.nolankuza.theultimatealliance.model.Match;
import com.nolankuza.theultimatealliance.model.PitData;
import com.nolankuza.theultimatealliance.model.PlayoffData;
import com.nolankuza.theultimatealliance.model.Settings;
import com.nolankuza.theultimatealliance.model.Student;
import com.nolankuza.theultimatealliance.model.Team;

@Database(entities = {Match.class, Student.class, Assignment.class, Team.class, Settings.class, GameData.class, PitData.class, PlayoffData.class}, version = 37, exportSchema = true)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract MatchDao matchDao();
    public abstract StudentDao studentDao();
    public abstract AssignmentDao assignmentDao();
    public abstract TeamDao teamDao();
    public abstract SettingsDao settingsDao();
    public abstract GameDataDao gameDataDao();
    public abstract PitDataDao pitDataDao();
    public abstract PlayoffDataDao playoffDataDao();
}