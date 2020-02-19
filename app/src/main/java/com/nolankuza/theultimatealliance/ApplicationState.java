package com.nolankuza.theultimatealliance;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.nolankuza.theultimatealliance.model.Settings;
import com.nolankuza.theultimatealliance.room.AppDatabase;
import com.nolankuza.theultimatealliance.room.SettingsDao;
import com.nolankuza.theultimatealliance.util.Prefs;

public class ApplicationState extends Application {
    public static boolean locked = true;
    public static AppDatabase database;
    public static SharedPreferences prefs;
    public static int masterCount = 1;

    public static boolean changeTheme = true;
    public static boolean masterThemeChanged;

    @Override
    public void onCreate() {
        super.onCreate();
        database = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "the-ultimate-alliance").fallbackToDestructiveMigration().build();
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if(Prefs.getIsMaster(false)) {
            locked = false;
        }

        //Populate settings database if empty
        new Thread() {
            @Override
            public void run() {
                SettingsDao settingsDao = database.settingsDao();
                if(settingsDao.get() == null) {
                    settingsDao.update(new Settings());
                }
            }
        }.start();
    }

    public AppDatabase getDatabase() {
        return database;
    }

    //TODO Google Drive API?
}
