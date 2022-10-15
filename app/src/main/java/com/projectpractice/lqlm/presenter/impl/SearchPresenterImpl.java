package com.projectpractice.lqlm.presenter.impl;

import com.projectpractice.lqlm.model.Api;
import com.projectpractice.lqlm.model.entity.SearchRecommend;
import com.projectpractice.lqlm.presenter.ISearchPresent;
import com.projectpractice.lqlm.utils.RetrofitManager;
import com.projectpractice.lqlm.view.ISearchCallback;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchPresenterImpl implements ISearchPresent {
    private Api api;
    private ISearchCallback callback;

    public SearchPresenterImpl() {
        this.api = RetrofitManager.getInstance().getRetrofit().create(Api.class);
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

    }

    @Override
    public void deleteHistories() {

    }

    @Override
    public void doSearch(String keyword) {

    }

    @Override
    public void research() {

    }

    @Override
    public void loadMore() {

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
