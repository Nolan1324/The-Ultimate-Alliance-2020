package com.nolankuza.theultimatealliance.datasync;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.text.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

import static com.nolankuza.theultimatealliance.ApplicationState.database;
import static com.nolankuza.theultimatealliance.ApplicationState.prefs;

public class ExportTask extends AsyncTask<Void, Void, String> {
    private final WeakReference<Context> context;
    private SyncOptions options;
    private Listener listener;

    public ExportTask(Context context, SyncOptions syncOptions, Listener listener) {
        this.context = new WeakReference<>(context);
        this.options = syncOptions;
        this.listener = listener;
    }

    interface Listener {
        void onTaskCompleted(String file);
    }

    @Override
    public String doInBackground(Void... voids) {
        Cursor cursor = options.game ? database.gameDataDao().getCursor() : database.pitDataDao().getCursor();
        int colCount = cursor.getColumnCount();
        int scoutedIndex = Arrays.asList(cursor.getColumnNames()).indexOf("scouted");
        if(scoutedIndex == -1) {
            return "ERROR";
        }
        ArrayList<String> colHeaders = new ArrayList<>(Arrays.asList(cursor.getColumnNames()));
        colHeaders.remove(scoutedIndex + 1 /*Synced index*/);

        FileOutputStream os = null;
        try {
            //TODO Make more concise
            File dir = new File(context.get().getExternalFilesDir(null), options.game ? "game/" : "pit/");
            if(!dir.exists()) {
                dir.mkdir();
            }
            if(options.game && prefs.getString("game_timestamp", null) != null && new File(dir, ("GameData.csv")).exists()) {
                new File(dir, ("GameData.csv")).renameTo(new File(dir, ("GameData_" + prefs.getString("game_timestamp", null) + ".csv")));
            } else if(options.pit && prefs.getString("pit_timestamp", null) != null && new File(dir, ("PitData.csv")).exists()) {
                new File(dir, ("PitData.csv")).renameTo(new File(dir, ("PitData_" + prefs.getString("pit_timestamp", null) + ".csv")));
            }
            prefs.edit().putString(options.game ? "game_timestamp" : "pit_timestamp", timeStamp()).apply();

            File file = new File(dir, (options.game ? "GameData" : "PitData") + ".csv");
            //file.createNewFile();
            os = new FileOutputStream(file);
            os.write(TextUtils.join(",", colHeaders).getBytes());
            exportCursor(cursor, colCount, scoutedIndex, os);
            if(options.game) {
                exportCursor(database.gameDataDao().getPlayoffCursor(), colCount, scoutedIndex, os);
            }

            return file.getAbsolutePath().replace("/storage/emulated/0/", "");
        } catch (IOException e) {
            e.printStackTrace();
            return "ERROR";
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onPostExecute(String file) {
        listener.onTaskCompleted(file);
    }

    private static boolean fieldIsMeta(String fieldName) {
        return fieldName.equals("$change") || fieldName.equals("serialVersionUID");
    }

    private static void exportCursor(Cursor cursor, int colCount, int scoutedIndex, OutputStream os) throws IOException {
        if(cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                StringBuilder builder = new StringBuilder();
                //TODO Make scouted index dynamic
                for(int i = 0; i < scoutedIndex + 1; i++) {
                    builder.append(cursor.getString(i)).append(",");
                }
                if(cursor.getInt(scoutedIndex) == 1) {
                    //Continues at scoutedIndex + 2, excludes "synced" field
                    for(int i = scoutedIndex + 2; i < colCount; i++) {
                        builder.append(cursor.getString(i)).append(",");
                    }
                }
                builder.delete(builder.length() - 1, builder.length());
                os.write(("\n" + builder.toString()).getBytes());
                cursor.moveToNext();
            }
        }
    }

    private static String timeStamp() {
        SimpleDateFormat fmt = new SimpleDateFormat("MM-dd-yyyy hh-mm-ss aa", Locale.US);
        return fmt.format(Calendar.getInstance().getTime());
    }
}