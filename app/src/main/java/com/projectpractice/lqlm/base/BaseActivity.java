package com.projectpractice.lqlm.base;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        initView();
        initPresenter();
        initListener();
    }

    protected void initPresenter() {
    }

    protected void initListener() {

    }

    protected abstract void initView();

    public abstract View getLayoutResource();
}
