package com.nolankuza.theultimatealliance;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.nolankuza.theultimatealliance.assignment.AssignmentActivity;
import com.nolankuza.theultimatealliance.datasync.DataSyncActivity;
import com.nolankuza.theultimatealliance.dialogs.NewPasswordDialogFragment;
import com.nolankuza.theultimatealliance.dialogs.PasswordDialogFragment;
import com.nolankuza.theultimatealliance.eventimport.EventImportActivity;
import com.nolankuza.theultimatealliance.main.MainActivity;
import com.nolankuza.theultimatealliance.settings.SettingsActivity;
import com.nolankuza.theultimatealliance.model.Settings;
import com.nolankuza.theultimatealliance.students.StudentsActivity;
import com.nolankuza.theultimatealliance.tasks.SettingsQueryTask;

import static com.nolankuza.theultimatealliance.ApplicationState.locked;
import static com.nolankuza.theultimatealliance.Constants.lockedItems;
import static com.nolankuza.theultimatealliance.Constants.masterItems;

@SuppressLint("Registered")
public abstract class BaseActivity extends AppCompatActivity implements PasswordDialogFragment.Listener {

    private DrawerLayout drawerLayout;
    private MenuItem lockItem;
    public ActionBar actionBar;

    private ProgressBar progressBar;

    private int toolBarMenu = R.menu.toolbar_default;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set references
        try { progressBar = findViewById(R.id.progressBar); } catch (Exception ignored) {};
        drawerLayout = findViewById(R.id.drawer_layout);

        //Toolbar init
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        //Configure drawer item click events
        final NavigationView navigationView = findViewById(R.id.nav_view);
        final Class<? extends Activity> thisClass = this.getClass();
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        //Drawer formatting
                        drawerLayout.closeDrawers();
                        //Set the activity based on option
                        Class<?> intentClass = null;
                        switch(menuItem.getItemId()) {
                            case R.id.event_import_option:
                                intentClass = EventImportActivity.class;
                                break;
                            case R.id.student_option:
                                intentClass = StudentsActivity.class;
                                break;
                            case R.id.assignment_option:
                                intentClass = AssignmentActivity.class;
                                break;
                            case R.id.data_sync_option:
                                intentClass = DataSyncActivity.class;
                                break;
                            /*case R.id.personal_settings_option:
                                intentClass = PersonalSettingsActivity.class;
                                break;*/
                            case R.id.settings_option:
                                intentClass = SettingsActivity.class;
                                break;
                        }
                        if(intentClass != null) {
                            Intent intent = new Intent(getApplicationContext(), intentClass);
                            startActivity(intent);
                        }
                        //Remove from stack if not the MainActivity
                        if(thisClass != MainActivity.class) {
                            finish();
                        }
                        return false;
                    }
                });
    }

    @Override
    public final boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(toolBarMenu, menu);
        return true;
    }

    @Override
    public final boolean onPrepareOptionsMenu(Menu menu) {
        lockItem = menu.findItem(R.id.lock_button);
        updateMenuLock(lockItem, locked);
        updateIsMaster();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //Drawer button handler
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.lock_button:
                if(locked) {
                    new SettingsQueryTask(new SettingsQueryTask.Listener() {
                        @Override
                        public void onTaskCompleted(Settings settings) {
                            if(settings.passwordHash != null) {
                                Bundle bundle = new Bundle();
                                bundle.putByteArray("hash", settings.passwordHash);
                                bundle.putByteArray("salt", settings.passwordSalt);

                                PasswordDialogFragment passwordDialogFragment = new PasswordDialogFragment();
                                passwordDialogFragment.setArguments(bundle);
                                passwordDialogFragment.show(getFragmentManager(), "");
                            } else {
                                NewPasswordDialogFragment newPasswordDialogFragment = new NewPasswordDialogFragment();
                                newPasswordDialogFragment.show(getFragmentManager(), "");
                            }
                        }
                    }).execute();
                } else {
                    updateMenuLock(item, true);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public final void onPasswordCorrect(DialogFragment dialog) {
        updateMenuLock(lockItem, false);
    }

    @Override
    public void onBackPressed() {
        if(this.getClass() != MainActivity.class) {
            finish();
        }
        super.onBackPressed();
    }

    @Override
    public void onPostResume() {
        if(lockItem != null) {
            updateMenuLock(lockItem, locked);
        }
        updateIsMaster();
        super.onPostResume();
    }

    private void updateMenuLock(MenuItem lockItem, boolean newLocked) {
        if(newLocked) {
            lockItem.setIcon(getResources().getDrawable(R.drawable.ic_lock_closed));
        } else {
            lockItem.setIcon(getResources().getDrawable(R.drawable.ic_lock_open));
        }
        NavigationView navigationView = findViewById(R.id.nav_view);
        for(int itemId : lockedItems) {
            MenuItem item = navigationView.getMenu().findItem(itemId);
            item.setEnabled(!newLocked);
        }
        locked = newLocked;
        onMenuLockChanged(locked);
    }

    public final void updateIsMaster() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        for (int itemId : masterItems) {
            MenuItem item = navigationView.getMenu().findItem(itemId);
            item.setVisible(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("is_master_pref", false));
        }
    }

    //This method is for overriding
    public void onMenuLockChanged(boolean locked) {

    }

    public final void setToolBarMenu(int id) {
        toolBarMenu = id;
    }

    public final void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public final void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }
}
