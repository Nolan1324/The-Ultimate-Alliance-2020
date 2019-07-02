package com.nolankuza.theultimatealliance.datasync;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.nolankuza.theultimatealliance.R;

public class GroupToggle extends LinearLayout implements View.OnClickListener {

    private CheckBox checkBox;
    private TextView textView;

    private int toggleButtonsId;
    private boolean checked = true;

    public GroupToggle(Context context) {
        super(context);
        textView = new TextView(context);
        setUp(context);
    }

    public GroupToggle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        textView = new TextView(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.GroupToggle, 0, 0);
        toggleButtonsId = a.getResourceId(R.styleable.GroupToggle_toggle_buttons, 0);

        setUp(context);
    }

    public GroupToggle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        textView = new TextView(context, attrs, defStyleAttr);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.GroupToggle, 0, 0);
        toggleButtonsId = a.getResourceId(R.styleable.GroupToggle_toggle_buttons, 0);

        setUp(context);
    }



    private void setUp(Context context) {
        setOrientation(HORIZONTAL);

        checkBox = new CheckBox(context);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        textView.setLayoutParams(params);
        checkBox.setLayoutParams(params);
        checkBox.setChecked(true);
        checkBox.setEnabled(false);

        addView(checkBox);
        addView(textView);

        setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        checked = !checked;
        setChecked(checked);
    }

    public void setChecked(boolean checked) {
        checkBox.setChecked(checked);

        if(toggleButtonsId != 0) {
            ViewGroup v = getRootView().findViewById(toggleButtonsId);
            for(int i = 0; i < v.getChildCount(); i++) {
                ((ToggleButton) v.getChildAt(i)).setChecked(checked);
            }
        }
    }
}
