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
import com.nolankuza.theultimatealliance.structure.Alliance;
import com.nolankuza.theultimatealliance.structure.GameData;
import com.nolankuza.theultimatealliance.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.nolankuza.theultimatealliance.ApplicationState.prefs;

public class AutoFragment extends Fragment {
    private GameDataListener listener;
    private ScoutBasicActivity activity;

    private ImageView field;
    private List<Button> fieldButtons = new ArrayList<>();
    private ImageButton hatchButton;
    private ImageButton cargoButton;
    private Drawable hatchDrawable;
    private Drawable hatchOnDrawable;
    private Drawable cargoDrawable;
    private Drawable cargoOnDrawable;
    private ToggleButton droveYes;
    private ToggleButton droveNo;
    private ToggleButton droveStuck;

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
        fieldButtons.add((Button)view.findViewById(R.id.posLeft1));
        fieldButtons.add((Button)view.findViewById(R.id.posLeft2));
        fieldButtons.add((Button)view.findViewById(R.id.posLeft3));
        fieldButtons.add((Button)view.findViewById(R.id.posRight1));
        fieldButtons.add((Button)view.findViewById(R.id.posRight2));
        fieldButtons.add((Button)view.findViewById(R.id.posRight3));
        final boolean notReverseButtons = (activity.getGameData().alliance == Alliance.Blue) == prefs.getBoolean("field_reverse_pref", false);
        hatchButton = view.findViewById(R.id.preloadHatch);
        cargoButton = view.findViewById(R.id.preloadCargo);
        hatchDrawable = AppCompatResources.getDrawable(activity, R.drawable.ic_hatch);
        hatchOnDrawable = AppCompatResources.getDrawable(activity, R.drawable.ic_hatch_on);
        cargoDrawable = AppCompatResources.getDrawable(activity, R.drawable.ic_cargo);
        cargoOnDrawable = AppCompatResources.getDrawable(activity, R.drawable.ic_cargo_on);
        droveYes = view.findViewById(R.id.droveYes);
        droveNo = view.findViewById(R.id.droveNo);
        droveStuck = view.findViewById(R.id.droveStuck);
        //endregion

        view.findViewById(R.id.match_exit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefs.edit().putInt(Constants.PREF_CURRENT_SCOUTING_PAGE, -1).apply();
                Objects.requireNonNull(getActivity()).finish();
            }
        });
        if(!notReverseButtons) {
            fieldButtons.get(1).setVisibility(View.INVISIBLE);
            fieldButtons.get(4).setVisibility(View.VISIBLE);
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
        fieldButtons.get(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fieldChoose(view);
                activity.data.startPosition = notReverseButtons ? (byte) 3 : 1;
                activity.data.startLevel = notReverseButtons ? (byte) 2 : 1;
            }
        });
        fieldButtons.get(4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fieldChoose(view);
                activity.data.startPosition = 2;
                activity.data.startLevel = 1;
            }
        });
        fieldButtons.get(5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fieldChoose(view);
                activity.data.startPosition = notReverseButtons ? (byte) 1 : 3;
                activity.data.startLevel = notReverseButtons ? (byte) 2 : 1;
            }
        });
        if(activity.getGameData().alliance == Alliance.Blue) {
            field.setImageDrawable(getResources().getDrawable(R.drawable.field_blue));
        }
        hatchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameData.Data data = activity.data;
                if(data.preload == 0 || data.preload == 2) {
                    data.preload = 1;
                    hatchButton.setImageDrawable(hatchOnDrawable);
                } else {
                    data.preload = 0;
                    hatchButton.setImageDrawable(hatchDrawable);
                }
                cargoButton.setImageDrawable(cargoDrawable);
            }
        });
        cargoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameData.Data data = activity.data;
                if(data.preload == 0 || data.preload == 1) {
                    data.preload = 2;
                    cargoButton.setImageDrawable(cargoOnDrawable);
                } else {
                    data.preload = 0;
                    cargoButton.setImageDrawable(cargoDrawable);
                }
                hatchButton.setImageDrawable(hatchDrawable);
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
        droveStuck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                droveOffButtonsOff();
                ((ToggleButton)view).setChecked(true);
                activity.data.droveOff = -1;
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

    private void droveOffButtonsOff() {
        droveYes.setChecked(false);
        droveNo.setChecked(false);
        droveStuck.setChecked(false);
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