package com.nolankuza.theultimatealliance.personalsettings;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.view.MenuItem;

import com.nolankuza.theultimatealliance.BaseActivity;
import com.nolankuza.theultimatealliance.R;

public class PersonalSettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_personal_settings);
        super.onCreate(savedInstanceState);
        actionBar.setTitle("Personal Preferences");

        final NavigationView navigationView = findViewById(R.id.nav_view);
        //final MenuItem item = navigationView.getMenu().findItem(R.id.personal_settings_option);
        //item.setChecked(true);
        //item.setEnabled(false);
    }
}
