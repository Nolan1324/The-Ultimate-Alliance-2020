package com.nolankuza.theultimatealliance.tasks;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.util.Log;

import com.nolankuza.theultimatealliance.room.Database;
import com.nolankuza.theultimatealliance.room.MatchDao;
import com.nolankuza.theultimatealliance.room.SettingsDao;
import com.nolankuza.theultimatealliance.room.TeamDao;
import com.nolankuza.theultimatealliance.structure.Assignment;
import com.nolankuza.theultimatealliance.structure.GameData;
import com.nolankuza.theultimatealliance.structure.Match;
import com.nolankuza.theultimatealliance.structure.PitData;
import com.nolankuza.theultimatealliance.structure.PlayoffData;
import com.nolankuza.theultimatealliance.structure.Settings;
import com.nolankuza.theultimatealliance.structure.Student;
import com.nolankuza.theultimatealliance.structure.Team;
import com.nolankuza.theultimatealliance.util.Binary;
import com.nolankuza.theultimatealliance.util.Sync;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static com.nolankuza.theultimatealliance.ApplicationState.database;
import static com.nolankuza.theultimatealliance.ApplicationState.prefs;
import static com.nolankuza.theultimatealliance.util.Constants.BLUETOOTH_UUID;

public class BluetoothServerThread extends Thread {
    private final WeakReference<Context> context;
    private Listener listener;

    public BluetoothServerThread(Context context, Listener listener) {
        this.context = new WeakReference<>(context);
        this.listener = listener;
    }

    public interface Listener {
        void onTaskInit();
        void onMatchesSynced();
        void onTeamsSynced();
        void onAssignmentSynced();
        void onStudentsSynced();
        void onSettingsSynced(boolean allowStudentsToChangeNameChanged, boolean showAllChanged);
        void onPlayoffsSynced();
    }

    public void run() {
        boolean gameDataSynced = false;
        BluetoothServerSocket serverSocket;
        BluetoothAdapter bAdapter = BluetoothAdapter.getDefaultAdapter();
        try {
            while(!isInterrupted()) {
                serverSocket = bAdapter.listenUsingRfcommWithServiceRecord("TUA_Data_Sync", BLUETOOTH_UUID);
                BluetoothSocket socket;
                try {
                    socket = serverSocket.accept();

                    InputStream is = socket.getInputStream();
                    OutputStream os = socket.getOutputStream();

                    int directionCode = is.read();
                    boolean isMore = false;
                    if(directionCode == 0) {
                        //If it is a data request
                        isMore = is.read() == 1;

                        os.write(1); //Start the response
                        int dataCode = is.read();
                        List<Integer> dataCodes = new ArrayList<>();
                        while(dataCode != 0) {
                            dataCodes.add(dataCode);
                            dataCode = is.read();
                        }
                        for(int i = 0; i < dataCodes.size(); i++) {
                            dataCode = dataCodes.get(i);
                            if (dataCode == Sync.GAMEDATA) {
                                List<GameData> list = database.gameDataDao().getAll();
                                os.write(Binary.createSendData(list, i != dataCodes.size() - 1, dataCode));
                                gameDataSynced = true;
                            } else if (dataCode == Sync.PITDATA) {
                                List<PitData> list = database.pitDataDao().getScouted();
                                os.write(Binary.createSendData(list, i != dataCodes.size() - 1, dataCode));
                            } else if (dataCode == Sync.PLAYOFFDATA) {
                                List<PlayoffData> list = database.playoffDataDao().getAll();
                                os.write(Binary.createSendData(list, i != dataCodes.size() - 1, dataCode));
                            }
                        }
                    }

                    if(directionCode == 1 || isMore) {
                        if(isMore) {
                            is.skip(1); //Read the direction code, since it has not been read yet
                        }

                        int dataHasMore = 1;
                        while(dataHasMore == 1) {
                            dataHasMore = is.read();
                            int dataCode = is.read();
                            byte[] lengthBytes = new byte[4];
                            is.read(lengthBytes);
                            ByteBuffer lengthWrapped = ByteBuffer.wrap(lengthBytes);
                            switch (dataCode) {
                                case Sync.MATCHES:
                                    List<Match> matches = Binary.unmarshall(Binary.readBytes(is, lengthWrapped.getInt()), Match.class, Match.CREATOR);
                                    MatchDao matchDao = database.matchDao();
                                    //TODO Do we want this?
                                    //matchDao.deleteAll();
                                    matchDao.addAll(matches);
                                    if(matches.size() != 0) {
                                        updateEventKey(matches.get(0).key);
                                    }
                                    listener.onMatchesSynced();
                                    break;
                                case Sync.TEAMS:
                                    List<Team> teams = Binary.unmarshall(Binary.readBytes(is, lengthWrapped.getInt()), Team.class, Team.CREATOR);
                                    TeamDao teamDao = database.teamDao();
                                    teamDao.deleteAll();
                                    teamDao.insertAll(teams);
                                    //region Event Key
                                    byte[] keyLengthBytes = new byte[4];
                                    is.read(keyLengthBytes);
                                    ByteBuffer keyLengthBytesWrapped = ByteBuffer.wrap(keyLengthBytes);
                                    byte[] eventKeyBytes = new byte[keyLengthBytesWrapped.getInt()];
                                    is.read(eventKeyBytes);
                                    updateEventKey(Arrays.toString(keyLengthBytes));
                                    //endregion
                                    listener.onTeamsSynced();
                                    break;
                                case Sync.ASSIGNMENTS:
                                    Assignment assignment = Binary.unmarshall(Binary.readBytes(is, lengthWrapped.getInt()), Assignment.class, Assignment.CREATOR).get(0);
                                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context.get()).edit();
                                    editor.putString("driver_pref", Byte.toString(assignment.role));
                                    editor.putString("student_pref", assignment.student);
                                    editor.putBoolean("playoffs_pref", assignment.playoffs);
                                    editor.putInt("team_portion", assignment.teamPortionAssignment);
                                    editor.putInt("total_pit_scouters", assignment.totalPitScouters);
                                    editor.apply();
                                    listener.onAssignmentSynced();
                                    break;
                                case Sync.STUDENTS:
                                    List<Student> students = Binary.unmarshall(Binary.readBytes(is, lengthWrapped.getInt()), Student.class, Student.CREATOR);
                                    database.studentDao().deleteAll();
                                    database.studentDao().insertAll(students);
                                    //TODO Change name
                                    listener.onStudentsSynced();
                                    break;
                                case Sync.SETTINGS:
                                    Settings settings = Binary.unmarshall(Binary.readBytes(is, lengthWrapped.getInt()), Settings.class, Settings.CREATOR).get(0);
                                    boolean allowStudentsToChangeNameChanged = prefs.getBoolean("allow_pref", false) != settings.allowStudentsToChangeName;
                                    boolean showAllChanged = prefs.getBoolean("show_all_pref", false) != settings.showAll;
                                    prefs.edit().putBoolean("allow_pref", settings.allowStudentsToChangeName).apply();
                                    prefs.edit().putBoolean("show_all_pref", settings.showAll).apply();
                                    prefs.edit().putBoolean("field_reverse_pref", settings.fieldReverse).apply();
                                    SettingsDao settingsDao = database.settingsDao();
                                    settingsDao.update(settings);
                                    listener.onSettingsSynced(allowStudentsToChangeNameChanged, showAllChanged);
                                    break;
                                case Sync.PLAYOFFDATA:
                                    List<PlayoffData> playoffDataList = Binary.unmarshall(Binary.readBytes(is, lengthWrapped.getInt()), PlayoffData.class, PlayoffData.CREATOR);
                                    Log.d("HELLO", playoffDataList.size() + "");
                                    database.playoffDataDao().insert(playoffDataList);
                                    listener.onPlayoffsSynced();
                                    break;
                            }
                        }
                    }

                    socket.close();
                    serverSocket.close();

                    //TODO Move
                    //Mark all as synced if the syncing is successful
                    if(gameDataSynced) database.gameDataDao().syncAll();
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateEventKey(String eventKey) {
        SettingsDao settingsDao = database.settingsDao();
        Settings settings = settingsDao.get();
        settings.eventKey = eventKey;
        settingsDao.update(settings);
    }
}