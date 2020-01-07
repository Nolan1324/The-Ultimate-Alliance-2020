package com.nolankuza.theultimatealliance.eventimport;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.nolankuza.theultimatealliance.BaseActivity;
import com.nolankuza.theultimatealliance.R;
import com.nolankuza.theultimatealliance.settings.SettingsActivity;
import com.nolankuza.theultimatealliance.model.Event;
import com.nolankuza.theultimatealliance.model.Match;
import com.nolankuza.theultimatealliance.model.Team;
import com.nolankuza.theultimatealliance.util.CSVReader;
import com.nolankuza.theultimatealliance.util.Prefs;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.nolankuza.theultimatealliance.Constants.CHOOSE_MATCHES_FILE_CODE;
import static com.nolankuza.theultimatealliance.Constants.CHOOSE_TEAMS_FILE_CODE;

public class EventImportActivity extends BaseActivity {

    private String userEventKey;
    private ArrayList<Team> userTeams = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_event_import);
        super.onCreate(savedInstanceState);

        setToolBarMenu(R.menu.toolbar_event_import);

        //Action bar
        actionBar.setSubtitle("Event Import");

        //Create a task to load events
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.INTERNET}, 0);
        }

        if(Prefs.getTeam(null) == null) {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Error");
            alertDialog.setMessage("Please configure your team number in the settings");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                            finish();
                        }
                    });
            alertDialog.show();
        }

        showProgressBar();
        new EventsTask(Integer.parseInt(Prefs.getTeam("33")), Integer.parseInt(Prefs.getYear(Integer.toString(Calendar.getInstance().get(Calendar.YEAR)))), new EventsTask.Listener() {
            @Override
            public void onTaskCompleted(List<Event> events) {
                hideProgressBar();
                RecyclerView eventRecycler = findViewById(R.id.event_recycler);
                eventRecycler.setLayoutManager(new LinearLayoutManager(EventImportActivity.this));
                eventRecycler.addItemDecoration(new DividerItemDecoration(EventImportActivity.this, DividerItemDecoration.VERTICAL));
                final EventListAdapter eventListAdapter = new EventListAdapter(EventImportActivity.this, events);
                eventListAdapter.setClickListener(new EventListAdapter.ItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getApplicationContext(), EventContentImportActivity.class);
                        intent.putExtra("event", eventListAdapter.getItem(position));
                        startActivity(intent);
                    }
                });
                eventRecycler.setAdapter(eventListAdapter);
            }
        }).execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.from_csv_button:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Enter the Event Key");
                final EditText input = new EditText(this);
                input.setHint("Found in TBA URL");
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        View view = EventImportActivity.this.getCurrentFocus();
                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                            if(imm != null) imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                        userEventKey = input.getText().toString();
                        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                        chooseFile.setType("*/*");
                        chooseFile = Intent.createChooser(chooseFile, "Choose a CSV containing teams");
                        startActivityForResult(chooseFile, CHOOSE_TEAMS_FILE_CODE);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CHOOSE_TEAMS_FILE_CODE) {
            if (resultCode == RESULT_OK) {
                userTeams = new ArrayList<>();
                new CSVReader(data, getContentResolver(), new CSVReader.Listener() {
                    @Override
                    public void onReadLine(String[] data) {
                        Team team = new Team();
                        team.teamNumber = Integer.parseInt(data[0]);
                        team.key = "frc" + team.teamNumber;
                        team.nickname = data[1];
                        userTeams.add(team);
                    }

                    @Override
                    public void onFinish() {
                        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                        chooseFile.setType("*/*");
                        chooseFile = Intent.createChooser(chooseFile, "Choose a CSV containing matches");
                        startActivityForResult(chooseFile, CHOOSE_MATCHES_FILE_CODE);
                    }
                }).execute();
            }
        } else if (requestCode == CHOOSE_MATCHES_FILE_CODE) {
            if (resultCode == RESULT_OK) {
                new CSVReader(data, getContentResolver(), new CSVReader.Listener() {
                    ArrayList<Match> userMatches = new ArrayList<>();

                    @Override
                    public void onReadLine(String[] data) {
                        Match match = new Match();
                        match.key = userEventKey + "_qm" + data[0];
                        match.matchNumber = Integer.parseInt(data[0]);
                        match.red1 = data[1];
                        match.red2 = data[2];
                        match.red3 = data[3];
                        match.blue1 = data[4];
                        match.blue2 = data[5];
                        match.blue3 = data[6];
                        userMatches.add(match);
                    }

                    @Override
                    public void onFinish() {
                        Event event = new Event();
                        event.key = userEventKey;
                        event.name = userEventKey;
                        Intent intent = new Intent(getApplicationContext(), EventContentImportActivity.class);
                        intent.putExtra("event", event);
                        intent.putParcelableArrayListExtra("teams", userTeams);
                        intent.putParcelableArrayListExtra("matches", userMatches);
                        startActivity(intent);
                    }
                }).execute();
            }
        }
    }
}
