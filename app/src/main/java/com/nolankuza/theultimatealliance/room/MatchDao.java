package com.nolankuza.theultimatealliance.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.nolankuza.theultimatealliance.structure.Match;

import java.util.List;

@Dao
public interface MatchDao {
    @Query("SELECT * FROM matches ORDER by time")
    List<Match> getAll();

    @Query("SELECT * FROM matches WHERE scouted=0 ORDER by time")
    List<Match> getNotScouted();

    @Query("SELECT * FROM matches WHERE `key`=:key")
    Match getByKey(String key);

    @Query("SELECT COUNT(`key`) FROM matches")
    int getMatchCount();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Match> matches);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Match match);

    @Insert
    void addAll(List<Match> matches);

    @Query("UPDATE matches SET scouted=0")
    void unscoutAll();

    @Query("DELETE FROM matches")
    void deleteAll();

    @Delete
    void delete(Match match);
}