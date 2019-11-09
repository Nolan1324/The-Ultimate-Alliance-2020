package com.nolankuza.theultimatealliance;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.UUID;

public class Constants {
    public static final String EVENT_DATE_FMT_STR = "yyyy-MM-dd";
    public static final SimpleDateFormat EVENT_DATE_FMT = new SimpleDateFormat(EVENT_DATE_FMT_STR, Locale.US);
    public static final int[] lockedItems = new int[] {
            R.id.event_import_option,
            R.id.student_option,
            R.id.assignment_option,
            R.id.settings_option,
            R.id.data_sync_option};
    public static final int[] masterItems = new int[] {
            R.id.assignment_option};
    public static final int CHOOSE_STUDENTS_FILE_CODE = 49;
    public static final int CHOOSE_TEAMS_FILE_CODE = 50;
    public static final int CHOOSE_MATCHES_FILE_CODE = 51;
    public static final UUID BLUETOOTH_UUID = UUID.fromString("dc0755f4-99b2-11e8-9eb6-529269fb1459");
    public static final String PREF_PIT_ASSIGN_CHANGED = "pit_assign_changed";
    public static final String PREF_SCOUT_ASSIGN_CHANGED = "pit_scout_changed";
    public static final String PREF_SYNCED_SETTINGS = "synced_settings";
    public static final String PREF_NEXT_MATCH = "next_match";
    public static final String PREF_CURRENT_SCOUTING_PAGE = "current_scouting_page";
}
