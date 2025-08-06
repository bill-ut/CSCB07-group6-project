package com.example.b07demosummer2024.tips;

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

/**
 * Defines an adapter class for a {@link RecyclerView}.
 */
public class TipsRecyclerViewAdapter extends RecyclerView.Adapter<TipsRecyclerViewAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<TipsModel> tipsModels;

    /**
     * Defines a holder for the views to be placed in the recycler view.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        /**
         * Standard constructor to locally store View objects.
         *
         * @param itemView The item view.
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView3);

        }
    }

    /**
     * Default parameterized constructor.
     *
     * @param context The context to display the tips in.
     * @param tipsModels An array of tips.
     */
    public TipsRecyclerViewAdapter(Context context, ArrayList<TipsModel> tipsModels) {
        this.context = context;
        this.tipsModels = tipsModels;
    }

    /**
     * Provides implementation for {@link RecyclerView.Adapter#onCreateViewHolder(ViewGroup, int)}.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return Returns the adapted ViewHolder.
     */
    @NonNull
    @Override
    public TipsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_row, parent, false);
        return new TipsRecyclerViewAdapter.ViewHolder(view);
    }

    /**
     * Provides implementation for
     * {@link RecyclerView.Adapter#onBindViewHolder(RecyclerView.ViewHolder, int)}.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull TipsRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.textView.setText(tipsModels.get(position).getText());
        holder.imageView.setImageResource(tipsModels.get(position).getImage());
    }

    /**
     * Provides implementation for {@link RecyclerView.Adapter#getItemCount()}.
     *
     * @return The number of tips held.
     */
    @Override
    public int getItemCount() {
        return tipsModels.size();
    }
}
