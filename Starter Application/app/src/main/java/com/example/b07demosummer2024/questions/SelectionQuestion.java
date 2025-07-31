package com.example.b07demosummer2024.questions;

import android.content.Context;

import com.example.b07demosummer2024.questions.response.MultipleResponse;
import com.example.b07demosummer2024.questions.widget.CheckboxWidget;

import java.util.ArrayList;

public class SelectionQuestion extends Question {
    protected ArrayList<String> choices;

    public SelectionQuestion(String statement,
                             String id,
                             ArrayList<String> choices,
                             int maxSelections

    ) {
        super(statement, id);
        this.choices = choices;
        this.response = new MultipleResponse(maxSelections);
    }

    @Override
    public boolean isValid() {
        return response.isValid();
    }

    public void buildWidget(Context context) {
        this.widget = new CheckboxWidget(context, statement, response, choices);
        this.widget.setHandler(this::handler);
    }
}
