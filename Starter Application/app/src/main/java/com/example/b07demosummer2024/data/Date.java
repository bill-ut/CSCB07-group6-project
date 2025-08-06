package com.example.b07demosummer2024.data;

import android.widget.DatePicker;

/**
 * Encapsulates the components of a date (day, month, year). Note for all linked methods, the month
 * is 0 indexed as according to {@link DatePicker#getDayOfMonth()}.
 */
public class Date {

    public final static char sep = '/';
    int day;
    int month;
    int year;

    /**
     * Default constructor, initializes all fields to 0.
     */
    public Date() {
        this.day = this.month = this.year = 0;
    }

    /**
     * Standard parameterized constructor.
     *
     * @param day The day of the month.
     * @param month The month of the year.
     * @param year The year.
     */
    public Date(int day, int month, int year) {
        setDate(day, month, year);
    }

    /**
     * sets the date to the provided fields.
     *
     * @param day The day of the month.
     * @param month The month of the year.
     * @param year The year.
     */
    public void setDate(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    /**
     * Gets the day of month.
     */
    public int getDay() {
        return day;
    }

    /**
     * Gets the month of year (0 indexed).
     */
    public int getMonth() {
        return month;
    }

    /**
     * Gets the year.
     */
    public int getYear() {
        return year;
    }

    /**
     * Gets the date as a string with format <code>D/M/yyyy</code>. Inverse operation of
     * {@link DataHandler#stringToDate(String)}.
     *
     * @return The date as a string.
     */
    public String getDate() {
        return String.valueOf(day) + sep + month + sep + year;
    }
}
