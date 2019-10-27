package com.nolankuza.theultimatealliance.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.nolankuza.theultimatealliance.BaseActivity;
import com.nolankuza.theultimatealliance.R;
import com.nolankuza.theultimatealliance.dialogs.NewPasswordDialogFragment;
import com.nolankuza.theultimatealliance.model.Settings;

import static com.nolankuza.theultimatealliance.ApplicationState.database;
import static com.nolankuza.theultimatealliance.ApplicationState.prefs;

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceChangeListener {

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);
        SharedPreferences sharedPreferences = getPreferenceManager().getSharedPreferences();
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        for(String key : sharedPreferences.getAll().keySet()) {
            updateSummary(findPreference(key));
        }

        findPreference("team_pref").setOnPreferenceChangeListener(this);
        findPreference("year_pref").setOnPreferenceChangeListener(this);
        findPreference("student_pref").setOnPreferenceChangeListener(this);
        findPreference("password_pref").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                NewPasswordDialogFragment newPasswordDialogFragment = new NewPasswordDialogFragment();
                newPasswordDialogFragment.show(getFragmentManager(), "");
                return true;
            }
        });

        updateField();
        findPreference("field_reverse_pref").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                prefs.edit().putBoolean("field_reverse_pref", !prefs.getBoolean("field_reverse_pref", false)).apply();
                setPreferenceScreen(null);
                onCreate(savedInstanceState);
                return false;
            }
        });
    }

    private void updateField() {
        findPreference("field_reverse_pref").setLayoutResource(prefs.getBoolean("field_reverse_pref", false) ? R.layout.fragment_field_setting_blue : R.layout.fragment_field_setting_red);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, final String key) {
        Preference pref = findPreference(key);

        updateSummary(pref);
        if(key.equals("is_master_pref")) {
            BaseActivity activity = (BaseActivity) getActivity();
            if(activity != null) {
                activity.updateIsMaster();
            }
        } else if(key.equals("allow_pref")) {
            new Thread() {
                @Override
                public void run() {
                    Settings settings = database.settingsDao().get();
                    settings.allowStudentsToChangeName = prefs.getBoolean(key,false);
                    database.settingsDao().update(settings);
                }
            }.start();
        } else if(key.equals("show_all_pref")) {
            new Thread() {
                @Override
                public void run() {
                    Settings settings = database.settingsDao().get();
                    settings.showAll = prefs.getBoolean(key,false);
                    database.settingsDao().update(settings);
                }
            }.start();
        } else if(key.equals("field_reverse_pref")) {
            new Thread() {
                @Override
                public void run() {
                    Settings settings = database.settingsDao().get();
                    settings.fieldReverse = prefs.getBoolean(key,false);
                    database.settingsDao().update(settings);
                }
            }.start();
        }
    }

    public void updateSummary(Preference pref) {
        if(pref instanceof ListPreference) {
            ListPreference listPref = (ListPreference) pref;
            pref.setSummary(listPref.getEntry());
        } else if(pref instanceof EditTextPreference) {
            EditTextPreference textPref = (EditTextPreference) pref;
            pref.setSummary(textPref.getText());
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        return o.toString().length() != 0;
    }
}
