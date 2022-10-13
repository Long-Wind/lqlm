package com.projectpractice.lqlm.presenter;

import android.content.Context;

import com.projectpractice.lqlm.base.IBasePresenter;
import com.projectpractice.lqlm.view.ICategoryPagerCallback;

public interface ICategoryPagerPresenter extends IBasePresenter<ICategoryPagerCallback> {
    void getContentByCategoryId(int categoryId);

    void loadMore(int categoryId);

    void reload(int categoryId);


}
