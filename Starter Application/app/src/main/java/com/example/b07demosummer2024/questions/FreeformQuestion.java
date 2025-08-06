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
    public void buildWidget(Context context, String defaultValue) {
        if (type.equals("text"))
            this.widget = new TextWidget(context, statement, response);
        else
            this.widget = new DateWidget(context, statement, response);

        if (defaultValue != null && !defaultValue.isEmpty()) {
            this.widget.setDisplay(defaultValue);
        }

        this.widget.setHandler(this::handler);
    }

    @Override
    public void updateBranch() {
        // does nothing, FreeformQuestions have no branching logic
    }
}
