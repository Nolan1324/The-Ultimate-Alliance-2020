package com.nolankuza.theultimatealliance.main.master;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;

import com.nolankuza.theultimatealliance.R;

public class ActionImageView extends android.support.v7.widget.AppCompatImageView {

    private static final int[] STATE_WAIT = {R.attr.state_wait};
    private static final int[] STATE_WARN = {R.attr.state_warn};
    private static final int[] STATE_DONE = {R.attr.state_done};

    private boolean isWait = false;
    private boolean isWarn = false;
    private boolean isDone = false;

    public ActionImageView(Context context) {
        super(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ActionImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setBackground(getResources().getDrawable(R.drawable.action_circle, null));
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ActionView, 0, 0);
        isWait = a.getBoolean(R.styleable.ActionView_state_wait, false);
        isWarn = a.getBoolean(R.styleable.ActionView_state_warn, false);
        isDone = a.getBoolean(R.styleable.ActionView_state_done, false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 3);
        if(isWait) {
            mergeDrawableStates(drawableState, STATE_WAIT);
        }
        if(isWarn) {
            mergeDrawableStates(drawableState, STATE_WARN);
        }
        if(isDone) {
            mergeDrawableStates(drawableState, STATE_DONE);
        }
        return drawableState;
    }

    public boolean isWait() {
        return isWait;
    }

    public void setWait(boolean wait) {
        isWait = wait;
        isWarn = !wait;
        isDone = !wait;
        refreshDrawableState();
    }

    public boolean isWarn() {
        return isWarn;
    }

    public void setWarn(boolean warn) {
        isWarn = warn;
        isWait = !warn;
        isDone = !warn;
        refreshDrawableState();
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
        isWait = !done;
        isWarn = !done;
        refreshDrawableState();
    }
}
