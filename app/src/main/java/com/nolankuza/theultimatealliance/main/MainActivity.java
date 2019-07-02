package com.nolankuza.theultimatealliance.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Switch;

import com.nolankuza.theultimatealliance.BaseActivity;
import com.nolankuza.theultimatealliance.R;
import com.nolankuza.theultimatealliance.main.master.MasterFragment;
import com.nolankuza.theultimatealliance.room.SettingsDao;
import com.nolankuza.theultimatealliance.scout.ScoutBasicActivity;
import com.nolankuza.theultimatealliance.structure.Match;
import com.nolankuza.theultimatealliance.structure.PitData;
import com.nolankuza.theultimatealliance.structure.Settings;
import com.nolankuza.theultimatealliance.tasks.BluetoothServerThread;
import com.nolankuza.theultimatealliance.tasks.MatchQueryTask;
import com.nolankuza.theultimatealliance.tasks.PitQueryTask;
import com.nolankuza.theultimatealliance.util.Constants;

import java.util.List;

import static com.nolankuza.theultimatealliance.ApplicationState.database;
import static com.nolankuza.theultimatealliance.ApplicationState.locked;
import static com.nolankuza.theultimatealliance.ApplicationState.prefs;

public class MainActivity extends BaseActivity {

    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);

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

        new BluetoothServerThread(getApplicationContext(), new BluetoothServerThread.Listener() {
            @Override
            public void onTaskInit() { }

            @Override
            public void onMatchesSynced() {
                //TODO Bind getAll to a control

                if(fragment.getClass() == SlaveFragment.class) {
                    ((SlaveFragment) fragment).updateMatches();
                }
            }

            @Override
            public void onTeamsSynced() {
                if(fragment.getClass() == PitFragment.class) {
                    ((PitFragment) fragment).updatePits();
                }
            }

            @Override
            public void onPlayoffsSynced() {
                if(fragment.getClass() == AnalysisFragment.class) {
                    View view = fragment.getView();
                    if(view != null) {
                        ((AnalysisFragment) fragment).update(view);
                    }
                }
            }

            @Override
            public void onAssignmentSynced() {
                loadFragment();
            }

            @Override
            public void onStudentsSynced() {
                if(fragment.getClass() == SlaveFragment.class) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            View view = fragment.getView();
                            if(view != null) {
                                ((SlaveFragment) fragment).updateSettings(view, true, false);
                            }
                        }
                    });
                } else if(fragment.getClass() == PitFragment.class) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            View view = fragment.getView();
                            if(view != null) {
                                ((PitFragment) fragment).updateSettings(view, true, false);
                            }
                        }
                    });
                } else if(fragment.getClass() == PlayoffFragment.class) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            View view = fragment.getView();
                            if(view != null) {
                                ((PlayoffFragment) fragment).updateSettings(view, true, false);
                            }
                        }
                    });
                }
            }

            @Override
            public void onSettingsSynced(final boolean allowStudentsToChangeNameChanged, final boolean showAllChanged) {
                if(fragment.getClass() == SlaveFragment.class) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            View view = fragment.getView();
                            if(view != null) {
                                ((SlaveFragment) fragment).updateSettings(view, allowStudentsToChangeNameChanged, showAllChanged);
                            }
                        }
                    });
                } else if(fragment.getClass() == PitFragment.class) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            View view = fragment.getView();
                            if(view != null) {
                                ((PitFragment) fragment).updateSettings(view, allowStudentsToChangeNameChanged, showAllChanged);
                            }
                        }
                    });
                } else if(fragment.getClass() == PlayoffFragment.class) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            View view = fragment.getView();
                            if(view != null) {
                                ((PlayoffFragment) fragment).updateSettings(view, allowStudentsToChangeNameChanged, showAllChanged);
                            }
                        }
                    });
                }
            }
        }).start();

        //final ProgressBar progressBar = findViewById(R.id.progressBar);

        /*
        new Thread() {
            @Override
            public void run() {
                database.gameDataDao().fix();
            }
        }.start();
        */
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        loadFragment();
    }

    @Override
    public void onPostResume() {
        super.onPostResume();

        loadFragment();
    }

    public void loadFragment() {
        if(fragment != null) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(prefs.getBoolean("is_master_pref", false)) {
            fragment = new MasterFragment();
        } else {
            String role = prefs.getString("driver_pref", "6");
            if(role.equals("6")){
                fragment = new PitFragment();
            } else if(role.equals("7"))  {
                fragment = new AnalysisFragment();
            } else if(prefs.getBoolean("playoffs_pref", false)) {
                fragment = new PlayoffFragment();
            } else {
                fragment = new SlaveFragment();
            }
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, fragment).commit();
    }

    @Override
    public void onMenuLockChanged(boolean locked) {
        if(fragment.getClass() == SlaveFragment.class) {
            View view = fragment.getView();
            if(view != null) {
                //view.findViewById(R.id.show_all).setEnabled(!locked);
                view.findViewById(R.id.student_spinner).setEnabled(prefs.getBoolean("allow_pref", false) || !locked);
            }
        }
    }
}
