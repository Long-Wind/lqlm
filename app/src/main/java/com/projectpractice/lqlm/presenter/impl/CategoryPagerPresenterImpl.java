package com.projectpractice.lqlm.presenter.impl;

import android.content.Context;
import android.util.Log;

import com.projectpractice.lqlm.model.Api;
import com.projectpractice.lqlm.model.entity.HomePagerContent;
import com.projectpractice.lqlm.presenter.ICategoryPagerPresenter;
import com.projectpractice.lqlm.utils.RetrofitManager;
import com.projectpractice.lqlm.view.ICategoryPagerCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CategoryPagerPresenterImpl implements ICategoryPagerPresenter {

    private static final String TAG = "CategoryPagerPresenter";
    private Map<Integer, Integer> pageInfo = new HashMap<>();
    private Integer currentPage;

//    private CategoryPagerPresenterImpl() {
//
//    }
//
//    private static ICategoryPagerPresenter sInstance = null;
//
//    public static ICategoryPagerPresenter getsInstance() {
//        if (sInstance == null) {
//            sInstance = new CategoryPagerPresenterImpl();
//        }
//        return sInstance;
//    }

    @Override
    public void getContentByCategoryId(int categoryId) {
        for (ICategoryPagerCallback callback : callbacks) {
            if (callback.getCategoryId() == categoryId) {
                callback.onLoading();
            }
        }
        Integer targetPage = pageInfo.get(categoryId);
//        Log.d(TAG, "target page :" + targetPage);
        if (targetPage == null) {
            targetPage = 0;
            pageInfo.put(categoryId, targetPage);
        }
        Log.d(TAG, "page info ==> " + pageInfo);
        Call<HomePagerContent> task = createTask(categoryId,targetPage);
        task.enqueue(new Callback<HomePagerContent>() {
            @Override
            public void onResponse(Call<HomePagerContent> call, Response<HomePagerContent> response) {
                int code = response.code();
                Log.d(TAG, "onResponse code ===> " + code);
                if (code == 200) {
                    HomePagerContent homePagerContent = response.body();
                    Log.d(TAG, "pager content ===>" + homePagerContent);
                    handleHomePagerContentResult(homePagerContent, categoryId);
                } else {
                    handleNetWorkError(categoryId);
                }
            }

            @Override
            public void onFailure(Call<HomePagerContent> call, Throwable t) {
                Log.d(TAG, "onFailure ===>" + t);
                handleNetWorkError(categoryId);
            }
        });
    }

    private Call<HomePagerContent> createTask(int categoryId,Integer targetPage) {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        Call<HomePagerContent> task = api.getHomePagerContent(categoryId, targetPage);
        return task;
    }

    private void handleNetWorkError(int categoryId) {
        for (ICategoryPagerCallback callback : callbacks) {
            if (callback.getCategoryId() == categoryId) {
                callback.onError();
            }
        }
    }

    private void handleHomePagerContentResult(HomePagerContent homePagerContent, int categoryId) {
        List<HomePagerContent.DataBean> data = homePagerContent.getData();
        for (ICategoryPagerCallback callback : callbacks) {
            if (callback.getCategoryId() == categoryId) {
                if (homePagerContent.getData().size() == 0 || homePagerContent.getData() == null) {
                    callback.onEmpty();
                } else {
                    List<HomePagerContent.DataBean> looperData = data.subList(data.size() - 5, data.size());
                    callback.onContentLoaded(homePagerContent.getData());
                    callback.onLooperListLoaded(looperData);
                }
            }
        }
    }

    @Override
    public void loadMore(int categoryId) {
        //加载更多数据
        //1、拿到当前页码
        currentPage = pageInfo.get(categoryId);
        if (currentPage == null) {
            currentPage = 1;
        }
        //2、页码++
        currentPage++;
        //3、网络请求加载数据
        Call<HomePagerContent> task = createTask(categoryId,currentPage);
        //4、处理数据结果
        task.enqueue(new Callback<HomePagerContent>() {
            @Override
            public void onResponse(Call<HomePagerContent> call, Response<HomePagerContent> response) {
                int code = response.code();
                if (code == HttpsURLConnection.HTTP_OK) {
                    HomePagerContent result = response.body();
                    handleLoadMoreResult(result, categoryId);
                    pageInfo.put(categoryId,currentPage);
                } else {
                    handleLoadMoreError(categoryId);
                }
            }

            @Override
            public void onFailure(Call<HomePagerContent> call, Throwable t) {
                Log.d(TAG, "onFailure ==> " + t);
                handleLoadMoreError(categoryId);
            }
        });
    }

    private void handleLoadMoreResult(HomePagerContent result, int categoryId) {
        for (ICategoryPagerCallback callback : callbacks) {
            if (callback.getCategoryId() == categoryId) {
                if (result == null || result.getData().size() == 0) {
                    callback.onLoadMoreEmpty();
                } else {
                    callback.onLoadMoreLoaded(result.getData());
                }
            }
        }
    }

    private void handleLoadMoreError(int categoryId) {
        currentPage--;
        pageInfo.put(categoryId, currentPage);
        for (ICategoryPagerCallback callback : callbacks) {
            if (callback.getCategoryId() == categoryId) {
                callback.onLoadMoreError();
            }
        }
    }

    @Override
    public void reload(int categoryId) {

    }

    private List<ICategoryPagerCallback> callbacks = new ArrayList<>();

    @Override
    public void registerCallback(ICategoryPagerCallback callback) {
        if (!callbacks.contains(callback)) {
            callbacks.add(callback);
        }
    }

    @Override
    public void unregisterCallback(ICategoryPagerCallback callback) {
        callbacks.remove(callback);
    }
}
