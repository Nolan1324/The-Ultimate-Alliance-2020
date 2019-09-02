package com.nolankuza.theultimatealliance.scout;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nolankuza.theultimatealliance.R;

public class TeleopFragment extends Fragment {
    private GameDataListener listener;
    private ScoutBasicActivity activity;

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
            if(activity.isScouted()) {
                ((Counter)view.findViewById(R.id.hatch_loading)).setValue(activity.data.hatchLoading);
                ((Counter)view.findViewById(R.id.hatch_floor)).setValue(activity.data.hatchFloor);
                ((Counter)view.findViewById(R.id.cargo_loading)).setValue(activity.data.cargoLoading);
                ((Counter)view.findViewById(R.id.cargo_floor)).setValue(activity.data.cargoFloor);
                ((Counter)view.findViewById(R.id.hatch_rocket_3_s)).setValue(activity.data.hatchRocket3S);
                ((Counter)view.findViewById(R.id.hatch_rocket_2_s)).setValue(activity.data.hatchRocket2S);
                ((Counter)view.findViewById(R.id.hatch_rocket_1_s)).setValue(activity.data.hatchRocket1S);
                ((Counter)view.findViewById(R.id.hatch_rocket_3_f)).setValue(activity.data.hatchRocket3F);
                ((Counter)view.findViewById(R.id.hatch_rocket_2_f)).setValue(activity.data.hatchRocket2F);
                ((Counter)view.findViewById(R.id.hatch_rocket_1_f)).setValue(activity.data.hatchRocket1F);
                ((Counter)view.findViewById(R.id.cargo_rocket_3_s)).setValue(activity.data.cargoRocket3S);
                ((Counter)view.findViewById(R.id.cargo_rocket_2_s)).setValue(activity.data.cargoRocket2S);
                ((Counter)view.findViewById(R.id.cargo_rocket_1_s)).setValue(activity.data.cargoRocket1S);
                ((Counter)view.findViewById(R.id.cargo_rocket_3_f)).setValue(activity.data.cargoRocket3F);
                ((Counter)view.findViewById(R.id.cargo_rocket_2_f)).setValue(activity.data.cargoRocket2F);
                ((Counter)view.findViewById(R.id.cargo_rocket_1_f)).setValue(activity.data.cargoRocket1F);
                ((Counter)view.findViewById(R.id.fumble_hatch)).setValue(activity.data.hatchFumble);
                ((Counter)view.findViewById(R.id.fumble_cargo)).setValue(activity.data.cargoFumble);
                ((Counter)view.findViewById(R.id.hatch_ship_s)).setValue(activity.data.hatchShipS);
                ((Counter)view.findViewById(R.id.hatch_ship_f)).setValue(activity.data.hatchShipF);
                ((Counter)view.findViewById(R.id.cargo_ship_s)).setValue(activity.data.cargoShipS);
                ((Counter)view.findViewById(R.id.cargo_ship_f)).setValue(activity.data.cargoShipF);
            }
        }
    }

    public void save() {
        View fragmentView = getView();
        if(fragmentView != null) {
            activity.data.hatchLoading = ((Counter)fragmentView.findViewById(R.id.hatch_loading)).getValue();
            activity.data.hatchFloor = ((Counter)fragmentView.findViewById(R.id.hatch_floor)).getValue();
            activity.data.cargoLoading = ((Counter)fragmentView.findViewById(R.id.cargo_loading)).getValue();
            activity.data.cargoFloor = ((Counter)fragmentView.findViewById(R.id.cargo_floor)).getValue();
            activity.data.hatchRocket3S = ((Counter)fragmentView.findViewById(R.id.hatch_rocket_3_s)).getValue();
            activity.data.hatchRocket2S = ((Counter)fragmentView.findViewById(R.id.hatch_rocket_2_s)).getValue();
            activity.data.hatchRocket1S = ((Counter)fragmentView.findViewById(R.id.hatch_rocket_1_s)).getValue();
            activity.data.hatchRocket3F = ((Counter)fragmentView.findViewById(R.id.hatch_rocket_3_f)).getValue();
            activity.data.hatchRocket2F = ((Counter)fragmentView.findViewById(R.id.hatch_rocket_2_f)).getValue();
            activity.data.hatchRocket1F = ((Counter)fragmentView.findViewById(R.id.hatch_rocket_1_f)).getValue();
            activity.data.cargoRocket3S = ((Counter)fragmentView.findViewById(R.id.cargo_rocket_3_s)).getValue();
            activity.data.cargoRocket2S = ((Counter)fragmentView.findViewById(R.id.cargo_rocket_2_s)).getValue();
            activity.data.cargoRocket1S = ((Counter)fragmentView.findViewById(R.id.cargo_rocket_1_s)).getValue();
            activity.data.cargoRocket3F = ((Counter)fragmentView.findViewById(R.id.cargo_rocket_3_f)).getValue();
            activity.data.cargoRocket2F = ((Counter)fragmentView.findViewById(R.id.cargo_rocket_2_f)).getValue();
            activity.data.cargoRocket1F = ((Counter)fragmentView.findViewById(R.id.cargo_rocket_1_f)).getValue();
            activity.data.hatchFumble = ((Counter)fragmentView.findViewById(R.id.fumble_hatch)).getValue();
            activity.data.cargoFumble = ((Counter)fragmentView.findViewById(R.id.fumble_cargo)).getValue();
            activity.data.hatchShipS = ((Counter)fragmentView.findViewById(R.id.hatch_ship_s)).getValue();
            activity.data.hatchShipF = ((Counter)fragmentView.findViewById(R.id.hatch_ship_f)).getValue();
            activity.data.cargoShipS = ((Counter)fragmentView.findViewById(R.id.cargo_ship_s)).getValue();
            activity.data.cargoShipF = ((Counter)fragmentView.findViewById(R.id.cargo_ship_f)).getValue();
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
