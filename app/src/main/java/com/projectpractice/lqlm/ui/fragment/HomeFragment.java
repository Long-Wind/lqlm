package com.projectpractice.lqlm.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.projectpractice.lqlm.R;
import com.projectpractice.lqlm.base.BaseFragment;
import com.projectpractice.lqlm.databinding.FragmentHomeBinding;
import com.projectpractice.lqlm.model.entity.Category;
import com.projectpractice.lqlm.presenter.IHomePresenter;
import com.projectpractice.lqlm.presenter.impl.HomePresenterImpl;
import com.projectpractice.lqlm.ui.adapter.HomePagerAdapter;
import com.projectpractice.lqlm.utils.PresenterManager;
import com.projectpractice.lqlm.view.IHomeCallback;

public class HomeFragment extends BaseFragment implements IHomeCallback {

    private static final String TAG = "HomeFragment";
    private IHomePresenter homePresenter;
    private FragmentHomeBinding binding;
    private HomePagerAdapter homePagerAdapter;

    @Override
    protected View getRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    protected View loadRootView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return inflater.inflate(R.layout.base_home_fragment_layout, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView...");
    }

    @Override
    protected void initPresenter() {
        homePresenter = PresenterManager.getInstance().getHomePresenter();
        homePresenter.registerCallback(this);

    }

    @Override
    protected void initView() {
        TabLayout homeIndicator = binding.homeIndicator;
        ViewPager homePager = binding.homePager;
        homeIndicator.setupWithViewPager(homePager);
        homePagerAdapter = new HomePagerAdapter(getChildFragmentManager());
        homePager.setAdapter(homePagerAdapter);
    }

    @Override
    protected void loadData() {
        homePresenter.getCategory();
    }

    @Override
    protected void release() {
        if (homePresenter != null) {
            homePresenter.unregisterCallback(this);
        }
    }

    @Override
    public void onCategoryLoaded(Category category) {
        if (category != null) {
            homePagerAdapter.setCategories(category);
        }
    }

    @Override
    protected void retryClick() {
        if (homePresenter != null) {
            homePresenter.getCategory();
        }
    }


    @Override
    public void onEmpty() {
        setUpState(State.EMPTY);
    }

    @Override
    public void onLoading() {
        setUpState(State.LOADING);
    }

    @Override
    public void onError() {
        setUpState(State.ERROR);
    }

    @Override
    public void onSuccess() {
        setUpState(State.SUCCESS);
    }
}