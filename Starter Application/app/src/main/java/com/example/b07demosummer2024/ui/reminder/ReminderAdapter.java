package com.example.b07demosummer2024.ui.reminder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.b07demosummer2024.R;
import com.example.b07demosummer2024.data.Reminder;

import java.util.List;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder> {
    private List<Reminder> reminders;
    private OnReminderActionListener listener;

    public interface OnReminderActionListener {
        void onEdit(Reminder reminder);
        void onDelete(Reminder reminder);
    }

    public ReminderAdapter(List<Reminder> reminders, OnReminderActionListener listener) {
        this.reminders = reminders;
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView timeText, freqText, contentText;
        Button editBtn, deleteBtn;

        public ViewHolder(View view) {
            super(view);
            timeText = view.findViewById(R.id.reminderTime);
            freqText = view.findViewById(R.id.reminderFreq);
            contentText = view.findViewById(R.id.reminderContent);
            editBtn = view.findViewById(R.id.btnEdit);
            deleteBtn = view.findViewById(R.id.btnDelete);
        }
    }

    @NonNull
    @Override
    public ReminderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reminder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reminder r = reminders.get(position);
        holder.timeText.setText(r.time);
        holder.freqText.setText(r.frequency);
        holder.contentText.setText(r.content);
        holder.editBtn.setOnClickListener(v -> listener.onEdit(r));
        holder.deleteBtn.setOnClickListener(v -> listener.onDelete(r));
    }

    @Override
    public int getItemCount() {
        return reminders.size();
    }
}

