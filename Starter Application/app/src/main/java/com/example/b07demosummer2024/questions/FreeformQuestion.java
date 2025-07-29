package com.example.b07demosummer2024.questions;

import android.content.Context;
import android.widget.EditText;

import com.example.b07demosummer2024.questions.response.SingleResponse;
import com.example.b07demosummer2024.questions.widget.TextWidget;

import java.util.Objects;

public class FreeformQuestion extends Question {
    private final String type;

    public FreeformQuestion(String statement, String id, String type) {
        super(statement, id);
        this.type = type;
        this.response = new SingleResponse();
    }

    @Override
    public boolean isValid() {
        return !response.isEmpty();
    }

    @Override
    public void setResponse() {
        ((SingleResponse) this.response).setResponse(
                ((EditText) this.widget.getWidget()).getText().toString().trim()
        );
    }

    @Override
    public void buildWidget(Context context) {
        if (Objects.equals(type, "text"))
            this.widget = new TextWidget(context, statement, response);
        else
            this.widget = new TextWidget(context, statement, response); // TODO: implement date input
        this.widget.setHandler(this::handler);
    }
}
