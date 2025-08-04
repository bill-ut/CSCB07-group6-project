package com.example.b07demosummer2024.ui.emergency;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.b07demosummer2024.R;
import com.example.b07demosummer2024.emergency.SafeLocation;

import java.util.List;

public class SafeLocationAdapter extends RecyclerView.Adapter<SafeLocationAdapter.ViewHolder> {

    private List<SafeLocation> locationList;
    private OnLocationActionListener listener;

    public interface OnLocationActionListener {
        void onEditLocation(SafeLocation location);
        void onDeleteLocation(SafeLocation location);
    }

    public SafeLocationAdapter(List<SafeLocation> locationList, OnLocationActionListener listener) {
        this.locationList = locationList;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView locationName, address, notes;
        public Button editButton, deleteButton;

        public ViewHolder(View view) {
            super(view);
            locationName = view.findViewById(R.id.locationNameText);
            address = view.findViewById(R.id.locationAddressText);
            notes = view.findViewById(R.id.locationNotesText);
            editButton = view.findViewById(R.id.editButton);
            deleteButton = view.findViewById(R.id.deleteButton);
        }
    }

    @NonNull
    @Override
    public SafeLocationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_safe_location, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SafeLocationAdapter.ViewHolder holder, int position) {
        SafeLocation location = locationList.get(position);
        holder.locationName.setText(location.getLocationName());
        holder.address.setText(location.getAddress());

        // Handle empty notes
        if (location.getNotes() != null && !location.getNotes().isEmpty()) {
            holder.notes.setText(location.getNotes());
            holder.notes.setVisibility(View.VISIBLE);
        } else {
            holder.notes.setVisibility(View.GONE);
        }

        if (listener != null) {
            holder.editButton.setOnClickListener(v -> listener.onEditLocation(location));
            holder.deleteButton.setOnClickListener(v -> listener.onDeleteLocation(location));
        }
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }

    public void setLocations(List<SafeLocation> newList) {
        this.locationList = newList;
        notifyDataSetChanged();
    }
}
