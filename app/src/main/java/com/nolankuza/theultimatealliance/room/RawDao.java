package com.nolankuza.theultimatealliance.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Transaction;

import static com.nolankuza.theultimatealliance.ApplicationState.database;

@Dao
public abstract class RawDao {
    private static final String query = "INSERT INTO gamedata (matchKey,matchNumber,alliance,driverStation,teamNumber) VALUES (?,?,?,?,?)";

    @Transaction
    public void initGameData(Object[] data) {
        database.query(query, data);
    }
}
