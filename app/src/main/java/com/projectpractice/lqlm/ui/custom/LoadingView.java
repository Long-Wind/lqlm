package com.projectpractice.lqlm.ui.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.projectpractice.lqlm.R;

public class LoadingView extends AppCompatImageView {

    private float degrees = 0;
    private boolean needRotate = true;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setImageResource(R.mipmap.loading);

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        needRotate = true;
        startRotate();
    }

    private void startRotate() {
        post(new Runnable() {
            @Override
            public void run() {
                degrees += 10;
                if (degrees >= 360) {
                    degrees = 0;
                }
                invalidate();
                if (!needRotate && getVisibility() != VISIBLE) {
                    removeCallbacks(this);
                } else
                    postDelayed(this, 20);
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        needRotate = false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.rotate(degrees, getWidth() / 2, getHeight() / 2);
        super.onDraw(canvas);
    }
}
