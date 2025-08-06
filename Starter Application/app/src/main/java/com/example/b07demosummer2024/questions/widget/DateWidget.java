package com.example.b07demosummer2024.questions.widget;

import static android.os.Build.VERSION_CODES.R;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.content.ContextCompat;

import com.example.b07demosummer2024.data.DataHandler;
import com.example.b07demosummer2024.data.Date;
import com.example.b07demosummer2024.questions.response.Response;

/**
 * Defines the date type question widget. Includes setter to access the internal date object.
 */
public class DateWidget extends Widget {
    private final Date date;

    /**
     * Generic constructor setting up the layout using the <code>DatePicker</code> view. Also defines
     * the date object, used to encapsulate I/O operations made using a date-like structure. See
     * {@link Widget#Widget(Context, String)} for the parent constructor.
     *
     * @param context The context to place the displays in.
     * @param statement The question statement.
     * @param response The locally stored response object.
     */
    public DateWidget(Context context, String statement, Response response) {
        super(context, statement);
        this.widget = new DatePicker(new ContextThemeWrapper(context,
                com.example.b07demosummer2024.R.style.SpinnerDatePicker));
        widget.setScaleX(0.75f);
        widget.setScaleY(0.75f);
        this.date = new Date();
        buildLayout(statement, response);
    }

    /**
     * Provides implementation for {@link Widget#setHandler(Runnable)}. Callback is made when user
     * changes a dial on the date wheel, either the day, month or year.
     *
     * @param handler The function to run after a user interaction with the view.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void setHandler(Runnable handler) {
        DatePicker.OnDateChangedListener d = (view, year, monthOfYear, dayOfMonth) -> {
            setDate(dayOfMonth, monthOfYear, year);
            handler.run();
        };

        ((DatePicker) this.widget).setOnDateChangedListener(d);
    }

    /**
     * Provides implementation for {@link Widget#setDisplay(String)}. Decodes the string and writes
     * it to the date display. Note that month is 0 indexed.
     *
     * @param response The selected response to display as a string or encoded string.
     */
    @Override
    public void setDisplay(String response) {
        Date dateResponse = DataHandler.stringToDate(response);
        ((DatePicker) this.widget).updateDate(
                dateResponse.getYear(),
                dateResponse.getMonth(),
                dateResponse.getDay()
        );
    }

    /**
     * Provides implementation for {@link Widget#setResponseValue(Response)}. Sets response value
     * using the internal response object.
     *
     * @param response The response object to set.
     */
    @Override
    public void setResponseValue(Response response) {
        response.setValue(date.getDate());
    }

    /**
     * Provides implementation for {@link Widget#setWarning()}.
     */
    @Override
    protected void setWarning() {
        warning.setText(com.example.b07demosummer2024.R.string.date_warning);
        warning.setTextColor(ContextCompat.getColor(context,
                com.example.b07demosummer2024.R.color.red));
        warning.setTextSize(12.0F);
        warning.setVisibility(View.VISIBLE);
        warning.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )
        );
    }

    /**
     * Sets the date object. Relegates duties to {@link Date#setDate(int, int, int)}. Note that the
     * month is 0 indexed.
     *
     * @param day The day of month.
     * @param month The month of the year.
     * @param year The year.
     */
    private void setDate(int day, int month, int year) {
        this.date.setDate(day, month, year);
    }
}
