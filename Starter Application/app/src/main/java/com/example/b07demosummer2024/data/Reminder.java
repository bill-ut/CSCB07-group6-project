package com.example.b07demosummer2024.data;

public class Reminder {
    public String id;
    public String time;      // format: HH:mm
    public String frequency; // values: daily, weekly, monthly

    public Reminder() {
        // Required by Firebase
    }

    public Reminder(String id, String time, String frequency) {
        this.id = id;
        this.time = time;
        this.frequency = frequency;
    }
}
