package com.nolankuza.theultimatealliance.scout;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.content.res.AppCompatResources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.nolankuza.theultimatealliance.R;
import com.nolankuza.theultimatealliance.util.DataUtil;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class EndGameFragment extends Fragment {
    private GameDataListener listener;
    private ScoutBasicActivity activity;

    private Timer timer = new Timer();
    private long climbTime = 0;
    private boolean timerRunning;

    private RadioGroup level1Group;
    private RadioGroup level2Group;
    private RadioGroup level3Group;
    private RadioButton endLevel1S;
    private RadioButton endLevel1F;
    private RadioButton endLevel2S;
    private RadioButton endLevel2F;
    private RadioButton endLevel2Assisted1;
    private RadioButton endLevel2Assisted2;
    private RadioButton endLevel2WasAssisted;
    private RadioButton endLevel3S;
    private RadioButton endLevel3F;
    private RadioButton endLevel3Assisted1;
    private RadioButton endLevel3Assisted2;
    private RadioButton endLevel3WasAssisted;
    private CheckBox endLevel3FromLevel2;
    private CheckBox endLevel3SharedPlatform;
    private CheckBox endNoHabAttempt;

    RadioGroup level2Extra;
    RadioGroup level3Extra;

    private ImageButton playButton;
    private TextView timeText;
    private ImageButton resetButton;

    private Button matchSaveButton;

    private int lastLevel2ExtraSelected;
    private int lastLevel3ExtraSelected;

    public EndGameFragment() {

    }

    public static EndGameFragment newInstance() {
        EndGameFragment fragment = new EndGameFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_end_game, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        activity = (ScoutBasicActivity) getActivity();

        level1Group = view.findViewById(R.id.level_1);
        level2Group = view.findViewById(R.id.level_2);
        level3Group = view.findViewById(R.id.level_3);

        endLevel1S = view.findViewById(R.id.end_level_1_s);
        endLevel1F = view.findViewById(R.id.end_level_1_f);
        endLevel2S = view.findViewById(R.id.end_level_2_s);
        endLevel2F = view.findViewById(R.id.end_level_2_f);
        endLevel2Assisted1 = view.findViewById(R.id.end_level_2_assist_1);
        endLevel2Assisted2 = view.findViewById(R.id.end_level_2_assist_2);
        endLevel2WasAssisted = view.findViewById(R.id.end_level_2_was_assisted);
        endLevel3S = view.findViewById(R.id.end_level_3_s);
        endLevel3F = view.findViewById(R.id.end_level_3_f);
        endLevel3Assisted1 = view.findViewById(R.id.end_level_3_assist_1);
        endLevel3Assisted2 = view.findViewById(R.id.end_level_3_assist_2);
        endLevel3WasAssisted = view.findViewById(R.id.end_level_3_was_assisted);
        endLevel3FromLevel2 = view.findViewById(R.id.end_level_3_from_level_2);
        endLevel3SharedPlatform = view.findViewById(R.id.end_level_3_shared_platform);
        endNoHabAttempt = view.findViewById(R.id.end_no_hab_attempt);

        level2Extra = view.findViewById(R.id.level_2_assist);
        level3Extra = view.findViewById(R.id.level_3_assist);

        endLevel1S.setOnClickListener(new Level1Listener());
        endLevel1F.setOnClickListener(new Level1Listener());
        endLevel2S.setOnClickListener(new Level2Listener());
        endLevel2F.setOnClickListener(new Level2Listener());
        endLevel3S.setOnClickListener(new Level3Listener());
        endLevel3F.setOnClickListener(new Level3Listener());

        endLevel2Assisted1.setOnClickListener(new Level2ExtraListener());
        endLevel2Assisted2.setOnClickListener(new Level2ExtraListener());
        endLevel2WasAssisted.setOnClickListener(new Level2ExtraListener());
        endLevel3Assisted1.setOnClickListener(new Level3ExtraListener());
        endLevel3Assisted2.setOnClickListener(new Level3ExtraListener());
        endLevel3WasAssisted.setOnClickListener(new Level3ExtraListener());

        endNoHabAttempt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(endNoHabAttempt.isChecked()) {
                    clearLevel1();
                    clearLevel2();
                    clearLevel3();
                }
                matchSaveButton.setEnabled(endNoHabAttempt.isChecked());
            }
        });

        playButton = view.findViewById(R.id.playButton);
        timeText = view.findViewById(R.id.time);
        resetButton = view.findViewById(R.id.resetButton);

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                final String timeString = String.format(Locale.US, "%01d:%02d.%02d",
                        TimeUnit.MILLISECONDS.toMinutes(climbTime) % TimeUnit.HOURS.toMinutes(1),
                        TimeUnit.MILLISECONDS.toSeconds(climbTime) % TimeUnit.MINUTES.toSeconds(1),
                        (int) (((climbTime / 1000f) - Math.floor(climbTime / 1000f)) * 100));
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        timeText.setText(timeString);
                    }
                });
                if(timerRunning) {
                    climbTime += 10;
                }
            }
        }, 0, 10);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!timerRunning) {
                    playButton.setImageDrawable(AppCompatResources.getDrawable(activity, R.drawable.ic_pause));
                    timerRunning = true;
                } else {
                    playButton.setImageDrawable(AppCompatResources.getDrawable(activity, R.drawable.ic_play));
                    timerRunning = false;
                }
            }
        });
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timerRunning = false;
                climbTime = 0;
                timeText.setText("0:00.00");
                playButton.setImageDrawable(AppCompatResources.getDrawable(activity, R.drawable.ic_play));
            }
        });

        matchSaveButton = view.findViewById(R.id.match_save_button);
        matchSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View fragmentView = getView();
                if(fragmentView != null) {
                    if(!activity.isScouted()) {
                        activity.data.endLevel1S = endLevel1S.isChecked();
                        activity.data.endLevel1F = endLevel1F.isChecked();
                        activity.data.endLevel2S = endLevel2S.isChecked();
                        activity.data.endLevel2F = endLevel2F.isChecked();
                        activity.data.endLevel2Assisted = endLevel2Assisted1.isChecked() ? 1 : (endLevel2Assisted2.isChecked() ? 2 : 0);
                        activity.data.endLevel2WasAssisted = endLevel2WasAssisted.isChecked();
                        activity.data.endLevel3S = endLevel3S.isChecked();
                        activity.data.endLevel3F = endLevel3F.isChecked();
                        activity.data.endLevel3Assisted = endLevel3Assisted1.isChecked() ? 1 : (endLevel3Assisted2.isChecked() ? 2 : 0);
                        activity.data.endLevel3WasAssisted = endLevel3WasAssisted.isChecked();
                        activity.data.endLevel3FromLevel2 = endLevel3FromLevel2.isChecked();
                        activity.data.endLevel3SharedPlatform = endLevel3SharedPlatform.isChecked();
                        activity.data.endNoHabAttempt = endNoHabAttempt.isChecked();

                        activity.data.climbTime = climbTime / 1000f;
                        activity.data.totallyWorking = ((RadioButton) fragmentView.findViewById(R.id.totally_working)).isChecked();
                        activity.data.partiallyWorking = ((RadioButton) fragmentView.findViewById(R.id.partially_working)).isChecked();
                        activity.data.noShow = ((RadioButton) fragmentView.findViewById(R.id.no_show)).isChecked();
                        activity.data.beatToDeath = ((RadioButton) fragmentView.findViewById(R.id.beat_to_death)).isChecked();
                        activity.data.selfDied = ((RadioButton) fragmentView.findViewById(R.id.self_died)).isChecked();
                        activity.data.fellOver = ((RadioButton) fragmentView.findViewById(R.id.fell_over)).isChecked();
                        activity.data.pushedOver = ((RadioButton) fragmentView.findViewById(R.id.pushed_over)).isChecked();
                        activity.data.playedDefense = ((CheckBox) fragmentView.findViewById(R.id.played_defense)).isChecked();
                        activity.data.comments = DataUtil.clean(((EditText) fragmentView.findViewById(R.id.comments)).getText().toString());
                    }
                    listener.onSave();
                }
            }
        });
    }

    private class Level1Listener implements RadioButton.OnClickListener {
        @Override
        public void onClick(View view) {
            clearLevel2();
            clearLevel3();
            endNoHabAttempt.setChecked(false);
            matchSaveButton.setEnabled(true);
        }
    }

    private class Level2Listener implements RadioButton.OnClickListener {
        @Override
        public void onClick(View view) {
            clearLevel1();
            clearLevel3();
            setLevel2ExtraEnabled(true);
            endNoHabAttempt.setChecked(false);
            matchSaveButton.setEnabled(true);
        }
    }

    private class Level3Listener implements RadioButton.OnClickListener {
        @Override
        public void onClick(View view) {
            clearLevel1();
            clearLevel2();
            setLevel3ExtraEnabled(true);
            endNoHabAttempt.setChecked(false);
            matchSaveButton.setEnabled(true);
        }
    }

    private class Level2ExtraListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            int i = level2Extra.getCheckedRadioButtonId();
            if(lastLevel2ExtraSelected == i) {
                level2Extra.clearCheck();
                lastLevel2ExtraSelected = -1;
            } else {
                lastLevel2ExtraSelected = i;
                matchSaveButton.setEnabled(true);
            }
        }
    }

    private class Level3ExtraListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            int i = level3Extra.getCheckedRadioButtonId();
            if(lastLevel3ExtraSelected == i) {
                level3Extra.clearCheck();
                lastLevel3ExtraSelected = -1;
            } else {
                lastLevel3ExtraSelected = i;
                matchSaveButton.setEnabled(true);
            }
        }
    }

    private void clearLevel1() {
        endLevel1S.setChecked(false);
        endLevel1F.setChecked(false);
        level1Group.clearCheck();
    }

    private void clearLevel2() {
        endLevel2S.setChecked(false);
        endLevel2F.setChecked(false);
        endLevel2WasAssisted.setChecked(false);
        setLevel2ExtraEnabled(false);
        level2Group.clearCheck();
    }

    private void clearLevel3() {
        endLevel3S.setChecked(false);
        endLevel3F.setChecked(false);
        endLevel3WasAssisted.setChecked(false);
        endLevel3FromLevel2.setChecked(false);
        endLevel3SharedPlatform.setChecked(false);
        setLevel3ExtraEnabled(false);
        level3Group.clearCheck();
    }

    private void setLevel2ExtraEnabled(boolean enabled) {
        endLevel2WasAssisted.setEnabled(enabled);
    }

    private void setLevel3ExtraEnabled(boolean enabled) {
        endLevel3WasAssisted.setEnabled(enabled);
        endLevel3FromLevel2.setEnabled(enabled);
        endLevel3SharedPlatform.setEnabled(enabled);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof GameDataListener) {
            listener = (GameDataListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement GameDataListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
