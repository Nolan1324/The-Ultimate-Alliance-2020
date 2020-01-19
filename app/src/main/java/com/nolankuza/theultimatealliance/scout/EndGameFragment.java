package com.nolankuza.theultimatealliance.scout;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.content.res.AppCompatResources;
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

import java.util.ArrayList;
import java.util.List;

public class EndGameFragment extends Fragment {
    private GameDataListener listener;
    private ScoutBasicActivity activity;

    private Drawable starDrawable;
    private Drawable starOnDrawable;

    private List<ImageButton> defenseButtons = new ArrayList<>();
    private TextView defenseRatingTextView;
    private View defenseRatingView;
    private boolean displayDefense;

    private CheckBox endOnPlatform;
    private TimerButton hangTimer;
    private CheckBox endHangF;

    private int lastHangAssistSelected;
    private RadioGroup hangAssist;
    private RadioButton endHangAssist1;
    private RadioButton endHangAssist2;
    private RadioButton endHangWasAssisted;

    private CheckBox endHangLevel;
    private ToggleButton endHangSharedRung0;
    private ToggleButton endHangSharedRung1;
    private ToggleButton endHangSharedRung2;

    private CheckBox endNoRendezvousAttempt;

    private Button matchSaveButton;

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

        defenseButtons.add((ImageButton)view.findViewById(R.id.star_1));
        defenseButtons.add((ImageButton)view.findViewById(R.id.star_2));
        defenseButtons.add((ImageButton)view.findViewById(R.id.star_3));
        defenseButtons.add((ImageButton)view.findViewById(R.id.star_4));
        defenseButtons.add((ImageButton)view.findViewById(R.id.star_5));
        starDrawable = AppCompatResources.getDrawable(activity, R.drawable.ic_star_border);
        starOnDrawable = AppCompatResources.getDrawable(activity, R.drawable.ic_star);
        new CounterListHandler(defenseButtons, starDrawable, starOnDrawable, new CounterListHandler.Listener() {
            @Override
            public void onValueChange(int newValue) {
                activity.data.defenseAbility = newValue;
            }
        });

        defenseRatingTextView = view.findViewById(R.id.defense_rating_text);
        defenseRatingView = view.findViewById(R.id.defense_rating);
        displayDefense(false);

        endOnPlatform = view.findViewById(R.id.end_on_platform);
        hangTimer = view.findViewById(R.id.hang_timer);
        endHangF = view.findViewById(R.id.end_hang_f);

        hangAssist = view.findViewById(R.id.hang_assist);
        endHangAssist1 = view.findViewById(R.id.end_hang_assist_1);
        endHangAssist2 = view.findViewById(R.id.end_hang_assist_2);
        endHangWasAssisted = view.findViewById(R.id.end_hang_was_assisted);
        endHangLevel = view.findViewById(R.id.end_hang_level);
        endNoRendezvousAttempt = view.findViewById(R.id.end_no_rendezvous_attempt);

        endHangLevel = view.findViewById(R.id.end_hang_level);
        endHangSharedRung0 = view.findViewById(R.id.end_hang_shared_rung_0);
        endHangSharedRung1 = view.findViewById(R.id.end_hang_shared_rung_1);
        endHangSharedRung2 = view.findViewById(R.id.end_hang_shared_rung_2);

        HangAssistListener hangAssistListener = new HangAssistListener();
        endHangAssist1.setOnClickListener(hangAssistListener);
        endHangAssist2.setOnClickListener(hangAssistListener);
        endHangWasAssisted.setOnClickListener(hangAssistListener);

        endOnPlatform.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                enableHanging(isChecked);
            }
        });

        endNoRendezvousAttempt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(endNoRendezvousAttempt.isChecked()) {
                    endOnPlatform.setChecked(false);
                    hangTimer.resetTimer();
                    endHangF.setChecked(false);
                    endHangAssist1.setChecked(false);
                    endHangAssist2.setChecked(false);
                    endHangWasAssisted.setChecked(false);
                    endHangLevel.setChecked(false);
                    sharedRungButtonsOff();
                    endHangSharedRung0.setChecked(true);
                    endOnPlatform.setEnabled(false);
                    enableHanging(false);
                } else {
                    endOnPlatform.setEnabled(true);
                }
                matchSaveButton.setEnabled(endNoRendezvousAttempt.isChecked());
            }
        });

        endHangSharedRung0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedRungButtonsOff();
                ((ToggleButton)view).setChecked(true);
                activity.data.endHangSharedRung = 0;
            }
        });
        endHangSharedRung1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedRungButtonsOff();
                ((ToggleButton)view).setChecked(true);
                activity.data.endHangSharedRung = 1;
            }
        });
        endHangSharedRung2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedRungButtonsOff();
                ((ToggleButton)view).setChecked(true);
                activity.data.endHangSharedRung = 2;
            }
        });

        matchSaveButton = view.findViewById(R.id.match_save_button);
        matchSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View fragmentView = getView();
                if(fragmentView != null) {
                    if(!activity.isScouted()) {
                        activity.data.endPark = endOnPlatform.isChecked() && !hangTimer.hasRan();
                        activity.data.endHangF = endHangF.isChecked();
                        activity.data.endHangAssisted = endHangAssist1.isChecked() ? 1 : (endHangAssist2.isChecked() ? 2 : 0);
                        activity.data.endHangWasAssisted = endHangWasAssisted.isChecked();
                        activity.data.endHangLevel = endHangLevel.isChecked();

                        activity.data.totallyWorking = ((RadioButton) fragmentView.findViewById(R.id.totally_working)).isChecked();
                        activity.data.partiallyWorking = ((RadioButton) fragmentView.findViewById(R.id.partially_working)).isChecked();
                        activity.data.noShow = ((RadioButton) fragmentView.findViewById(R.id.no_show)).isChecked();
                        activity.data.beatToDeath = ((RadioButton) fragmentView.findViewById(R.id.beat_to_death)).isChecked();
                        activity.data.selfDied = ((RadioButton) fragmentView.findViewById(R.id.self_died)).isChecked();
                        activity.data.fellOver = ((RadioButton) fragmentView.findViewById(R.id.fell_over)).isChecked();
                        activity.data.pushedOver = ((RadioButton) fragmentView.findViewById(R.id.pushed_over)).isChecked();
                        activity.data.comments = DataUtil.clean(((EditText) fragmentView.findViewById(R.id.comments)).getText().toString());
                    }
                    listener.onSave();
                }
            }
        });
    }

    public void displayDefense(boolean display) {
        int visibility = display ? View.VISIBLE : View.GONE;
        if(defenseRatingTextView != null) defenseRatingTextView.setVisibility(visibility);
        if(defenseRatingView != null) defenseRatingView.setVisibility(visibility);
        displayDefense = display;
    }

    private void enableHanging(boolean enable) {
        hangTimer.setEnabled(enable);
        endHangF.setEnabled(enable);
        endHangAssist1.setEnabled(enable);
        endHangAssist2.setEnabled(enable);
        endHangWasAssisted.setEnabled(enable);
        endHangLevel.setEnabled(enable);
        endHangSharedRung0.setEnabled(enable);
        endHangSharedRung1.setEnabled(enable);
        endHangSharedRung2.setEnabled(enable);
    }

    private void sharedRungButtonsOff() {
        endHangSharedRung0.setChecked(false);
        endHangSharedRung1.setChecked(false);
        endHangSharedRung2.setChecked(false);
    }

    private class HangAssistListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            int i = hangAssist.getCheckedRadioButtonId();
            if(lastHangAssistSelected == i) {
                hangAssist.clearCheck();
                lastHangAssistSelected = -1;
            } else {
                lastHangAssistSelected = i;
                matchSaveButton.setEnabled(true);
            }
        }
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
