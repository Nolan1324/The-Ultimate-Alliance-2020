package com.nolankuza.theultimatealliance.scout;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.constraint.Guideline;
import android.support.v4.app.Fragment;
import android.support.v7.content.res.AppCompatResources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ToggleButton;

import com.nolankuza.theultimatealliance.R;
import com.nolankuza.theultimatealliance.model.Alliance;
import com.nolankuza.theultimatealliance.model.gamedata.GameData;
import com.nolankuza.theultimatealliance.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.nolankuza.theultimatealliance.ApplicationState.prefs;

public class AutoFragment extends Fragment {
    private GameDataListener listener;
    private ScoutBasicActivity activity;

    private ImageView field;
    private List<Button> fieldButtons = new ArrayList<>();
    private List<ImageButton> preloadButtons = new ArrayList<>();
    private Drawable cellDrawable;
    private Drawable cellOnDrawable;
    private ToggleButton droveYes;
    private ToggleButton droveNo;

    private ColorStateList offColor = ColorStateList.valueOf(Color.parseColor("#33ffffff"));
    private ColorStateList onColor = ColorStateList.valueOf(Color.parseColor("#99ffffff"));

    public AutoFragment() {

    }

    public static AutoFragment newInstance() {
        AutoFragment fragment = new AutoFragment();
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
        return inflater.inflate(R.layout.fragment_auto, container, false);
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //region Vars
        activity = ((ScoutBasicActivity) getActivity());
        field = view.findViewById(R.id.field);
        fieldButtons.add((Button)view.findViewById(R.id.pos1));
        fieldButtons.add((Button)view.findViewById(R.id.pos2));
        fieldButtons.add((Button)view.findViewById(R.id.pos3));
        final boolean notReverseButtons = (activity.getGameData().alliance == Alliance.Blue) == prefs.getBoolean("field_reverse_pref", false);
        preloadButtons.add((ImageButton)view.findViewById(R.id.preload1));
        preloadButtons.add((ImageButton)view.findViewById(R.id.preload2));
        preloadButtons.add((ImageButton)view.findViewById(R.id.preload3));
        cellDrawable = AppCompatResources.getDrawable(activity, R.drawable.ic_cell);
        cellOnDrawable = AppCompatResources.getDrawable(activity, R.drawable.ic_cell_on);
        droveYes = view.findViewById(R.id.droveYes);
        droveNo = view.findViewById(R.id.droveNo);
        //endregion

        view.findViewById(R.id.match_exit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefs.edit().putInt(Constants.PREF_CURRENT_SCOUTING_PAGE, -1).apply();
                Objects.requireNonNull(getActivity()).finish();
            }
        });
        if(!notReverseButtons) {
            ((Guideline) view.findViewById(R.id.guidelineLeft)).setGuidelinePercent(0.5f);
            ((Guideline) view.findViewById(R.id.guidelineRight)).setGuidelinePercent(1.0f);
        }
        if(prefs.getBoolean("field_reverse_pref", false)) {
            field.setScaleX(-1);
        }
        fieldOff();
        fieldButtons.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fieldChoose(view);
                activity.data.startPosition = notReverseButtons ? (byte) 3 : 1;
                activity.data.startLevel = notReverseButtons ? (byte) 1 : 2;
            }
        });
        fieldButtons.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fieldChoose(view);
                activity.data.startPosition = 2;
                activity.data.startLevel = 1;
            }
        });
        fieldButtons.get(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fieldChoose(view);
                activity.data.startPosition = notReverseButtons ? (byte) 1 : 3;
                activity.data.startLevel = notReverseButtons ? (byte) 1 : 2;
            }
        });
        if(activity.getGameData().alliance == Alliance.Blue) {
            field.setImageDrawable(getResources().getDrawable(R.drawable.field_blue));
        }
        preloadButtons.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameData.Data data = activity.data;
                if(data.preload == 1) {
                    setPreloadButtons(0);
                } else {
                    setPreloadButtons(1);
                }
            }
        });
        preloadButtons.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameData.Data data = activity.data;
                if(data.preload == 2) {
                    setPreloadButtons(1);
                } else {
                    setPreloadButtons(2);
                }
            }
        });
        preloadButtons.get(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameData.Data data = activity.data;
                if(data.preload == 3) {
                    setPreloadButtons(2);
                } else {
                    setPreloadButtons(3);
                }
            }
        });
        droveYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                droveOffButtonsOff();
                ((ToggleButton)view).setChecked(true);
                activity.data.droveOff = 1;
                activity.autoScoring();
            }
        });
        droveNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                droveOffButtonsOff();
                ((ToggleButton)view).setChecked(true);
                activity.data.droveOff = 0;
                activity.autoScoring();
            }
        });
        //view.findViewById(R.id.posLeft1).setOnClickListener();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void fieldOff() {
        for(Button button : fieldButtons) {
            button.setBackgroundTintList(offColor);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void fieldChoose(View view) {
        fieldOff();
        view.setBackgroundTintList(onColor);
    }

    private void setPreloadButton(int index, boolean on) {
        preloadButtons.get(index).setImageDrawable(on ? cellOnDrawable : cellDrawable);
    }

    private void setPreloadButtons(int value) {
        activity.data.preload = (byte) value;
        setPreloadButton(0, value >= 1);
        setPreloadButton(1, value >= 2);
        setPreloadButton(2, value >= 3);
    }

    private void droveOffButtonsOff() {
        droveYes.setChecked(false);
        droveNo.setChecked(false);
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