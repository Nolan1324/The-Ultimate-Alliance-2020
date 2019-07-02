package com.nolankuza.theultimatealliance.scout;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.InverseBindingAdapter;
import android.databinding.InverseBindingListener;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nolankuza.theultimatealliance.R;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class AutoScoreFragment extends Fragment {
    private GameDataListener listener;
    private ScoutBasicActivity activity;
    private Timer timer = new Timer();

    public AutoScoreFragment() {

    }

    public static AutoScoreFragment newInstance() {
        AutoScoreFragment fragment = new AutoScoreFragment();
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
        return inflater.inflate(R.layout.fragment_auto_score, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        activity = (ScoutBasicActivity) getActivity();
        if(activity != null) {
            Log.d("HELLO", activity.isScouted() + " YE");
            if(activity.isScouted()) {
                 ((Counter)view.findViewById(R.id.hatch_loading)).setValue(activity.data.autoHatchLoading);
                 ((Counter)view.findViewById(R.id.hatch_floor)).setValue(activity.data.autoHatchFloor);
                 ((Counter)view.findViewById(R.id.cargo_loading)).setValue(activity.data.autoCargoLoading);
                 ((Counter)view.findViewById(R.id.cargo_floor)).setValue(activity.data.autoCargoFloor);
                 ((Counter)view.findViewById(R.id.hatch_rocket_3_s)).setValue(activity.data.autoHatchRocket3S);
                 ((Counter)view.findViewById(R.id.hatch_rocket_2_s)).setValue(activity.data.autoHatchRocket2S);
                 ((Counter)view.findViewById(R.id.hatch_rocket_1_s)).setValue(activity.data.autoHatchRocket1S);
                 ((Counter)view.findViewById(R.id.hatch_rocket_3_f)).setValue(activity.data.autoHatchRocket3F);
                 ((Counter)view.findViewById(R.id.hatch_rocket_2_f)).setValue(activity.data.autoHatchRocket2F);
                 ((Counter)view.findViewById(R.id.hatch_rocket_1_f)).setValue(activity.data.autoHatchRocket1F);
                 ((Counter)view.findViewById(R.id.cargo_rocket_3_s)).setValue(activity.data.autoCargoRocket3S);
                 ((Counter)view.findViewById(R.id.cargo_rocket_2_s)).setValue(activity.data.autoCargoRocket2S);
                 ((Counter)view.findViewById(R.id.cargo_rocket_1_s)).setValue(activity.data.autoCargoRocket1S);
                 ((Counter)view.findViewById(R.id.cargo_rocket_3_f)).setValue(activity.data.autoCargoRocket3F);
                 ((Counter)view.findViewById(R.id.cargo_rocket_2_f)).setValue(activity.data.autoCargoRocket2F);
                 ((Counter)view.findViewById(R.id.cargo_rocket_1_f)).setValue(activity.data.autoCargoRocket1F);
                 ((Counter)view.findViewById(R.id.fumble_hatch)).setValue(activity.data.autoHatchFumble);
                 ((Counter)view.findViewById(R.id.fumble_cargo)).setValue(activity.data.autoCargoFumble);
                 ((Counter)view.findViewById(R.id.hatch_ship_s)).setValue(activity.data.autoHatchShipS);
                 ((Counter)view.findViewById(R.id.hatch_ship_f)).setValue(activity.data.autoHatchShipF);
                 ((Counter)view.findViewById(R.id.cargo_ship_s)).setValue(activity.data.autoCargoShipS);
                 ((Counter)view.findViewById(R.id.cargo_ship_f)).setValue(activity.data.autoCargoShipF);
            }
        }
    }

    public void startTimer() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.d("HELLO", activity.data.autoCargoLoading + "");
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        View view = getView();
                        if(view != null) {
                            final View background = view.findViewById(R.id.background);
                            ObjectAnimator colorFade = ObjectAnimator.ofObject(background, "backgroundColor", new ArgbEvaluator(), Color.WHITE, 0xffcc0000);
                            colorFade.setDuration(1500);
                            colorFade.start();
                        }
                    }
                });
            }
        }, 14000);
    }

    public void save() {
        View fragmentView = getView();
        if(fragmentView != null) {
            activity.data.autoHatchLoading = ((Counter)fragmentView.findViewById(R.id.hatch_loading)).getValue();
            activity.data.autoHatchFloor = ((Counter)fragmentView.findViewById(R.id.hatch_floor)).getValue();
            activity.data.autoCargoLoading = ((Counter)fragmentView.findViewById(R.id.cargo_loading)).getValue();
            activity.data.autoCargoFloor = ((Counter)fragmentView.findViewById(R.id.cargo_floor)).getValue();
            activity.data.autoHatchRocket3S = ((Counter)fragmentView.findViewById(R.id.hatch_rocket_3_s)).getValue();
            activity.data.autoHatchRocket2S = ((Counter)fragmentView.findViewById(R.id.hatch_rocket_2_s)).getValue();
            activity.data.autoHatchRocket1S = ((Counter)fragmentView.findViewById(R.id.hatch_rocket_1_s)).getValue();
            activity.data.autoHatchRocket3F = ((Counter)fragmentView.findViewById(R.id.hatch_rocket_3_f)).getValue();
            activity.data.autoHatchRocket2F = ((Counter)fragmentView.findViewById(R.id.hatch_rocket_2_f)).getValue();
            activity.data.autoHatchRocket1F = ((Counter)fragmentView.findViewById(R.id.hatch_rocket_1_f)).getValue();
            activity.data.autoCargoRocket3S = ((Counter)fragmentView.findViewById(R.id.cargo_rocket_3_s)).getValue();
            activity.data.autoCargoRocket2S = ((Counter)fragmentView.findViewById(R.id.cargo_rocket_2_s)).getValue();
            activity.data.autoCargoRocket1S = ((Counter)fragmentView.findViewById(R.id.cargo_rocket_1_s)).getValue();
            activity.data.autoCargoRocket3F = ((Counter)fragmentView.findViewById(R.id.cargo_rocket_3_f)).getValue();
            activity.data.autoCargoRocket2F = ((Counter)fragmentView.findViewById(R.id.cargo_rocket_2_f)).getValue();
            activity.data.autoCargoRocket1F = ((Counter)fragmentView.findViewById(R.id.cargo_rocket_1_f)).getValue();
            activity.data.autoHatchFumble = ((Counter)fragmentView.findViewById(R.id.fumble_hatch)).getValue();
            activity.data.autoCargoFumble = ((Counter)fragmentView.findViewById(R.id.fumble_cargo)).getValue();
            activity.data.autoHatchShipS = ((Counter)fragmentView.findViewById(R.id.hatch_ship_s)).getValue();
            activity.data.autoHatchShipF = ((Counter)fragmentView.findViewById(R.id.hatch_ship_f)).getValue();
            activity.data.autoCargoShipS = ((Counter)fragmentView.findViewById(R.id.cargo_ship_s)).getValue();
            activity.data.autoCargoShipF = ((Counter)fragmentView.findViewById(R.id.cargo_ship_f)).getValue();
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

    @BindingAdapter("app:value")
    public static void setValue(Counter view, int value) {
        view.setValue(value);
    }

    /*
    @InverseBindingAdapter(attribute = "app:value", event = "android:onClick")
    public static int getValue(Counter view) {
        Log.d("HELLO", view.getValue() + "");
        return view.getValue();
    }
    */
}
