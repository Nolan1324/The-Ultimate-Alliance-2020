package com.nolankuza.theultimatealliance.settings;

import android.os.Bundle;

import com.nolankuza.theultimatealliance.BaseActivity;
import com.nolankuza.theultimatealliance.R;

public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SettingsFragment settingsFragment = new SettingsFragment();

        getFragmentManager().beginTransaction().replace(R.id.settings_fragment, settingsFragment).commit();
        setContentView(R.layout.activity_settings);
        super.onCreate(savedInstanceState);
    }
}
