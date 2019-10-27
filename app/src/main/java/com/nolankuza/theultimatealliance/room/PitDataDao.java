package com.nolankuza.theultimatealliance.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.database.Cursor;

import com.nolankuza.theultimatealliance.model.PitData;

import java.util.List;

@Dao
public interface PitDataDao {
    @Query("SELECT * FROM pitdata")
    List<PitData> getAll();

    @Query("SELECT * FROM pitdata where scouted=0")
    List<PitData> getNotScouted();

    @Query("SELECT * FROM pitdata where scouted=1")
    List<PitData> getScouted();

    @Query("SELECT COUNT(*) FROM pitdata")
    Integer getPitCount();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PitData pitData);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<PitData> pitDataList);

    @Query("SELECT * FROM pitdata")
    Cursor getCursor();

    @Query("DELETE FROM pitdata")
    void deleteAll();
}