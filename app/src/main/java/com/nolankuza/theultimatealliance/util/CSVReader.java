package com.nolankuza.theultimatealliance.util;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CSVReader {

    private Intent data;
    private ContentResolver contentResolver;
    private Listener listener;

    public CSVReader(Intent data, ContentResolver contentResolver, Listener listener) {
        this.data = data;
        this.contentResolver = contentResolver;
        this.listener = listener;
    }

    public void execute() {
        try {
            Uri uri = data.getData();
            if(uri != null) {
                InputStream inputStream = contentResolver.openInputStream(uri);
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                reader.readLine();
                String line = "";
                while((line = reader.readLine()) != null) {
                    String[] lineData = line.split(",");
                    listener.onReadLine(lineData);
                }
                reader.close();
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        listener.onFinish();
    }

    public interface Listener {
        void onReadLine(String[] data);
        void onFinish();
    }
}
