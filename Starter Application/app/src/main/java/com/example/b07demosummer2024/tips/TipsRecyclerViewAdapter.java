package com.example.b07demosummer2024.tips;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.b07demosummer2024.R;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TipsRecyclerViewAdapter extends RecyclerView.Adapter<TipsRecyclerViewAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<TipsModel> tipsModels;

    public TipsRecyclerViewAdapter(Context context, ArrayList<TipsModel> tipsModels) {
        this.context = context;
        this.tipsModels = tipsModels;
    }

    @NonNull
    @Override
    public TipsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_row, parent, false);
        return new TipsRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TipsRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.textView.setText(tipsModels.get(position).getText());
        holder.imageView.setImageResource(tipsModels.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return tipsModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView3);

        }
    }
}
