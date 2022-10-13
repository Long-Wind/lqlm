package com.projectpractice.lqlm.view;

import com.projectpractice.lqlm.base.IBaseCallback;
import com.projectpractice.lqlm.model.entity.SelectedContent;
import com.projectpractice.lqlm.model.entity.SelectedPageCategory;

public interface ISelectedPageCallback extends IBaseCallback {
    void onCategoriesLoaded(SelectedPageCategory categories);

    void onContentLoaded(SelectedContent contents);
}
