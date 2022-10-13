package com.projectpractice.lqlm.view;

import com.projectpractice.lqlm.base.IBaseCallback;
import com.projectpractice.lqlm.model.entity.Category;

public interface IHomeCallback extends IBaseCallback {
    void onCategoryLoaded(Category category);

    void onSuccess();
}
