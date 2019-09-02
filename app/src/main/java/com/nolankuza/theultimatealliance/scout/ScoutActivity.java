package com.nolankuza.theultimatealliance.scout;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nolankuza.theultimatealliance.R;

public class ScoutActivity extends AppCompatActivity {

    float downX = 0;
    float downY = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scout);

        final Button redExchange = findViewById(R.id.redExchange);
        redExchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("+1 Exchange deposit", true);
            }
        });

        final ScaleButton scaleTop = findViewById(R.id.scaleTop);
        scaleTop.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent e) {
                if(e.getAction() == MotionEvent.ACTION_DOWN) {
                    downX = e.getX();
                    downY = e.getY();
                } else if(e.getAction() == MotionEvent.ACTION_UP) {
                    float upY = e.getY();

                    if(downY - upY >= 220) {
                        showToast("+1 Scale high", true);
                    } else if(upY - downY >= 220) {
                        showToast("+1 Scale low", true);
                    } else {
                        showToast("+1 Scale middle", true);
                    }
                    scaleTop.performClick();
                }
                return true;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if(e.getAction() == MotionEvent.ACTION_DOWN) {
            downX = e.getX();
            downY = e.getY();
        } else if(e.getAction() == MotionEvent.ACTION_UP) {
            float upX = e.getX();
            float upY = e.getY();

            if(distance(downX, upX, downY, upY) >= 300) {
                showToast("Failed", false);
            }
        }
        return super.onTouchEvent(e);
    }

    public void showToast(String message, boolean positive) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_scout,
                (ViewGroup) findViewById(R.id.toast_scout));

        TextView text = layout.findViewById(R.id.toast_scout_text);
        text.setText(message);
        if(positive) {
            text.setTextColor(Color.parseColor("#ff669900"));
        } else {
            text.setTextColor(Color.parseColor("#ffcc0000"));
        }

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 0, 80);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    public static float distance(float x1, float x2, float y1, float y2) {
        return (float) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    public static class ScaleButton extends android.support.v7.widget.AppCompatButton {
        public int downX;
        public int downY;

        public ScaleButton(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        public ScaleButton(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            init();
        }

        public void init() {
            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        @Override
        public boolean performClick() {
            return super.performClick();
        }
    }
}
