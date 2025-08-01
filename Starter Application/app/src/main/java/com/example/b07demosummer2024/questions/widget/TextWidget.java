package com.example.b07demosummer2024.questions.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;

import com.example.b07demosummer2024.R;
import com.example.b07demosummer2024.questions.response.Response;
import com.example.b07demosummer2024.questions.response.SingleResponse;

public class TextWidget extends Widget {
    public TextWidget(Context context, String statement, Response response) {
        super(context, statement);
        this.widget = new EditText(context);
        buildLayout(statement, response);
    }

    @Override
    protected void setWarning() {
        warning.setText(R.string.text_warning);
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

    public void setText(String text) {
        ((EditText) this.widget).setText(text);
    }

    public String getText() {
        return ((EditText) this.widget).getText().toString().trim();
    }

    @Override
    public void setResponseValue(Response response) {
        ((SingleResponse) response).setResponse(
            ((EditText) this.widget).getText().toString().trim()
        );
    }

    @Override
    public void setDisplay(String response) {
        ((EditText) this.widget).setText(response);
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
