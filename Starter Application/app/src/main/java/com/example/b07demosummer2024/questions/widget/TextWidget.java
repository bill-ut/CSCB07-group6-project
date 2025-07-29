package com.example.b07demosummer2024.questions.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.example.b07demosummer2024.questions.response.Response;

public class TextWidget extends Widget {
    public TextWidget(Context context, String statement, Response response) {
        super(context, statement, response);
        this.widget = new EditText(context);
        buildLayout(statement, response);
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
