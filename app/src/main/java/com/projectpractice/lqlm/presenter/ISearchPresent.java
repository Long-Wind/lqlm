package com.projectpractice.lqlm.presenter;

import com.projectpractice.lqlm.base.IBasePresenter;
import com.projectpractice.lqlm.view.ISearchCallback;

public interface ISearchPresent extends IBasePresenter<ISearchCallback> {
    void getHistories();

    void deleteHistories();

    void doSearch(String keyword);

    void research();

    void loadMore();

    void getRecommendWords();
}
