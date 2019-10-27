package com.nolankuza.theultimatealliance.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.nolankuza.theultimatealliance.model.Team;

import java.util.List;

@Dao
public interface TeamDao {
    @Query("SELECT * FROM teams ORDER BY teamNumber")
    List<Team> getAll();

    @Query("SELECT * FROM teams WHERE `key`='frc' + :teamNumber")
    Team getByTeamNumber(int teamNumber);

    @Query("SELECT COUNT(teamNumber) FROM teams")
    int getTeamCount();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Team> teams);

    @Delete
    void delete(Team team);

    @Query("DELETE FROM teams")
    void deleteAll();
}
