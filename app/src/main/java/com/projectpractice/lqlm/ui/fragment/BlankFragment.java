package com.projectpractice.lqlm.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.projectpractice.lqlm.R;
import com.projectpractice.lqlm.base.BaseFragment;

public class BlankFragment extends BaseFragment {

    @Override
    protected View getRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        return view;
    }
}
