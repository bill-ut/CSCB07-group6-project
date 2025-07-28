package com.example.b07demosummer2024.questions.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class SpinnerWidget extends Widget {
    public SpinnerWidget(Context context, ArrayList<String> choices) {
        Spinner sp = new Spinner(context);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        context,
                        android.R.layout.simple_spinner_dropdown_item,
                        choices
                );
        sp.setAdapter(adapter);

        this.widget = sp;
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
