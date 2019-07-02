package com.nolankuza.theultimatealliance.tasks;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Parcel;
import android.preference.PreferenceManager;
import android.util.Log;

import com.nolankuza.theultimatealliance.MessageLogger;
import com.nolankuza.theultimatealliance.datasync.SyncOptions;
import com.nolankuza.theultimatealliance.room.AppDatabase;
import com.nolankuza.theultimatealliance.room.Database;
import com.nolankuza.theultimatealliance.structure.Assignment;
import com.nolankuza.theultimatealliance.structure.DeviceInfo;
import com.nolankuza.theultimatealliance.structure.Event;
import com.nolankuza.theultimatealliance.structure.GameData;
import com.nolankuza.theultimatealliance.structure.Match;
import com.nolankuza.theultimatealliance.structure.PitData;
import com.nolankuza.theultimatealliance.structure.PlayoffData;
import com.nolankuza.theultimatealliance.structure.Settings;
import com.nolankuza.theultimatealliance.structure.Team;
import com.nolankuza.theultimatealliance.util.Binary;
import com.nolankuza.theultimatealliance.util.Constants;
import com.nolankuza.theultimatealliance.util.Sync;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.nolankuza.theultimatealliance.ApplicationState.database;
import static com.nolankuza.theultimatealliance.ApplicationState.prefs;
import static com.nolankuza.theultimatealliance.util.Constants.BLUETOOTH_UUID;

public class BluetoothClientTask extends AsyncTask<Void, Void, Boolean> {
    private WeakReference<Context> context;
    private Listener listener;
    private BluetoothAdapter bAdapter;
    private SyncOptions options;
    private List<DeviceInfo> deviceInfos;
    private List<BluetoothDevice> analysisDevices = new ArrayList<>();
    private MessageLogger messageLogger;
    private boolean allSynced = true;

    public BluetoothClientTask(Context context, BluetoothAdapter bAdapter, SyncOptions options, List<DeviceInfo> deviceInfos, MessageLogger messageLogger, Listener listener) {
        this.context = new WeakReference<>(context);
        this.bAdapter = bAdapter;
        this.options = options;
        this.deviceInfos = deviceInfos;
        this.messageLogger = messageLogger;
        this.listener = listener;
    }

    public interface Listener {
        void onTaskInit();
        void onTaskCompleted(Boolean allSynced);
    }

    @Override
    protected void onPreExecute() {
        listener.onTaskInit();
    }

    @Override
    public Boolean doInBackground(Void... voids) {
        byte[] eventKey = database.settingsDao().get().eventKey.getBytes();
        List<Match> matches = database.matchDao().getAll();
        List<Team> teams = database.teamDao().getAll();
        List<PlayoffData> playoffs = database.playoffDataDao().getAll();
        List<Settings> settings = new ArrayList<>();
        settings.add(database.settingsDao().get());
        int currentPitScouter = 1;
        int totalPitScouters = database.assignmentDao().getTotalPitScouters();

        List<BluetoothDevice> devices = Sync.getDevices(bAdapter);
        for(int i = 0; i < deviceInfos.size(); i++) {
            if(deviceInfos.get(i).isEnabled()) {
                BluetoothDevice device = devices.get(i);

                List<Assignment> assignments = database.assignmentDao().getByName(device.getName());
                if(assignments.get(0).role == 7 && options.playoffs) {
                    analysisDevices.add(device);
                    continue;
                }

                try {
                    broadcast(String.format("Attempting connection to %s.", device.getName()), Color.BLACK);

                    BluetoothSocket socket = device.createRfcommSocketToServiceRecord(BLUETOOTH_UUID);
                    socket.connect();

                    InputStream is = socket.getInputStream();
                    OutputStream os = socket.getOutputStream();

                    boolean requestData = options.game || options.pit || options.playoffs;
                    boolean sendData = options.event || options.teams || options.assignments || options.students || options.settings;
                    if(requestData) {
                        os.write(0); //Header: request data
                        os.write(sendData ? 1 : 0); //Will there be more data
                        if(options.game) {
                            os.write(Sync.GAMEDATA);
                        }
                        if(options.pit) {
                            os.write(Sync.PITDATA);
                        }
                        if(options.playoffs) {
                            os.write(Sync.PLAYOFFDATA);
                        }
                        os.write(0);

                        if(is.read() == 1) {
                            int dataHasMore = 1;
                            while(dataHasMore == 1) {
                                dataHasMore = is.read();
                                int dataCode = is.read();
                                byte[] lengthBytes = new byte[4];
                                is.read(lengthBytes);
                                ByteBuffer lengthWrapped = ByteBuffer.wrap(lengthBytes);
                                switch (dataCode) {
                                    case Sync.GAMEDATA:
                                        List<GameData> gameDataList = Binary.unmarshall(Binary.readBytes(is, lengthWrapped.getInt()), GameData.class, GameData.CREATOR);
                                        database.gameDataDao().insert(gameDataList);
                                        broadcast(String.format("Synced " + gameDataList.size() + " matches from %s.", device.getName()), Color.GREEN);
                                        break;
                                    case Sync.PITDATA:
                                        List<PitData> pitDataList = Binary.unmarshall(Binary.readBytes(is, lengthWrapped.getInt()), PitData.class, PitData.CREATOR);
                                        database.pitDataDao().insert(pitDataList);
                                        broadcast(String.format("Synced " + pitDataList.size() + " teams from %s.", device.getName()), Color.GREEN);
                                        break;
                                    case Sync.PLAYOFFDATA:
                                        List<PlayoffData> playoffDataList = Binary.unmarshall(Binary.readBytes(is, lengthWrapped.getInt()), PlayoffData.class, PlayoffData.CREATOR);
                                        database.playoffDataDao().insert(playoffDataList);
                                        broadcast(String.format("Synced " + playoffDataList.size() + " playoff matches from %s.", device.getName()), Color.GREEN);
                                        break;
                                }
                            }
                        }
                    }

                    if(sendData) {
                        os.write(1); //Header: sending data
                        if(options.event) {
                            Binary.writeBytes(os, Binary.createSendData(matches, options.teams || (options.assignments && assignments.size() != 0), Sync.MATCHES));
                        }
                        if(options.teams) {
                            Binary.writeBytes(os, Binary.createSendData(teams, (options.assignments && assignments.size() != 0) || options.settings, Sync.TEAMS));
                            //region Event Key
                            ByteBuffer lengthBuffer = ByteBuffer.allocate(4);
                            lengthBuffer.putInt(eventKey.length);
                            Binary.writeBytes(os, lengthBuffer.array());
                            Binary.writeBytes(os, eventKey);
                            //endregion
                        }
                        if(options.settings) {
                            Binary.writeBytes(os, Binary.createSendData(settings, options.assignments && assignments.size() != 0, Sync.SETTINGS));
                        }
                        if(options.assignments) {
                            if(assignments.size() != 0) {
                                Assignment assignment = assignments.get(0);
                                assignment.teamPortionAssignment = currentPitScouter;
                                assignment.totalPitScouters = totalPitScouters;
                                currentPitScouter++;
                                assignments.set(0, assignment);
                                Binary.writeBytes(os, Binary.createSendData(assignments, options.students, Sync.ASSIGNMENTS));
                                prefs.edit().putBoolean(Constants.PREF_PIT_ASSIGN_CHANGED, false).apply();
                                prefs.edit().putBoolean(Constants.PREF_SCOUT_ASSIGN_CHANGED, false).apply();
                            } else {
                                broadcast(String.format("Warning: no assignment exists for %s", device.getName()), Color.rgb(127, 127, 0));
                            }
                        }
                        if(options.students) {
                            Binary.writeBytes(os, Binary.createSendData(database.studentDao().getAll(), false, Sync.STUDENTS));
                        }
                    }

                    //socket.close();

                    if(socket.isConnected()) {
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    broadcast(String.format("Syncing with %s completed.", device.getName()), Color.GREEN);
                    setDeviceToggle(i, false);
                } catch (IOException e) {
                    broadcast(e.getLocalizedMessage(), Color.RED);
                    allSynced = false;
                }
            }
        }

        for(BluetoothDevice device : analysisDevices) {
            try {
                broadcast(String.format("Attempting connection to %s.", device.getName()), Color.BLACK);

                BluetoothSocket socket = device.createRfcommSocketToServiceRecord(BLUETOOTH_UUID);
                socket.connect();

                InputStream is = socket.getInputStream();
                OutputStream os = socket.getOutputStream();

                os.write(1); //Header: sending data

                playoffs = database.playoffDataDao().getAll();
                Binary.writeBytes(os, Binary.createSendData(playoffs, false, Sync.PLAYOFFDATA));

                if (socket.isConnected()) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                broadcast(String.format("Syncing with %s completed.", device.getName()), Color.GREEN);
            } catch (IOException e) {
                broadcast(e.getLocalizedMessage(), Color.RED);
                allSynced = false;
            }
        }

        return allSynced;
    }

    @Override
    public void onPostExecute(Boolean allSynced) {
        listener.onTaskCompleted(allSynced);
    }

    private void broadcast(final String message, final int color) {
        ((Activity) context.get()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageLogger.broadcast(message, color);
            }
        });
    }

    private void setDeviceToggle(final int position, final boolean toggled) {
        ((Activity) context.get()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageLogger.setDeviceToggle(position, toggled);
            }
        });
    }
}
