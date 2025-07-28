package com.example.b07demosummer2024.ui.reminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.b07demosummer2024.R;
import com.example.b07demosummer2024.data.Reminder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReminderFragment extends Fragment {

    private TimePicker timePicker;
    private Spinner frequencySpinner;
    private Button addReminderButton;
    private RecyclerView reminderList;

    private DatabaseReference remindersRef;
    private ReminderAdapter adapter;
    private List<Reminder> reminderItems = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reminders, container, false);

        timePicker = view.findViewById(R.id.timePicker);
        frequencySpinner = view.findViewById(R.id.frequencySpinner);
        addReminderButton = view.findViewById(R.id.btnAddReminder);
        reminderList = view.findViewById(R.id.reminderList);

        timePicker.setIs24HourView(true);

        // Spinner setup
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                getContext(), R.array.reminder_frequencies, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        frequencySpinner.setAdapter(spinnerAdapter);

        // Firebase setup
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        remindersRef = FirebaseDatabase.getInstance().getReference("reminders").child(uid);

        // RecyclerView
        adapter = new ReminderAdapter(reminderItems);
        reminderList.setLayoutManager(new LinearLayoutManager(getContext()));
        reminderList.setAdapter(adapter);

        loadReminders();

        addReminderButton.setOnClickListener(v -> addReminder());

        return view;
    }

    private void loadReminders() {
        remindersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reminderItems.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Reminder r = child.getValue(Reminder.class);
                    reminderItems.add(r);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load reminders.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addReminder() {
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();
        String time = String.format("%02d:%02d", hour, minute);
        String frequency = frequencySpinner.getSelectedItem().toString();
        String id = remindersRef.push().getKey();

        if (TextUtils.isEmpty(id)) return;

        Reminder reminder = new Reminder(id, time, frequency);
        remindersRef.child(id).setValue(reminder);
        scheduleAlarm(reminder, hour, minute);
        Toast.makeText(getContext(), "Reminder added", Toast.LENGTH_SHORT).show();
    }

    private void scheduleAlarm(Reminder reminder, int hour, int minute) {
        Context context = getContext();
        if (context == null) return;

        Intent intent = new Intent(context, ReminderReceiver.class);
        intent.putExtra("reminderText", "Tap here to revisit your plan");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, reminder.id.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long repeatInterval;
        switch (reminder.frequency.toLowerCase()) {
            case "weekly": repeatInterval = AlarmManager.INTERVAL_DAY * 7; break;
            case "monthly": repeatInterval = AlarmManager.INTERVAL_DAY * 30; break;
            default: repeatInterval = AlarmManager.INTERVAL_DAY; break;
        }

        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                repeatInterval,
                pendingIntent
        );
    }
}
