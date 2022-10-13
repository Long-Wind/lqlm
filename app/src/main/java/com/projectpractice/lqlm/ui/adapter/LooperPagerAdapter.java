package com.projectpractice.lqlm.ui.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.projectpractice.lqlm.databinding.ItemLooperImageBinding;
import com.projectpractice.lqlm.model.entity.HomePagerContent;

import java.util.ArrayList;
import java.util.List;

public class LooperPagerAdapter extends PagerAdapter {

    private static final String TAG = "LooperPagerAdapter";
    private List<HomePagerContent.DataBean> data = new ArrayList<>();
    private com.projectpractice.lqlm.databinding.ItemLooperImageBinding binding;
    private OnLooperItemClicked listener = null;

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        int realPosition = position % data.size();
        binding = ItemLooperImageBinding.inflate(LayoutInflater.from(container.getContext()));
        String pictUrl = data.get(realPosition).getPict_url();
        Log.d(TAG, "url: " + pictUrl);
        ImageView looperImage = binding.looperImage;
        Glide.with(container.getContext()).load("https:" + pictUrl+"_240x240.jpg").into(looperImage);
        looperImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onLooperItemClicked(data.get(realPosition));
            }
        });
        container.addView(binding.getRoot());
        return binding.getRoot();
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
//        return data.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    public void setData(List<HomePagerContent.DataBean> contents) {
        data.clear();
        data.addAll(contents);
        notifyDataSetChanged();
    }

    public int getDataSize() {
        return data.size();
    }

    public void setOnLooperItemClickedListener(OnLooperItemClicked listener) {
        this.listener = listener;
    }

    public interface OnLooperItemClicked {
        void onLooperItemClicked(HomePagerContent.DataBean item);
    }
}
