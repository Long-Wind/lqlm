package com.projectpractice.lqlm.presenter.impl;

import com.projectpractice.lqlm.model.Api;
import com.projectpractice.lqlm.model.entity.Histories;
import com.projectpractice.lqlm.model.entity.SearchRecommend;
import com.projectpractice.lqlm.model.entity.SearchResult;
import com.projectpractice.lqlm.presenter.ISearchPresent;
import com.projectpractice.lqlm.utils.JsonCacheUtil;
import com.projectpractice.lqlm.utils.RetrofitManager;
import com.projectpractice.lqlm.view.ISearchCallback;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchPresenterImpl implements ISearchPresent {
    private final JsonCacheUtil jsonCacheUtil;
    private Api api;
    private ISearchCallback callback;
    public static final int DEFAULT_PAGE = 0;
    public static final String KEY_HISTORIES = "key_histories";
    private int currentPage = DEFAULT_PAGE;
    private String currentKeyword;

    public SearchPresenterImpl() {
        this.api = RetrofitManager.getInstance().getRetrofit().create(Api.class);
        jsonCacheUtil = JsonCacheUtil.getsInstance();
    }

    @Override
    public void registerCallback(ISearchCallback callback) {
        this.callback = callback;
    }

    @Override
    public void unregisterCallback(ISearchCallback callback) {
        this.callback = null;
    }

    @Override
    public void getHistories() {
        Histories histories = jsonCacheUtil.getValue(KEY_HISTORIES,Histories.class);
        if(callback != null) {
            callback.onHistoriesLoaded(histories);
        }
    }

    @Override
    public void deleteHistories() {
        jsonCacheUtil.delCache(KEY_HISTORIES);
        if(callback != null) {
            callback.onHistoriesDeleted();
        }
    }

    private void saveHistory(String history) {
        Histories histories = jsonCacheUtil.getValue(KEY_HISTORIES, Histories.class);
        List<String> historiesList = null;
        if (histories != null && histories.getHistories() != null) {
            historiesList = histories.getHistories();
            if (historiesList.contains(history)) {
                historiesList.remove(history);
            }
        }
        //去重完成
        //处理没有数据的情况
        if (historiesList == null) {
            historiesList = new ArrayList<>();
        }
        if (histories == null) {
            histories = new Histories();
        }
        histories.setHistories(historiesList);
        //对个数进行限制
        int historiesMaxSize = 10;
        if (historiesList.size() > historiesMaxSize) {
            historiesList = historiesList.subList(0, historiesMaxSize);
        }
        //添加记录
        historiesList.add(history);
        //保存记录
       jsonCacheUtil.saveCache(KEY_HISTORIES, histories);
    }

    @Override
    public void doSearch(String keyword) {
        if (currentKeyword == null || !currentKeyword.equals(keyword)) {
//            this.saveHistory(keyword);
            this.currentKeyword = keyword;
        }
        if (callback != null) {
            callback.onLoading();
        }
        Call<SearchResult> task = api.doSearch(currentPage, keyword);
        task.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    handleSearchResult(response.body());
                } else {
                    onError();
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                onError();
            }
        });
    }

    private void handleSearchResult(SearchResult result) {
        if (callback != null) {
            if (isResultEmpty(result)) {
                callback.onEmpty();
            } else {
                callback.onSearchSuccess(result);
            }
        }
    }

    private boolean isResultEmpty(SearchResult result) {
        try {
            return result == null ||
                    result.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data().size() == 0;
        } catch (Exception e) {
            return false;
        }
    }

    private void onError() {
        if (callback != null) {
            callback.onError();
        }
    }

    @Override
    public void research() {
        if (currentKeyword == null) {
            if (callback != null) {
                callback.onEmpty();
            }
        } else {
            this.doSearch(currentKeyword);
        }
    }

    @Override
    public void loadMore() {
        currentPage++;
        if (currentKeyword == null) {
            if (callback != null) {
                callback.onMoreLoadEmpty();
            }
        } else {
            doSearchMore();
        }
    }

    private void doSearchMore() {
        Call<SearchResult> task = api.doSearch(currentPage, currentKeyword);
        task.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    handleMoreSearchResult(response.body());
                } else {
                    onLoadMoreError();
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                t.printStackTrace();
                onLoadMoreError();
            }
        });
    }

    private void handleMoreSearchResult(SearchResult result) {
        if (callback != null) {
            if (isResultEmpty(result)) {
                //数据为空
                callback.onMoreLoadEmpty();
            } else {
                callback.onMoreLoaded(result);
            }
        }
    }

    private void onLoadMoreError() {
        if (callback != null) {
            callback.onMoreLoadError();
        }
    }

    @Override
    public void getRecommendWords() {
        Call<SearchRecommend> task = api.getRecommendWords();
        task.enqueue(new Callback<SearchRecommend>() {
            @Override
            public void onResponse(Call<SearchRecommend> call, Response<SearchRecommend> response) {
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    if (callback != null) {
                        callback.onRecommendWordsLoaded(response.body().getData());
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchRecommend> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
