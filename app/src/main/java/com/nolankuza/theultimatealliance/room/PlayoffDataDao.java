package com.nolankuza.theultimatealliance.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.database.Cursor;

import com.nolankuza.theultimatealliance.structure.GameData;
import com.nolankuza.theultimatealliance.structure.PlayoffData;

import java.util.List;

@Dao
public interface PlayoffDataDao {
    @Query("SELECT * FROM playoffdata ORDER BY `index`")
    List<PlayoffData> getAll();

    @Query("SELECT * FROM playoffdata WHERE scouted = 0 ORDER BY `index`")
    List<PlayoffData> getUnscouted();

    //TODO Can these six methods be one method that returns a List?
    @Query("SELECT * FROM playoffdata WHERE alliance = 0 AND driverStation = 1 AND scouted <> 0 AND `index` = :index")
    PlayoffData getRed1(int index);

    @Query("SELECT * FROM playoffdata WHERE alliance = 0 AND driverStation = 2 AND scouted <> 0 AND `index` = :index")
    PlayoffData getRed2(int index);

    @Query("SELECT * FROM playoffdata WHERE alliance = 0 AND driverStation = 3 AND scouted <> 0 AND `index` = :index")
    PlayoffData getRed3(int index);

    @Query("SELECT * FROM playoffdata WHERE alliance = 1 AND driverStation = 1 AND scouted <> 0 AND `index` = :index")
    PlayoffData getBlue1(int index);

    @Query("SELECT * FROM playoffdata WHERE alliance = 1 AND driverStation = 2 AND scouted <> 0 AND `index` = :index")
    PlayoffData getBlue2(int index);

    @Query("SELECT * FROM playoffdata WHERE alliance = 1 AND driverStation = 3 AND scouted <> 0 AND `index` = :index")
    PlayoffData getBlue3(int index);

    @Query("SELECT DISTINCT COUNT(matchKey) FROM gamedata WHERE scouted <> 0")
    int getScoutCount();

    @Query("SELECT COUNT(*) FROM playoffdata WHERE alliance = 0 AND driverStation = 1 AND scouted <> 0")
    int getRed1Count();

    @Query("SELECT COUNT(*) FROM playoffdata WHERE alliance = 0 AND driverStation = 2 AND scouted <> 0")
    int getRed2Count();

    @Query("SELECT COUNT(*) FROM playoffdata WHERE alliance = 0 AND driverStation = 3 AND scouted <> 0")
    int getRed3Count();

    @Query("SELECT COUNT(*) FROM playoffdata WHERE alliance = 1 AND driverStation = 1 AND scouted <> 0")
    int getBlue1Count();

    @Query("SELECT COUNT(*) FROM playoffdata WHERE alliance = 1 AND driverStation = 2 AND scouted <> 0")
    int getBlue2Count();

    @Query("SELECT COUNT(*) FROM playoffdata WHERE alliance = 1 AND driverStation = 3 AND scouted <> 0")
    int getBlue3Count();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PlayoffData playoffData);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<PlayoffData> playoffDataList);

    @Query("DELETE FROM playoffdata")
    void deleteAll();
}