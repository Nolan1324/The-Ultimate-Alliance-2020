package com.nolankuza.theultimatealliance.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.nolankuza.theultimatealliance.structure.Settings;

@Dao
public interface SettingsDao {
    @Query("SELECT * FROM settings")
    Settings get();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void update(Settings settings);
}
