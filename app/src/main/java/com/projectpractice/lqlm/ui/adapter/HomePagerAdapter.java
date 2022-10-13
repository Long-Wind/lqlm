package com.projectpractice.lqlm.ui.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.projectpractice.lqlm.model.entity.Category;
import com.projectpractice.lqlm.ui.fragment.HomePagerFragment;

import java.util.ArrayList;
import java.util.List;

public class HomePagerAdapter extends FragmentPagerAdapter {

    List<Category.DataBean> categoryList = new ArrayList<>();

    public HomePagerAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return categoryList.get(position).getTitle();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
//        HomePagerFragment homePagerFragment = new HomePagerFragment();
        return HomePagerFragment.newInstance(categoryList.get(position));
    }

    @Override
    public int getCount() {
        return categoryList.size();
    }

    public void setCategories(Category category) {
        categoryList.clear();
        List<Category.DataBean> data = category.getData();
        categoryList.addAll(data);
        notifyDataSetChanged();
    }
}
