package com.example.b07demosummer2024.questions.widget;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.core.content.ContextCompat;

import com.example.b07demosummer2024.R;
import com.example.b07demosummer2024.questions.response.Response;

import java.util.ArrayList;

public class SpinnerWidget extends Widget {
    public SpinnerWidget(Context context, String statement, Response response, ArrayList<String> choices) {
        super(context, statement, response);
        Spinner sp = new Spinner(context);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        context,
                        android.R.layout.simple_spinner_dropdown_item,
                        choices
                );
        sp.setAdapter(adapter);

        this.widget = sp;
        buildLayout(statement, response);
    }

    @Override
    protected void setWarning() {
        warning.setText(R.string.spinner_warning);
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
    public void setHandler(Runnable handler) {
        ((Spinner) this.widget).setOnItemSelectedListener(
            new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    handler.run();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }
            }
        );
    }
}
