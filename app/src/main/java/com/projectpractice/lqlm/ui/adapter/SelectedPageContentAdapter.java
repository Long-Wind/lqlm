package com.projectpractice.lqlm.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.projectpractice.lqlm.databinding.ItemSelectedPageContentBinding;
import com.projectpractice.lqlm.model.entity.SelectedContent;

public class SelectedPageContentAdapter extends RecyclerView.Adapter<SelectedPageContentAdapter.InnerHolder> {
    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSelectedPageContentBinding binding = ItemSelectedPageContentBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new InnerHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public void setData(SelectedContent contents) {

    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        private final ItemSelectedPageContentBinding binding;

        public InnerHolder(@NonNull ItemSelectedPageContentBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
