package com.example.b07demosummer2024.data;

public class Date {
    public static char sep = '/';
    int day;
    int month;
    int year;

    public Date() {
        this.day = this.month = this.year = 0;
    }

    public Date(int day, int month, int year) {
        setDate(day, month, year);
    }

    public void setDate(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public String getDate() {
        return String.valueOf(day) + sep + month + sep + year;
    }
}
