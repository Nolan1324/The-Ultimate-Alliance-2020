package com.nolankuza.theultimatealliance.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.nolankuza.theultimatealliance.structure.Assignment;

import java.util.List;

@Dao
public interface AssignmentDao {
    @Query("SELECT * FROM assignments ORDER BY name")
    List<Assignment> getAll();

    @Query("SELECT * FROM assignments WHERE name=:deviceName")
    List<Assignment> getByName(String deviceName);

    @Query("SELECT COUNT(role) FROM assignments WHERE role='6'")
    int getTotalPitScouters();

    @Query("SELECT COUNT(role) FROM assignments WHERE CAST(role as INT) <= 5")
    int getTotalMatchScouters();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Assignment assignment);

    @Delete
    void delete(Assignment assignment);

    @Query("DELETE FROM assignments")
    void deleteAll();
}
