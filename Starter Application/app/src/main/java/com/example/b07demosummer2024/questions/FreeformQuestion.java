package com.example.b07demosummer2024.questions;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.example.b07demosummer2024.questions.response.SingleResponse;
import com.example.b07demosummer2024.questions.widget.TextWidget;

public class FreeformQuestion extends Question {
    private final String type;

    public FreeformQuestion(String statement, String id, String type) {
        super(statement, id);
        this.type = type;
        this.response = new SingleResponse();
    }

    @Override
    public void setResponse() {
        ((SingleResponse) this.response).setResponse(
                ((EditText) this.widget.getView()).getText().toString().trim()
        );

        Log.d("Freeform Question", "Set Response: " + ((SingleResponse) this.response).getResponse());
    }

    @Override
    public void buildWidget(Context context) {
        this.widget = new TextWidget(context);
        this.widget.setHandler(this::setResponse);
    }
}
