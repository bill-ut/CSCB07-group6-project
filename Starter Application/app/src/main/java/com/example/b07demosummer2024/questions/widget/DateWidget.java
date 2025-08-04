package com.example.b07demosummer2024.questions.widget;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.content.ContextCompat;

import com.example.b07demosummer2024.R;
import com.example.b07demosummer2024.data.DataHandler;
import com.example.b07demosummer2024.data.Date;
import com.example.b07demosummer2024.questions.response.Response;
import com.example.b07demosummer2024.questions.response.SingleResponse;

public class DateWidget extends Widget {
    private final Date date;

    public DateWidget(Context context, String statement, Response response) {
        super(context, statement);
        this.widget = new DatePicker(new ContextThemeWrapper(context, R.style.SpinnerDatePicker));
        widget.setScaleX(0.75f);
        widget.setScaleY(0.75f);
        this.date = new Date();
        buildLayout(statement, response);
    }

    @Override
    protected void setWarning() {
        warning.setText(R.string.date_warning);
        warning.setTextColor(ContextCompat.getColor(context, R.color.red));
        warning.setTextSize(12.0F);
        warning.setVisibility(View.VISIBLE);
        warning.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )
        );
    }

    @Override
    public void setResponseValue(Response response) {
        ((SingleResponse) response).setResponse(date.getDate());
    }

    @Override
    public void setDisplay(String response) {
        Date dateResponse = DataHandler.stringToDate(response);
        ((DatePicker) this.widget).updateDate(
                dateResponse.getDay(),
                dateResponse.getMonth(),
                dateResponse.getYear()
        );
    }

    private void setDate(int day, int month, int year) {
        this.date.setDate(day, month, year);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void setHandler(Runnable handler) {
        DatePicker.OnDateChangedListener d = (view, year, monthOfYear, dayOfMonth) -> {
            setDate(dayOfMonth, monthOfYear, year);
            handler.run();
        };

        ((DatePicker) this.widget).setOnDateChangedListener(d);
    }
}
