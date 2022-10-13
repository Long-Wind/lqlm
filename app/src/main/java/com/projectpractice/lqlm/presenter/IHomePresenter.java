package com.projectpractice.lqlm.presenter;

import android.content.Context;

import com.projectpractice.lqlm.base.IBasePresenter;
import com.projectpractice.lqlm.view.IHomeCallback;

public interface IHomePresenter extends IBasePresenter<IHomeCallback> {
    void getCategory();

}
