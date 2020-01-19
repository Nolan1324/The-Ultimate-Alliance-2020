package com.nolankuza.theultimatealliance.scout;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageButton;

import com.nolankuza.theultimatealliance.model.gamedata.GameData;

import java.util.ArrayList;
import java.util.List;

public class CounterListHandler {

    private List<ImageButton> buttonList = new ArrayList<>();
    private Drawable offDrawable;
    private Drawable onDrawable;
    private Listener listener;

    private int value;

    interface Listener {
        void onValueChange(int newValue);
    }

    public CounterListHandler(List<ImageButton> buttonList, Drawable offDrawable, Drawable onDrawable, Listener listener) {
        this.buttonList = buttonList;
        this.offDrawable = offDrawable;
        this.onDrawable = onDrawable;
        this.listener = listener;

        for(int i = 0; i < buttonList.size(); i++) {
            final int index = i;
            buttonList.get(index).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                if(value == index + 1) {
                    setPreloadButtons(index);
                } else {
                    setPreloadButtons(index + 1);
                }
                }
            });
        }
    }

    private void setButton(int index, boolean on) {
        buttonList.get(index).setImageDrawable(on ? onDrawable : offDrawable);
    }

    private void setPreloadButtons(int newValue) {
        listener.onValueChange(newValue);
        value = newValue;
        for(int i = 0; i < buttonList.size(); i++) {
            setButton(i, newValue >= i + 1);
        }
    }
}
