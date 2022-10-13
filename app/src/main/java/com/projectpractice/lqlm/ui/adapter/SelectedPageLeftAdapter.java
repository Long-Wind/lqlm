package com.projectpractice.lqlm.ui.adapter;

import android.annotation.SuppressLint;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.collection.ArraySet;
import androidx.recyclerview.widget.RecyclerView;

import com.projectpractice.lqlm.R;
import com.projectpractice.lqlm.databinding.ItemSelectedPageLeftBinding;
import com.projectpractice.lqlm.model.entity.SelectedPageCategory;

import java.util.ArrayList;
import java.util.List;

public class SelectedPageLeftAdapter extends RecyclerView.Adapter<SelectedPageLeftAdapter.InnerHolder> {
    private static final String TAG = "LeftAdapter";
    private List<SelectedPageCategory.DataBean> data = new ArrayList<>();
    private int currentSelectedPosition = 0;
    private OnLeftItemClickListener itemClickListener = null;

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSelectedPageLeftBinding binding = ItemSelectedPageLeftBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new InnerHolder(binding);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        TextView categoryTv = holder.binding.leftCategoryTv;
        if (currentSelectedPosition == position) {
            categoryTv.setBackgroundColor(categoryTv.getResources().getColor(R.color.colorEEEEEE, null));
        } else {
            categoryTv.setBackgroundColor(categoryTv.getResources().getColor(R.color.white, null));
        }
        categoryTv.setText(data.get(position).getFavorites_title());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (currentSelectedPosition != position) {
                    currentSelectedPosition = position;
                    itemClickListener.onLeftItemClick(data.get(position));
                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<SelectedPageCategory.DataBean> data) {
        if (data != null) {
            this.data.clear();
            this.data.addAll(data);
            notifyDataSetChanged();
        }
        if (data.size() > 0) {
            itemClickListener.onLeftItemClick(data.get(currentSelectedPosition));
        }
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        private final ItemSelectedPageLeftBinding binding;

        public InnerHolder(@NonNull ItemSelectedPageLeftBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface OnLeftItemClickListener {
        void onLeftItemClick(SelectedPageCategory.DataBean item);
    }

    public void setOnLeftItemClickListener(OnLeftItemClickListener listener) {
        this.itemClickListener = listener;
    }
}
