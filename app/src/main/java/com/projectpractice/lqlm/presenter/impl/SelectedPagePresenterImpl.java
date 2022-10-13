package com.projectpractice.lqlm.presenter.impl;

import android.util.Log;

import com.projectpractice.lqlm.model.Api;
import com.projectpractice.lqlm.model.entity.SelectedContent;
import com.projectpractice.lqlm.model.entity.SelectedPageCategory;
import com.projectpractice.lqlm.presenter.ISelectedPagePresenter;
import com.projectpractice.lqlm.utils.RetrofitManager;
import com.projectpractice.lqlm.view.ISelectedPageCallback;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SelectedPagePresenterImpl implements ISelectedPagePresenter {
    private static final String TAG = "SelectedPagePresenter";
    private final Api api;
    private ISelectedPageCallback callback;

    public SelectedPagePresenterImpl() {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        api = retrofit.create(Api.class);
    }

    @Override
    public void registerCallback(ISelectedPageCallback callback) {
        this.callback = callback;
    }

    @Override
    public void unregisterCallback(ISelectedPageCallback callback) {
        if (this.callback != null) {
            this.callback = null;
        }
    }

    @Override
    public void getCategories() {
        if (callback != null) {
            callback.onLoading();
        }
        Call<SelectedPageCategory> task = api.getSelectedPageCategories();
        task.enqueue(new Callback<SelectedPageCategory>() {
            @Override
            public void onResponse(Call<SelectedPageCategory> call, Response<SelectedPageCategory> response) {
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    if (callback != null) {
                        callback.onCategoriesLoaded(response.body());
                    }
                } else
                    onLoadedError();
            }

            @Override
            public void onFailure(Call<SelectedPageCategory> call, Throwable t) {
                onLoadedError();
            }
        });
    }

    private void onLoadedError() {
        if (callback != null) {
            callback.onError();
        }
    }

    @Override
    public void getContentByCategoryId(SelectedPageCategory.DataBean item) {
        Call<SelectedContent> task = api.getSelectedPageContent(item.getFavorites_id());
        task.enqueue(new Callback<SelectedContent>() {
            @Override
            public void onResponse(Call<SelectedContent> call, Response<SelectedContent> response) {
                int code = response.code();
                Log.d(TAG, "getContentByCategoryId code ==>"+code);
                if (code == HttpURLConnection.HTTP_OK) {
                    Log.d(TAG, "getContentByCategoryId result ==> " + response.body());
                    if (callback != null) {
                        callback.onContentLoaded(response.body());
                    }
                }else
                    onLoadedError();
            }

            @Override
            public void onFailure(Call<SelectedContent> call, Throwable t) {
                onLoadedError();
            }
        });
    }

    @Override
    public void reload() {
        this.getCategories();
    }
}
