package com.projectpractice.lqlm.view;

import com.projectpractice.lqlm.base.IBaseCallback;
import com.projectpractice.lqlm.model.entity.Histories;
import com.projectpractice.lqlm.model.entity.SearchRecommend;
import com.projectpractice.lqlm.model.entity.SearchResult;

import java.util.List;

public interface ISearchCallback extends IBaseCallback {
    void onHistoriesLoaded(Histories histories);

    void onHistoriesDeleted();

    void onSearchSuccess(SearchResult result);

    void onMoreLoaded(SearchResult result);

    void onMoreLoadError();

    void onMoreLoadEmpty();

    void onRecommendWordsLoaded(List<SearchRecommend.DataBean> recommendWords);
}
