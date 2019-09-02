package com.nolankuza.theultimatealliance.room;

import android.content.Context;

import com.nolankuza.theultimatealliance.ApplicationState;

public class Database {
    public static AppDatabase get(Context context) {
        return ApplicationState.database;
        //return Room.databaseBuilder(context, AppDatabase.class, "the-ultimate-alliance").fallbackToDestructiveMigration().build();
    }
}
