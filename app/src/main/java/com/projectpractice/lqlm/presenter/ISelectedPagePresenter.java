package com.projectpractice.lqlm.presenter;

import com.projectpractice.lqlm.base.IBasePresenter;
import com.projectpractice.lqlm.model.entity.SelectedPageCategory;
import com.projectpractice.lqlm.view.ISelectedPageCallback;

public interface ISelectedPagePresenter extends IBasePresenter<ISelectedPageCallback> {
    void getCategories();

    void getContentByCategoryId(SelectedPageCategory.DataBean item);

    void reload();
}
