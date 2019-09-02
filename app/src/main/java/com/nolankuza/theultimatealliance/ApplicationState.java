package com.nolankuza.theultimatealliance;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.nolankuza.theultimatealliance.room.AppDatabase;

public class ApplicationState extends Application {
    public static boolean locked = true;
    public static AppDatabase database;
    public static SharedPreferences prefs;
    public static int masterCount = 1;

    @Override
    public void onCreate() {
        super.onCreate();
        database = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "the-ultimate-alliance").fallbackToDestructiveMigration().build();
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if(prefs.getBoolean("is_master_pref", false)) {
            locked = false;
        }
    }

    public AppDatabase getDatabase() {
        return database;
    }

    //TODO Load matches from csv
    //TODO Google Drive API?
    
}