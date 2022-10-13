package com.projectpractice.lqlm.presenter.impl;

import android.content.Context;
import android.util.Log;

import com.projectpractice.lqlm.model.Api;
import com.projectpractice.lqlm.model.entity.TicketParams;
import com.projectpractice.lqlm.model.entity.TicketResult;
import com.projectpractice.lqlm.presenter.ITicketPresenter;
import com.projectpractice.lqlm.utils.RetrofitManager;
import com.projectpractice.lqlm.view.ITicketCallback;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TicketPresenterImpl implements ITicketPresenter {
    private static final String TAG = "TicketPresenter";
    private ITicketCallback callback = null;
    private String cover;
    private TicketResult ticketResult;

    enum State {
        LOADING, SUCCESS, ERROR, NONE
    }

    private State currentState = State.NONE;

    @Override
    public void getTicket(String title, String url, String cover) {
        onLoadTicketLoading();
        this.cover = cover;
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        String url1 = "https:" + url;
//        Log.d(TAG, "title :" + title);
//        Log.d(TAG, "url :" + url1);
        TicketParams params = new TicketParams(url1, title);
        Call<TicketResult> task = api.getTicket(params);
        task.enqueue(new Callback<TicketResult>() {
            @Override
            public void onResponse(Call<TicketResult> call, Response<TicketResult> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    currentState = State.SUCCESS;
                    ticketResult = response.body();
                    onLoadTicketSuccess();
                } else {
                    //请求失败
                    onLoadTicketError();
                    currentState = State.ERROR;
                }
            }

            @Override
            public void onFailure(Call<TicketResult> call, Throwable t) {
                onLoadTicketError();
                currentState = State.ERROR;
            }
        });
    }

    private void onLoadTicketSuccess() {
        if (callback != null) {
            callback.onTicketLoad(cover, ticketResult);
        } else
            currentState = State.SUCCESS;
    }

    private void onLoadTicketError() {
        if (callback != null) {
            callback.onError();
        } else
            currentState = State.ERROR;
    }

    private void onLoadTicketLoading() {
        if (callback != null) {
            callback.onLoading();
        } else
            currentState = State.LOADING;
    }

    @Override
    public void registerCallback(ITicketCallback callback) {
        this.callback = callback;
        /**
         * 因为线程不同，一个是网络IO线程，一个是UI线程
         * 为避免网络加载快于UI创建
         * 故添加此操作
         */
        if (currentState != State.NONE) {
            //说明网络加载已完成，状态发生改变
            //通知UI更新
            if (currentState == State.SUCCESS) {
                onLoadTicketSuccess();
            } else if (currentState == State.ERROR) {
                onLoadTicketError();
            } else if (currentState == State.LOADING) {
                onLoadTicketLoading();
            }
        }
    }

    @Override
    public void unregisterCallback(ITicketCallback callback) {
        if (callback != null) {
            this.callback = null;
        }
    }
}
