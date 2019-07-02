package com.nolankuza.theultimatealliance.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.nolankuza.theultimatealliance.structure.Assignment;
import com.nolankuza.theultimatealliance.structure.GameData;
import com.nolankuza.theultimatealliance.structure.Match;
import com.nolankuza.theultimatealliance.structure.PitData;
import com.nolankuza.theultimatealliance.structure.PlayoffData;
import com.nolankuza.theultimatealliance.structure.Settings;
import com.nolankuza.theultimatealliance.structure.Student;
import com.nolankuza.theultimatealliance.structure.Team;

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