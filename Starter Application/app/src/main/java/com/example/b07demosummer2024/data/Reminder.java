package com.example.b07demosummer2024.data;

public class Reminder {
    public String id;
    public String time;
    public String frequency;
    public String content;

    public Reminder() {}  // Requi  red for Firebase

    public Reminder(String id, String time, String frequency, String content) {
        this.id = id;
        this.time = time;
        this.frequency = frequency;
        this.content = content;
    }
}
