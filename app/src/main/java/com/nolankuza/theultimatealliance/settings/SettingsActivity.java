package com.nolankuza.theultimatealliance.settings;

import android.app.DialogFragment;
import android.preference.Preference;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.nolankuza.theultimatealliance.BaseActivity;
import com.nolankuza.theultimatealliance.R;
import com.nolankuza.theultimatealliance.dialogs.NewPasswordDialogFragment;
import com.nolankuza.theultimatealliance.dialogs.PasswordDialogFragment;
import com.nolankuza.theultimatealliance.structure.Settings;
import com.nolankuza.theultimatealliance.tasks.SettingsQueryTask;
import com.nolankuza.theultimatealliance.util.Sha256;

import java.util.Objects;

public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SettingsFragment settingsFragment = new SettingsFragment();

        getFragmentManager().beginTransaction().replace(R.id.settings_fragment, settingsFragment).commit();
        setContentView(R.layout.activity_settings);
        super.onCreate(savedInstanceState);
    }
}
