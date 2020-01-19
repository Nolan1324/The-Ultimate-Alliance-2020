package com.nolankuza.theultimatealliance.scout;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nolankuza.theultimatealliance.R;

import java.util.Timer;
import java.util.TimerTask;

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
            if(activity.isScouted()) {
                //Load
            }
        }
    }

    public void startTimer() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
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
           //Save
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
