package com.example.b07demosummer2024.questions.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.example.b07demosummer2024.R;

import java.util.concurrent.Callable;

public class TextWidget extends Widget {
    public TextWidget(Context context) {
        EditText et = new EditText(context);

        this.widget = et;
    }

    public void setText(String text) {
        ((EditText) this.widget).setText(text);
    }

    @Override
    public void setHandler(Runnable handler) {
        ((EditText) this.widget).addTextChangedListener(
            new TextWatcher() {

                @Override
                public void afterTextChanged(Editable s) {
                    handler.run();
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}
            }
        );
    }
}
