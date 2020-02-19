package com.nolankuza.theultimatealliance.util;

import static com.nolankuza.theultimatealliance.ApplicationState.changeTheme;
import static com.nolankuza.theultimatealliance.ApplicationState.prefs;
import static com.nolankuza.theultimatealliance.Constants.PREF_CURRENT_SCOUTING_PAGE;
import static com.nolankuza.theultimatealliance.Constants.PREF_NEXT_MATCH;
import static com.nolankuza.theultimatealliance.Constants.PREF_PIT_ASSIGN_CHANGED;
import static com.nolankuza.theultimatealliance.Constants.PREF_SCOUT_ASSIGN_CHANGED;
import static com.nolankuza.theultimatealliance.Constants.PREF_SYNCED_SETTINGS;

public class Prefs {
    //region Getters
    public static boolean getIsMaster(boolean defValue) {
        return prefs.getBoolean("is_master_pref", defValue);
    }

    public static String getTeam(String defValue) {
        return prefs.getString("team_pref", defValue);
    }

    public static String getYear(String defValue) {
        return prefs.getString("year_pref", defValue);
    }

    public static boolean getAllowStudentsToChangeName(boolean defValue) {
        return prefs.getBoolean("allow_pref", defValue);
    }

    public static boolean getShowAll(boolean defValue) {
        return prefs.getBoolean("show_all_pref", defValue);
    }

    public static boolean getFieldReverse(boolean defValue) {
        return prefs.getBoolean("field_reverse_pref", defValue);
    }

    public static String getDriverStation(String defValue) {
        return prefs.getString("driver_pref", defValue);
    }

    public static String getStudent(String defValue) {
        return prefs.getString("student_pref", defValue);
    }

    public static boolean getPlayoffs(boolean defValue) {
        return prefs.getBoolean("playoffs_pref", defValue);
    }

    public static boolean getPitAssignChanged(boolean defValue) {
        return prefs.getBoolean(PREF_PIT_ASSIGN_CHANGED, defValue);
    }

    public static boolean getScoutAssignChanged(boolean defValue) {
        return prefs.getBoolean(PREF_SCOUT_ASSIGN_CHANGED, defValue);
    }

    public static boolean getSyncedSettings(boolean defValue) {
        return prefs.getBoolean(PREF_SYNCED_SETTINGS, defValue);
    }

    public static String getNextMatch(String defValue) {
        return prefs.getString(PREF_NEXT_MATCH, defValue);
    }

    public static int getCurrentScoutingPage(int defValue) {
        return prefs.getInt(PREF_CURRENT_SCOUTING_PAGE, defValue);
    }

    public static int getMasterPage(int defValue) {
        return prefs.getInt("master_page", defValue);
    }

    public static int getTheme(int defValue) {
        return prefs.getInt("app_theme", defValue);
    }
    //endregion

    //region Getters
    public static void setIsMaster(boolean value) {
        prefs.edit().putBoolean("is_master_pref", value).apply();
    }

    public static void setTeam(String value) {
        prefs.edit().putString("team_pref", value).apply();
    }

    public static void setYear(String value) {
        prefs.edit().putString("year_pref", value).apply();
    }

    public static void setAllowStudentsToChangeName(boolean value) {
        prefs.edit().putBoolean("allow_pref", value).apply();
    }

    public static void setShowAll(boolean value) {
        prefs.edit().putBoolean("show_all_pref", value).apply();
    }

    public static void setFieldReverse(boolean value) {
        prefs.edit().putBoolean("field_reverse_pref", value).apply();
    }

    public static void setDriverStation(String value) {
        prefs.edit().putString("driver_pref", value).apply();
    }

    public static void setStudent(String value) {
        prefs.edit().putString("student_pref", value).apply();
    }

    public static void setPlayoffs(boolean value) {
        prefs.edit().putBoolean("playoffs_pref", value).apply();
    }

    public static void setPitAssignChanged(boolean value) {
        prefs.edit().putBoolean(PREF_PIT_ASSIGN_CHANGED, value).apply();
    }

    public static void setScoutAssignChanged(boolean value) {
        prefs.edit().putBoolean(PREF_SCOUT_ASSIGN_CHANGED, value).apply();
    }

    public static void setSyncedSettings(boolean value) {
        prefs.edit().putBoolean(PREF_SYNCED_SETTINGS, value).apply();
    }

    public static void setNextMatch(String value) {
        prefs.edit().putString(PREF_NEXT_MATCH, value).apply();
    }

    public static void setCurrentScoutingPage(int value) {
        prefs.edit().putInt(PREF_CURRENT_SCOUTING_PAGE, value).apply();
    }

    public static void setMasterPage(int value) {
        prefs.edit().putInt("master_page", value).apply();
    }

    public static void setTheme(int value) {
        prefs.edit().putInt("app_theme", value).apply();
        changeTheme = true;
    }
    //endregion
}
