package com.projectpractice.lqlm.presenter.impl;

import android.content.Context;
import android.util.Log;

import com.projectpractice.lqlm.model.Api;
import com.projectpractice.lqlm.model.entity.Category;
import com.projectpractice.lqlm.presenter.IHomePresenter;
import com.projectpractice.lqlm.utils.RetrofitManager;
import com.projectpractice.lqlm.view.IHomeCallback;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomePresenterImpl implements IHomePresenter {
    private static final String TAG = "HomePresenterImpl";
    private IHomeCallback callback;

    @Override
    public void getCategory() {
        callback.onLoading();
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        Call<Category> task = api.getCategories();
        task.enqueue(new Callback<Category>() {
            @Override
            public void onResponse(Call<Category> call, Response<Category> response) {
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    Category category = response.body();
                    Log.d(TAG, "categories ===> " + category.toString());
                    if (callback != null) {
                        if (category == null || category.getData().size() == 0) {
                            callback.onEmpty();
                        } else {
                            callback.onSuccess();
                            callback.onCategoryLoaded(category);
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<Category> call, Throwable t) {
                Log.d(TAG, "onFailure ===> " + t.toString());
                callback.onError();            }
        });

    }

    @Override
    public void registerCallback(IHomeCallback callback) {
        this.callback = callback;
    }

    @Override
    public void unregisterCallback(IHomeCallback callback) {
        if (this.callback != null) {
            this.callback = null;
        }
    }
}
