package com.nolankuza.theultimatealliance.scout;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nolankuza.theultimatealliance.R;
import com.nolankuza.theultimatealliance.util.Resources;

import java.util.Locale;

public class Counter extends LinearLayout {

    private static final int[] STATE_NORMAL = {R.attr.state_normal};
    private static final int[] STATE_CARGO = {R.attr.state_cargo};
    private static final int[] STATE_HATCH = {R.attr.state_hatch};
    private static final int[] STATE_LEVEL_1 = {R.attr.state_level_1};
    private static final int[] STATE_LEVEL_2 = {R.attr.state_level_2};
    private static final int[] STATE_LEVEL_3 = {R.attr.state_level_3};
    private static final int[] STATE_SUCCESS = {R.attr.state_success};
    private static final int[] STATE_FAIL = {R.attr.state_fail};

    private Button plus;
    private TextView value;
    private Button minus;

    private int number = 0;
    private int min = 0;
    private int max = 99;
    private int step = 1;

    private boolean isNormal;
    private boolean isCargo;
    private boolean isHatch;
    private boolean isLevel1;
    private boolean isLevel2;
    private boolean isLevel3;
    private boolean isSuccess;
    private boolean isFail;

    public Counter(Context context) {
        super(context);
        value = new TextView(context);
        setUp(context);
    }

    public Counter(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        value = new TextView(context, attrs);

        setUpAttrs(context, attrs);
        setUp(context);
    }

    public Counter(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        value = new TextView(context, attrs, defStyleAttr);

        setUpAttrs(context, attrs);
        setUp(context);
    }

    public void setUpAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.Counter, 0, 0);
        number = a.getInteger(R.styleable.Counter_value, 0);
        min = a.getInteger(R.styleable.Counter_min, 0);
        max = a.getInteger(R.styleable.Counter_max, 99);
        step = a.getInteger(R.styleable.Counter_step, 1);
        isNormal = a.getBoolean(R.styleable.Counter_state_normal, false);
        isCargo = a.getBoolean(R.styleable.Counter_state_cargo, false);
        isHatch = a.getBoolean(R.styleable.Counter_state_hatch, false);
        isLevel1 = a.getBoolean(R.styleable.Counter_state_level_1, false);
        isLevel2 = a.getBoolean(R.styleable.Counter_state_level_2, false);
        isLevel3 = a.getBoolean(R.styleable.Counter_state_level_3, false);
        isSuccess = a.getBoolean(R.styleable.Counter_state_success, false);
        isFail = a.getBoolean(R.styleable.Counter_state_fail, false);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setUp(Context context) {
        //TODO Swap programmatic layout for inflater
        //LayoutInflater inflater = LayoutInflater.from(getContext());
        //inflater.inflate(R.layout.counter, this, false);

        setOrientation(HORIZONTAL);
        setZ(2);

        plus = new Button(context);
        minus = new Button(context);

        //setBackground(/*plus.getBackground()*/getResources().getDrawable(type == 0 ? R.drawable.rounded : (type == 1 ? R.drawable.rounded_hatch : R.drawable.rounded_cargo)));
        setBackground(getResources().getDrawable(R.drawable.rounded));

        float counterPartWidth = Resources.getDimen(getContext(), R.dimen.counter_part_width);
        plus.setText(">");
        plus.setTextSize(counterPartWidth * 0.466f);
        plus.setBackground(null);
        plus.setTextColor(Color.BLACK);
        updateTextView();
        value.setTextSize(counterPartWidth * 0.466f);
        value.setGravity(Gravity.CENTER);
        value.setTextColor(Color.BLACK);
        //value.setWidth((int) counterPartWidth);
        minus.setText("<");
        minus.setTextSize(counterPartWidth * 0.466f);
        minus.setBackground(null);
        minus.setTextColor(Color.BLACK);

        LinearLayout.LayoutParams valueParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        valueParams.gravity = Gravity.CENTER;
        buttonParams.gravity = Gravity.CENTER;
        value.setLayoutParams(valueParams);
        buttonParams.width = (int) counterPartWidth;
        plus.setLayoutParams(buttonParams);
        minus.setLayoutParams(buttonParams);

        plus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(number < max) {
                    number += step;
                    updateTextView();
                }
            }
        });

        minus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(number > min) {
                    number -= step;
                    updateTextView();
                }
            }
        });

        addView(minus);
        addView(value);
        addView(plus);
        refreshDrawableState();
    }

    public int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 8);
        if(isNormal) mergeDrawableStates(drawableState, STATE_NORMAL);
        if(isCargo) mergeDrawableStates(drawableState, STATE_CARGO);
        if(isHatch) mergeDrawableStates(drawableState, STATE_HATCH);
        if(isLevel1) mergeDrawableStates(drawableState, STATE_LEVEL_1);
        if(isLevel2) mergeDrawableStates(drawableState, STATE_LEVEL_2);
        if(isLevel3) mergeDrawableStates(drawableState, STATE_LEVEL_3);
        if(isSuccess) mergeDrawableStates(drawableState, STATE_SUCCESS);
        if(isFail) mergeDrawableStates(drawableState, STATE_FAIL);
        return drawableState;
    }

    public void updateTextView() {
        value.setText(String.format(Locale.US, "%d", number));
    }

    public void setValue(int number) {
        this.number = number;
        updateTextView();
    }

    public int getValue() {
        return number;
    }
}


