package com.nolankuza.theultimatealliance.scout;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nolankuza.theultimatealliance.R;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class TimerButton extends LinearLayout implements View.OnTouchListener {

    private Timer timer = new Timer();
    private boolean timerRunning;
    private long runTime;
    private static final int timerPeriodMs = 10;

    private Button timerButton;
    private TextView timerTextView;

    private String text;
    private boolean enabled;

    private Listener listener;
    private GestureDetector gestureDetector;

    public interface Listener {
        void enableScrolling(boolean enable);
        void onPlay();
        void onReset();
    }

    public TimerButton(Context context) {
        super(context);
        setUp(context);
    }

    public TimerButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        setUpAttrs(context, attrs);
        setUp(context);
    }

    public TimerButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setUpAttrs(context, attrs);
        setUp(context);
    }

    public void setUpAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TimerButton, 0, 0);
        text = a.getString(R.styleable.TimerButton_text);
        enabled = a.getBoolean(R.styleable.TimerButton_enabled, true);
    }

    @SuppressLint({"ClickableViewAccessibility", "NewApi"})
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setUp(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.timer_button, this, true);

        timerButton = getRootView().findViewById(R.id.timer_button);
        timerTextView = getRootView().findViewById(R.id.timer_text);

        timerButton.getBackground().setState(new int[] { android.R.attr.state_pressed, android.R.attr.state_enabled });
        timerButton.setText(text);
        timerButton.setEnabled(enabled);
        timerButton.setOnTouchListener(this);
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if(enabled) resetTimer();
                return true;
            }
        });

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateTextView();
                if(timerRunning) {
                    runTime += timerPeriodMs;
                }
            }
        }, 0, timerPeriodMs);

        refreshDrawableState();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        try {
            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if(enabled) startTimer();
                    //timerButton.getForeground().setState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled});
                    return gestureDetector.onTouchEvent(event);
                case MotionEvent.ACTION_UP:
                    if(enabled) stopTimer();
                    return gestureDetector.onTouchEvent(event);
                case MotionEvent.ACTION_CANCEL:
                    //Do nothing
                    //return true;
            }
        } catch(Exception e) {
            return true;
        }
        return gestureDetector.onTouchEvent(event);
        //return super.onTouchEvent(event);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    private void updateTextView() {
        final String timeString = String.format(Locale.US, "%01d:%02d.%02d",
                TimeUnit.MILLISECONDS.toMinutes(runTime) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(runTime) % TimeUnit.MINUTES.toSeconds(1),
                (int) (((runTime / 1000f) - Math.floor(runTime / 1000f)) * 100));
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if(listener != null) listener.enableScrolling(false);
                timerTextView.setText(timeString);
            }
        });
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        timerButton.setText(text);
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        timerTextView.setEnabled(enabled);
        timerButton.setEnabled(enabled);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void startTimer() {
        if (listener != null) {
            listener.enableScrolling(false);
            listener.onPlay();
        }
        timerRunning = true;
    }

    public void stopTimer() {
        if (listener != null) listener.enableScrolling(true);
        timerRunning = false;
    }

    public void resetTimer() {
        stopTimer();
        runTime = 0;
        updateTextView();
        if (listener != null) listener.onReset();
    }

    public float getSeconds() {
        return runTime / 1000f;
    }

    public void setSeconds(float seconds) {
        runTime = (long) (seconds * 1000f);
        updateTextView();
    }

    public boolean hasRan() {
        return runTime != 0;
    }
}
