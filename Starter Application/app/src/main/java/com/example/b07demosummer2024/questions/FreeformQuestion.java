package com.example.b07demosummer2024.questions;

import android.content.Context;

import com.example.b07demosummer2024.questions.response.SingleResponse;
import com.example.b07demosummer2024.questions.widget.DateWidget;
import com.example.b07demosummer2024.questions.widget.TextWidget;

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
    public void buildWidget(Context context) {
        if (type.equals("text"))
            this.widget = new TextWidget(context, statement, response);
        else
            this.widget = new DateWidget(context, statement, response); // TODO: implement date input
        this.widget.setHandler(this::handler);
    }
}
