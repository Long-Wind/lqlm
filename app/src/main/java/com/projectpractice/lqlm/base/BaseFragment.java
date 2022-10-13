package com.projectpractice.lqlm.base;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.projectpractice.lqlm.R;

public abstract class BaseFragment extends Fragment {

    private FrameLayout baseContainer;
    private View successView;
    private View loadingView;
    private View errorView;
    private View emptyView;
    private LinearLayout retry;

    public enum State {
        NONE, SUCCESS, LOADING, ERROR, EMPTY
    }

    private State currentState = State.NONE;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View baseView = loadRootView(inflater, container);
        baseContainer = baseView.findViewById(R.id.base_container);
        loadStateView(inflater, container, savedInstanceState);
        retry = baseView.findViewById(R.id.network_error_tips);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retryClick();
            }
        });
        initPresenter();
        initView();
        initListener();
        loadData();
        return baseView;
    }


    protected void initListener() {

    }

    protected void retryClick() {

    }


    protected View loadRootView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return inflater.inflate(R.layout.base_fragment_layout, container, false);
    }

    protected void initView() {
    }

    protected void loadData() {
    }

    protected void initPresenter() {
    }

    private void loadStateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        successView = loadSuccessView(inflater, container, savedInstanceState);
        baseContainer.addView(successView);

        loadingView = loadLoadingView(inflater, container, savedInstanceState);
        baseContainer.addView(loadingView);

        errorView = loadErrorView(inflater, container, savedInstanceState);
        baseContainer.addView(errorView);

        emptyView = loadEmptyView(inflater, container);
        baseContainer.addView(emptyView);
        setUpState(State.NONE);
    }

    private View loadEmptyView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_empty, container, false);
    }

    private View loadErrorView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_error, container, false);
    }

    //供子类使用改变页面状态
    public void setUpState(State state) {
        currentState = state;
        if (currentState == State.SUCCESS) {
            successView.setVisibility(View.VISIBLE);
        } else {
            successView.setVisibility(View.GONE);
        }
        if (currentState == State.LOADING) {
            loadingView.setVisibility(View.VISIBLE);
        } else {
            loadingView.setVisibility(View.GONE);
        }
        if (currentState == State.ERROR) {
            errorView.setVisibility(View.VISIBLE);
        } else {
            errorView.setVisibility(View.GONE);
        }
        if (currentState == State.EMPTY) {
            emptyView.setVisibility(View.VISIBLE);
        } else {
            emptyView.setVisibility(View.GONE);
        }
    }

    protected View loadLoadingView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_loading, container, false);
    }

    protected View loadSuccessView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return getRootView(inflater, container, savedInstanceState);
    }

    protected abstract View getRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        release();
    }

    protected void release() {

    }
}
