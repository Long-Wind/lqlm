package com.projectpractice.lqlm.view;

import com.projectpractice.lqlm.base.IBaseCallback;
import com.projectpractice.lqlm.model.entity.HomePagerContent;

import java.util.List;

public interface ICategoryPagerCallback extends IBaseCallback {
    int getCategoryId();

    void onContentLoaded(List<HomePagerContent.DataBean> contents);

    void onLoadMoreError();

    void onLoadMoreEmpty();

    void onLoadMoreLoaded(List<HomePagerContent.DataBean> contents);

    /**
     * 轮播图加载成功了
     *
     * @param contents
     */
    void onLooperListLoaded(List<HomePagerContent.DataBean> contents);
}
