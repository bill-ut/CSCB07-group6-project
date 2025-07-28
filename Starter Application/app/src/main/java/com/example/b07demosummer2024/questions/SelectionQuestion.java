package com.example.b07demosummer2024.questions;

import android.content.Context;
import android.util.Log;
import android.widget.CheckBox;

import com.example.b07demosummer2024.questions.response.MultipleResponse;
import com.example.b07demosummer2024.questions.response.SingleResponse;
import com.example.b07demosummer2024.questions.widget.CheckboxWidget;

import java.util.ArrayList;

public class SelectionQuestion extends Question {
    protected ArrayList<String> choices;
    protected final int maxSelections;

    public SelectionQuestion(String statement,
                             String id,
                             ArrayList<String> choices,
                             int maxSelections

    ) {
        super(statement, id);
        this.maxSelections = maxSelections;
        this.choices = choices;
        this.response = new MultipleResponse();
    }

    public ArrayList<String> getChoices() {
        return choices;
    }

    @Override
    public void setResponse() {
        for (CheckBox checkbox: ((CheckboxWidget) this.widget).getChildren()) {
            if (checkbox.isChecked())
                ((MultipleResponse) this.response).addResponse(checkbox.getText().toString());
            else
                ((MultipleResponse) this.response).removeResponse(checkbox.getText().toString());
        }

        Log.d("Checkbox Question", "Set Response: " + ((MultipleResponse) this.response).getResponse().toString());
    }

    public void buildWidget(Context context) {
        this.widget = new CheckboxWidget(context, choices);
        this.widget.setHandler(this::setResponse);
    }
}
