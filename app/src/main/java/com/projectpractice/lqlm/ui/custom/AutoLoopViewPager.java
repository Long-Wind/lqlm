package com.projectpractice.lqlm.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.projectpractice.lqlm.R;

public class AutoLoopViewPager extends ViewPager {

    private boolean isLoop = false;
    private static final int DEFAULT_DURATION = 3000;
    private int duration = DEFAULT_DURATION;

    public AutoLoopViewPager(@NonNull Context context) {
        this(context, null);
    }

    public AutoLoopViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //读取属性
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AutoLoopStyle);
        //获取属性
        duration = typedArray.getInteger(R.styleable.AutoLoopStyle_duration, DEFAULT_DURATION);
        //回收
        typedArray.recycle();
    }

    private void setLoopDuration(int duration) {
        this.duration = duration;
    }

    public void startLoop() {
        isLoop = true;
        post(new Runnable() {
            @Override
            public void run() {
                int currentItem = getCurrentItem();
                currentItem++;
                setCurrentItem(currentItem);
                if (isLoop) {
                    postDelayed(this, duration);
                }
            }
        });

    }

    public void stopLoop() {
        isLoop = false;
    }
}
