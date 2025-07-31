package com.example.b07demosummer2024.questions.widget;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.example.b07demosummer2024.R;
import com.example.b07demosummer2024.questions.response.Response;
import com.example.b07demosummer2024.questions.response.SingleResponse;

public class DateWidget extends Widget {
    private final Date date;

    public DateWidget(Context context, String statement, Response response) {
        super(context, statement, response);
        this.widget = new DatePicker(context);
        this.date = null;
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
    public void setResponse(Response response) {
        ((SingleResponse) response).setResponse(date.getDate());
    }

    private void setDate(int day, int month, int year) {
        this.date.setDate(day, month, year);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void setHandler(Runnable handler) {
        DatePicker.OnDateChangedListener d = (view, year, monthOfYear, dayOfMonth) -> {
            setDate(year, monthOfYear, dayOfMonth);
            handler.run();
        };

        ((DatePicker) this.widget).setOnDateChangedListener(d);
    }
}
