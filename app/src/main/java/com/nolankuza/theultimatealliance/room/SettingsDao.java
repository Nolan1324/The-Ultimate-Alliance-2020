package com.nolankuza.theultimatealliance.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.nolankuza.theultimatealliance.structure.Settings;
import com.nolankuza.theultimatealliance.structure.Team;

import java.util.List;

@Dao
public interface SettingsDao {
    @Query("SELECT * FROM settings")
    Settings get();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void update(Settings settings);
}
