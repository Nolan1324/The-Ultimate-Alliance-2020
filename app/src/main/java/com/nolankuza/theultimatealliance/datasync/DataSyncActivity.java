package com.nolankuza.theultimatealliance.datasync;

import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.nolankuza.theultimatealliance.BaseActivity;
import com.nolankuza.theultimatealliance.MessageLogger;
import com.nolankuza.theultimatealliance.R;
import com.nolankuza.theultimatealliance.databinding.ActivityDataSyncBinding;
import com.nolankuza.theultimatealliance.model.DeviceInfo;
import com.nolankuza.theultimatealliance.tasks.BluetoothClientTask;
import com.nolankuza.theultimatealliance.util.Prefs;
import com.nolankuza.theultimatealliance.util.Resources;
import com.nolankuza.theultimatealliance.util.Sync;

import java.util.List;

public class DataSyncActivity extends BaseActivity implements MessageLogger {

    private BluetoothAdapter bAdapter;
    private TabletListAdapter tabletListAdapter;
    private LinearLayout dataLog;

    private int syncingState;
    private BluetoothClientTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        boolean isMaster = Prefs.getIsMaster(false);

        setToolBarMenu(isMaster ? R.menu.toolbar_sync : R.menu.toolbar_sync_slave);
        ActivityDataSyncBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_data_sync);
        super.onCreate(savedInstanceState);
        binding.setHandler(new Handler());

        //Get info for bluetooth devices
        bAdapter = BluetoothAdapter.getDefaultAdapter();
        List<DeviceInfo> deviceInfos = Sync.getDeviceInfos(bAdapter);

        //Set up the bluetooth device recycler
        if(isMaster) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            RecyclerView tabletRecycler = findViewById(R.id.tablet_recycler);
            tabletRecycler.setLayoutManager(layoutManager);
            tabletListAdapter = new TabletListAdapter(this, deviceInfos);
            tabletRecycler.setAdapter(tabletListAdapter);
        } else {
            RecyclerView tabletRecycler = findViewById(R.id.tablet_recycler);
            tabletRecycler.setVisibility(View.GONE);
            findViewById(R.id.devices_to_sync).setVisibility(View.GONE);
        }

        //Resize the left pane according to the amount of devices
        if(isMaster) {
            DisplayMetrics displaymetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

            View dataSyncPane = findViewById(R.id.data_sync_pane);
            ViewGroup.LayoutParams p = dataSyncPane.getLayoutParams();
            int devicesWidth = (Resources.getDimen(this, R.dimen.data_button) + 5) * (tabletListAdapter.getItemCount() + 1);
            int halfWidth = displaymetrics.widthPixels / 2;
            p.width = Math.max(devicesWidth, halfWidth);
            dataSyncPane.setLayoutParams(p);
        }

        dataLog = findViewById(R.id.data_sync_log);

        ((ToggleButton)findViewById(R.id.toggle_sync_game)).setChecked(getIntent().getBooleanExtra("gamedata", false));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final SyncOptions options = getSyncOptions();

        switch (item.getItemId()) {
            case R.id.sync_button:
                if(syncingState == 0) {
                    dataLog.removeAllViewsInLayout();

                    if(!(options.event || options.teams || options.assignments || options.students || options.settings || options.game || options.pit || options.playoffs)) {
                        broadcast("Please select at least one type of data to sync", Color.RED);
                        return true;
                    }

                    syncingState = 1;
                    task = new BluetoothClientTask(this, bAdapter, options, tabletListAdapter.getDeviceInfos(),this, new BluetoothClientTask.Listener() {
                        @Override
                        public void onTaskCompleted(Boolean allSynced) {
                            //TODO Does this change if options changed while the task is running?
                            if(allSynced) {
                                ((GroupToggle) findViewById(R.id.devices_to_sync)).setChecked(true);
                            }
                            if(options.settings) {
                                Prefs.setSyncedSettings(true);
                            }
                            syncingState = 0;
                            if(options.game || options.pit) {
                                export(options);
                            }
                        }
                    });
                    task.execute();
                } else if(syncingState == 1) {
                    broadcast("A syncing process is currently running. Click again to force stop.", Color.RED);
                    syncingState = 2;
                } else {
                    task.cancel(true);
                    broadcast("Syncing terminated.", Color.RED);
                    syncingState = 0;
                }
                return true;

            case R.id.export_button:
                if(options.game || options.pit) {
                    export(options);
                } else {
                    broadcast("Please select at least one type of data to export", Color.RED);
                }

                return true;

            case R.id.delete_data_button:
                if(!(options.event || options.teams || options.assignments || options.students || options.settings || options.game || options.pit || options.playoffs)) {
                    broadcast("Please select at least one type of data to delete", Color.RED);
                    return true;
                }

                final AlertDialog.Builder builder = new AlertDialog.Builder(DataSyncActivity.this);
                builder.setTitle("WARNING");
                builder.setMessage("ALL selected data will be permanently DELETED from this device. Continue?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new DeleteTask(getApplicationContext(), options).execute();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.create().show();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void export(SyncOptions options) {
        new ExportTask(getApplicationContext(), options, new ExportTask.Listener() {
            @Override
            public void onTaskCompleted(String file) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DataSyncActivity.this);
                builder.setTitle("Success");
                builder.setPositiveButton("OK", null);
                builder.setMessage("Exported data to " + file);
                builder.create().show();
            }
        }).execute();
    }

    public SyncOptions getSyncOptions() {
        SyncOptions options = new SyncOptions();
        options.event = ((ToggleButton) findViewById(R.id.toggle_sync_event)).isChecked();
        options.teams = ((ToggleButton) findViewById(R.id.toggle_sync_teams)).isChecked();
        options.assignments = ((ToggleButton) findViewById(R.id.toggle_sync_assignments)).isChecked();
        options.students = ((ToggleButton) findViewById(R.id.toggle_sync_students)).isChecked();
        options.settings = ((ToggleButton) findViewById(R.id.toggle_sync_settings)).isChecked();
        options.game = ((ToggleButton) findViewById(R.id.toggle_sync_game)).isChecked();
        options.pit = ((ToggleButton) findViewById(R.id.toggle_sync_pits)).isChecked();
        options.playoffs = ((ToggleButton) findViewById(R.id.toggle_sync_playoffs)).isChecked();
        return options;
    }

    @Override
    public void broadcast(String message, int color) {
        appendLog(message, color);
    }

    public void appendLog(String message, int color) {
        TextView textView = new TextView(this);
        ViewGroup.LayoutParams p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(p);
        textView.setText(message);
        textView.setTextColor(color);

        dataLog.addView(textView);
    }

    @Override
    public void setDeviceToggle(int position, boolean toggled) {
        ((ToggleButton)((RecyclerView)findViewById(R.id.tablet_recycler)).getChildAt(position)).setChecked(toggled);
    }

    public class Handler {
        public void onDataClicked(View view) {
            //TODO Better way to do this (is annoying when deleting data)
            /*
            ToggleButton toggleButton = (ToggleButton) view;
            boolean toggled = toggleButton.isChecked();
            ((ToggleButton) findViewById(R.id.toggle_sync_game)).setChecked(false);
            ((ToggleButton) findViewById(R.id.toggle_sync_pits)).setChecked(false);
            ((ToggleButton) findViewById(R.id.toggle_sync_playoffs)).setChecked(false);
            toggleButton.setChecked(toggled);
            */
        }
    }
}