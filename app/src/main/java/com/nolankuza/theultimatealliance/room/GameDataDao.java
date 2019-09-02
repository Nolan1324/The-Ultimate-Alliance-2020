package com.nolankuza.theultimatealliance.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.database.Cursor;

import com.nolankuza.theultimatealliance.structure.GameData;

import java.util.List;

@Dao
public interface GameDataDao {
    @Query("SELECT * FROM gamedata WHERE synced = 0")
    List<GameData> getAll();

    @Query("SELECT * FROM gamedata WHERE matchKey = :matchKey")
    GameData get(String matchKey);

    @Query("SELECT COUNT(*) FROM gamedata WHERE matchKey = :matchKey")
    int exists(String matchKey);

    @Query("SELECT DISTINCT COUNT(matchKey) FROM gamedata WHERE scouted <> 0")
    int getScoutCount();

    @Query("SELECT COUNT(*) FROM gamedata WHERE alliance = 0 AND driverStation = 1 AND scouted <> 0")
    int getRed1Count();

    @Query("SELECT COUNT(*) FROM gamedata WHERE alliance = 0 AND driverStation = 2 AND scouted <> 0")
    int getRed2Count();

    @Query("SELECT COUNT(*) FROM gamedata WHERE alliance = 0 AND driverStation = 3 AND scouted <> 0")
    int getRed3Count();

    @Query("SELECT COUNT(*) FROM gamedata WHERE alliance = 1 AND driverStation = 1 AND scouted <> 0")
    int getBlue1Count();

    @Query("SELECT COUNT(*) FROM gamedata WHERE alliance = 1 AND driverStation = 2 AND scouted <> 0")
    int getBlue2Count();

    @Query("SELECT COUNT(*) FROM gamedata WHERE alliance = 1 AND driverStation = 3 AND scouted <> 0")
    int getBlue3Count();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(GameData gameData);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<GameData> gameDataList);

    @Query("SELECT * FROM gamedata WHERE matchKey LIKE '%qm%' ORDER BY teamNumber, matchNumber")
    Cursor getCursor();

    @Query("SELECT * FROM gamedata WHERE matchKey NOT LIKE '%qm%' ORDER BY teamNumber, matchNumber")
    Cursor getPlayoffCursor();

    @Query("DELETE FROM gamedata")
    void deleteAll();

    @Query("DELETE FROM gamedata WHERE matchKey = :matchKey")
    void delete(String matchKey);

    @Query("UPDATE gamedata SET synced = 1")
    void syncAll();
}