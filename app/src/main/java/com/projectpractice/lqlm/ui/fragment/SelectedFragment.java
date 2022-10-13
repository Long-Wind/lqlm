package com.projectpractice.lqlm.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.projectpractice.lqlm.R;
import com.projectpractice.lqlm.base.BaseFragment;
import com.projectpractice.lqlm.databinding.FragmentSelectedBinding;
import com.projectpractice.lqlm.model.entity.SelectedContent;
import com.projectpractice.lqlm.model.entity.SelectedPageCategory;
import com.projectpractice.lqlm.presenter.ISelectedPagePresenter;
import com.projectpractice.lqlm.ui.adapter.SelectedPageContentAdapter;
import com.projectpractice.lqlm.ui.adapter.SelectedPageLeftAdapter;
import com.projectpractice.lqlm.utils.PresenterManager;
import com.projectpractice.lqlm.view.ISelectedPageCallback;

public class SelectedFragment extends BaseFragment implements ISelectedPageCallback, SelectedPageLeftAdapter.OnLeftItemClickListener {
    private ISelectedPagePresenter selectedPagePresenter;
    private com.projectpractice.lqlm.databinding.FragmentSelectedBinding binding;
    private RecyclerView leftCategoryList;
    private RecyclerView rightContentList;
    private SelectedPageLeftAdapter leftAdapter;
    private SelectedPageContentAdapter contentAdapter;

    @Override
    protected View loadRootView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return inflater.inflate(R.layout.fragment_with_bar_layout,container,false);
    }

    @Override
    protected View getRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSelectedBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    protected void initPresenter() {
        selectedPagePresenter = PresenterManager.getInstance().getSelectedPagePresenter();
        selectedPagePresenter.registerCallback(this);
        selectedPagePresenter.getCategories();
    }

    @Override
    protected void initView() {
        leftCategoryList = binding.leftCategoryList;
        rightContentList = binding.rightContentList;
        leftAdapter = new SelectedPageLeftAdapter();
        contentAdapter = new SelectedPageContentAdapter();
        leftCategoryList.setLayoutManager(new LinearLayoutManager(getContext()));
        rightContentList.setLayoutManager(new LinearLayoutManager(getContext()));
        leftCategoryList.setAdapter(leftAdapter);
        rightContentList.setAdapter(contentAdapter);
    }

    @Override
    protected void initListener() {
        leftAdapter.setOnLeftItemClickListener(this);
    }

    @Override
    protected void retryClick() {
        if(selectedPagePresenter != null) {
            selectedPagePresenter.reload();
        }
    }

    @Override
    protected void release() {
        if (selectedPagePresenter != null) {
            selectedPagePresenter.unregisterCallback(this);
        }
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
    public void onEmpty() {
        setUpState(State.EMPTY);
    }

    @Override
    public void onCategoriesLoaded(SelectedPageCategory categories) {
        setUpState(State.SUCCESS);
        leftAdapter.setData(categories.getData());
    }

    @Override
    public void onContentLoaded(SelectedContent contents) {
        contentAdapter.setData(contents);
    }

    @Override
    public void onLeftItemClick(SelectedPageCategory.DataBean item) {
        selectedPagePresenter.getContentByCategoryId(item);
    }
}
