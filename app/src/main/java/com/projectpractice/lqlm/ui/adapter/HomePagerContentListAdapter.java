package com.projectpractice.lqlm.ui.adapter;

import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.projectpractice.lqlm.R;
import com.projectpractice.lqlm.databinding.ItemHomePagerContentBinding;
import com.projectpractice.lqlm.model.entity.HomePagerContent;

import java.util.ArrayList;
import java.util.List;

public class HomePagerContentListAdapter extends RecyclerView.Adapter<HomePagerContentListAdapter.InnerHolder> {

    private static final String TAG = "ListAdapter";
    private List<HomePagerContent.DataBean> data = null;
    private OnListItemClickListener listener;

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder ...");
        ItemHomePagerContentBinding binding = ItemHomePagerContentBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new InnerHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HomePagerContentListAdapter.InnerHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder position ==> "+position);
        TextView goodsAfterOffPrice = holder.binding.goodsAfterOffPrice;
        ImageView goodsCover = holder.binding.goodsCover;
        TextView goodsOffPrice = holder.binding.goodsOffPrice;
        TextView goodsTitle = holder.binding.goodsTitle;
        TextView goodsOriginalPrice = holder.binding.goodsOriginalPrice;
        TextView goodsSellCount = holder.binding.goodsSellCount;


        String originPrice = data.get(position).getZk_final_price();
        long couponAmount = data.get(position).getCoupon_amount();
        Glide.with(holder.itemView.getContext()).load("https:" + data.get(position).getPict_url() + "_80x80.jpg").into(goodsCover);
        goodsTitle.setText(data.get(position).getTitle());
        goodsOriginalPrice.setText("￥" + originPrice);
        goodsOriginalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        goodsOffPrice.setText("省" + couponAmount + "元");
        float finalPrice = Float.parseFloat(originPrice) - couponAmount;
        goodsAfterOffPrice.setText(String.format("%.2f", finalPrice));
        goodsSellCount.setText(data.get(position).getVolume() + "+人已购买");

        //设置item跳转淘口令的点击事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onListItemClick(data.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<HomePagerContent.DataBean> contents) {
        data = new ArrayList<>();
        data.addAll(contents);
        notifyDataSetChanged();
    }

    public void addData(List<HomePagerContent.DataBean> contents) {
        data.addAll(contents);
        notifyItemRangeChanged(data.size(), contents.size());
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        ItemHomePagerContentBinding binding;

        public InnerHolder(@NonNull ItemHomePagerContentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void setOnListItemClickListener(OnListItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnListItemClickListener {
        void onListItemClick(HomePagerContent.DataBean item);
    }
}
