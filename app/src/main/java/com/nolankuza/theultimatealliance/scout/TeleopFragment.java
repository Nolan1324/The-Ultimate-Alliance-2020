package com.nolankuza.theultimatealliance.scout;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.nolankuza.theultimatealliance.R;
import com.nolankuza.theultimatealliance.model.Alliance;

public class TeleopFragment extends Fragment implements TimerButton.Listener {
    private GameDataListener listener;
    private ScoutBasicActivity activity;

    String team1 = "";
    String team2 = "";
    String team3 = "";

    TimerButton defenseTimer1;
    TimerButton defenseTimer2;
    TimerButton defenseTimer3;

    public TeleopFragment() {

    }

    public static TeleopFragment newInstance() {
        TeleopFragment fragment = new TeleopFragment();
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
        return inflater.inflate(R.layout.fragment_teleop, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        activity = (ScoutBasicActivity) getActivity();
        if(activity != null) {
            if (activity.gameData.alliance == Alliance.Red) {
                team1 = activity.match.blue1;
                team2 = activity.match.blue2;
                team3 = activity.match.blue3;
            } else {
                team1 = activity.match.red1;
                team2 = activity.match.red2;
                team3 = activity.match.red3;
            }
            /*((TimerButton)view.findViewById(R.id.defense_timer_1)).setText(team1);
            ((TimerButton)view.findViewById(R.id.defense_timer_2)).setText(team2);
            ((TimerButton)view.findViewById(R.id.defense_timer_3)).setText(team3);*/

            defenseTimer1 = view.findViewById(R.id.defense_timer_1);
            defenseTimer2 = view.findViewById(R.id.defense_timer_2);
            defenseTimer3 = view.findViewById(R.id.defense_timer_3);

            defenseTimer1.setListener(this);
            defenseTimer2.setListener(this);
            defenseTimer3.setListener(this);

            ((TextView)view.findViewById(R.id.team_1)).setText(team1);
            ((TextView)view.findViewById(R.id.team_2)).setText(team2);
            ((TextView)view.findViewById(R.id.team_3)).setText(team3);

            if(activity.isScouted()) {
                //Load
                ((Counter)view.findViewById(R.id.cell_1_s)).setValue(activity.data.cell1S);
                ((Counter)view.findViewById(R.id.cell_1_f)).setValue(activity.data.cell1F);
                ((Counter)view.findViewById(R.id.cell_2_s)).setValue(activity.data.cell2S);
                ((Counter)view.findViewById(R.id.cell_2_f)).setValue(activity.data.cell2F);
                ((Counter)view.findViewById(R.id.cell_3_s)).setValue(activity.data.cell3S);

                ((Counter)view.findViewById(R.id.cell_loading)).setValue(activity.data.cellLoading);
                ((Counter)view.findViewById(R.id.cell_floor)).setValue(activity.data.cellFloor);
                ((Counter)view.findViewById(R.id.cell_fumble)).setValue(activity.data.cellFumble);

                ((ToggleButton)view.findViewById(R.id.rotation_control_s)).setChecked(activity.data.rotationControlS);
                ((ToggleButton)view.findViewById(R.id.rotation_control_f)).setChecked(activity.data.rotationControlF);
                ((ToggleButton)view.findViewById(R.id.position_control_s)).setChecked(activity.data.positionControlS);
                ((ToggleButton)view.findViewById(R.id.position_control_f)).setChecked(activity.data.positionControlF);

                defenseTimer1.setSeconds(activity.data.defendedTime1);
                defenseTimer2.setSeconds(activity.data.defendedTime2);
                defenseTimer3.setSeconds(activity.data.defendedTime3);
            }
        }
    }

    public void save() {
        View fragmentView = getView();
        if(fragmentView != null) {
            //Save
            activity.data.cell1S = ((Counter)fragmentView.findViewById(R.id.cell_1_s)).getValue();
            activity.data.cell1F = ((Counter)fragmentView.findViewById(R.id.cell_1_f)).getValue();
            activity.data.cell2S = ((Counter)fragmentView.findViewById(R.id.cell_2_s)).getValue();
            activity.data.cell2F = ((Counter)fragmentView.findViewById(R.id.cell_2_f)).getValue();
            activity.data.cell3S = ((Counter)fragmentView.findViewById(R.id.cell_3_s)).getValue();

            activity.data.cellLoading = ((Counter)fragmentView.findViewById(R.id.cell_loading)).getValue();
            activity.data.cellFloor = ((Counter)fragmentView.findViewById(R.id.cell_floor)).getValue();
            activity.data.cellFumble = ((Counter)fragmentView.findViewById(R.id.cell_fumble)).getValue();

            activity.data.rotationControlS = ((ToggleButton)fragmentView.findViewById(R.id.rotation_control_s)).isChecked();
            activity.data.rotationControlF = ((ToggleButton)fragmentView.findViewById(R.id.rotation_control_f)).isChecked();
            activity.data.positionControlS = ((ToggleButton)fragmentView.findViewById(R.id.position_control_s)).isChecked();
            activity.data.positionControlF = ((ToggleButton)fragmentView.findViewById(R.id.position_control_f)).isChecked();

            if(defenseTimer1.hasRan()) {
                activity.data.defendedTeam1 = team1;
                activity.data.defendedTime1 = defenseTimer1.getSeconds();
            }
            if(defenseTimer2.hasRan()) {
                activity.data.defendedTeam2 = team2;
                activity.data.defendedTime2 = defenseTimer2.getSeconds();
            }
            if(defenseTimer3.hasRan()) {
                activity.data.defendedTeam3 = team3;
                activity.data.defendedTime3 = defenseTimer3.getSeconds();
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

    @Override
    public void enableScrolling(boolean enable) {
        if(listener != null) listener.enableScrolling(enable);
    }

    @Override
    public void onPlay() {
        listener.displayDefense(true);
    }

    @Override
    public void onReset() {
        listener.displayDefense(false);
    }
}
