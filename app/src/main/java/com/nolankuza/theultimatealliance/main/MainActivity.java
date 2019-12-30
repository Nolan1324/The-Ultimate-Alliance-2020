package com.nolankuza.theultimatealliance.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.nolankuza.theultimatealliance.BaseActivity;
import com.nolankuza.theultimatealliance.R;
import com.nolankuza.theultimatealliance.main.analysisplayoff.AnalysisPlayoffFragment;
import com.nolankuza.theultimatealliance.main.master.MasterFragment;
import com.nolankuza.theultimatealliance.main.scoutmatch.ScoutFragment;
import com.nolankuza.theultimatealliance.main.scoutpit.PitFragment;
import com.nolankuza.theultimatealliance.main.scoutplayoff.PlayoffFragment;
import com.nolankuza.theultimatealliance.tasks.BluetoothServerThread;

import static com.nolankuza.theultimatealliance.ApplicationState.prefs;

public class MainActivity extends BaseActivity {

    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);

        new BluetoothServerThread(getApplicationContext(), new BluetoothServerThread.Listener() {

            //region Slave devices
            @Override
            public void onMatchesSynced() {
                //TODO Bind getAll to a control

                if(fragment.getClass() == ScoutFragment.class) {
                    ((ScoutFragment) fragment).updateData();
                }
            }

            @Override
            public void onTeamsSynced() {
                if(fragment.getClass() == PitFragment.class) {
                    ((PitFragment) fragment).updateData();
                }
            }

            @Override
            public void onAssignmentSynced() {
                loadFragment();
            }

            @Override
            public void onStudentsSynced() {
                if(fragment.getClass() == ScoutFragment.class) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            View view = fragment.getView();
                            if(view != null) {
                                ((ScoutFragment) fragment).updateSettings(view, true, false);
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
                if(fragment.getClass() == ScoutFragment.class) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            View view = fragment.getView();
                            if(view != null) {
                                ((ScoutFragment) fragment).updateSettings(view, allowStudentsToChangeNameChanged, showAllChanged);
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
            //endregion

            //region Analysis devices
            @Override
            public void onPlayoffsSynced() {
                if(fragment.getClass() == AnalysisPlayoffFragment.class) {
                    View view = fragment.getView();
                    if(view != null) {
                        ((AnalysisPlayoffFragment) fragment).update(view);
                    }
                }
            }
            //endregion
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

        if(prefs.getBoolean("is_master_pref", false)) {
            fragment = new MasterFragment();
        } else {
            String role = prefs.getString("driver_pref", "6");
            if(role.equals("6")){
                fragment = new PitFragment();
            } else if(role.equals("7"))  {
                fragment = new AnalysisPlayoffFragment();
            } else if(prefs.getBoolean("playoffs_pref", false)) {
                fragment = new PlayoffFragment();
            } else {
                fragment = new ScoutFragment();
            }
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, fragment).commit();
    }

    @Override
    public void onMenuLockChanged(boolean locked) {
        if(fragment.getClass() == ScoutFragment.class) {
            View view = fragment.getView();
            if(view != null) {
                //view.findViewById(R.id.show_all).setEnabled(!locked);
                view.findViewById(R.id.student_spinner).setEnabled(prefs.getBoolean("allow_pref", false) || !locked);
            }
        }
    }
}
