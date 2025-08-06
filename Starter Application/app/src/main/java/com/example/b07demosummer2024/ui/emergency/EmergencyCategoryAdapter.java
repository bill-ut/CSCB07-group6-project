package com.example.b07demosummer2024.ui.emergency;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.b07demosummer2024.R;
import com.example.b07demosummer2024.emergency.EmergencyCategory;

import java.util.List;

public class EmergencyCategoryAdapter extends RecyclerView.Adapter<EmergencyCategoryAdapter.ViewHolder> {

    public interface OnCategoryClickListener {
        void onCategoryClick(EmergencyCategory category);
    }

    private List<EmergencyCategory> categories;
    private OnCategoryClickListener listener;

    public EmergencyCategoryAdapter(List<EmergencyCategory> categories, OnCategoryClickListener listener) {
        this.categories = categories;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView icon;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.categoryName);
            icon = view.findViewById(R.id.categoryIcon);
        }
    }

    @NonNull
    @Override
    public EmergencyCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_emergency_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmergencyCategoryAdapter.ViewHolder holder, int position) {
        EmergencyCategory category = categories.get(position);
        holder.name.setText(category.name);
        holder.icon.setImageResource(category.iconResId);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCategoryClick(category);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}
