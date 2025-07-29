package com.example.b07demosummer2024.questions;

import android.content.Context;
import android.util.Log;
import android.widget.Spinner;

import com.example.b07demosummer2024.questions.response.SingleResponse;
import com.example.b07demosummer2024.questions.widget.SpinnerWidget;

import java.util.ArrayList;

public class DropdownQuestion extends Question {
    protected ArrayList<String> choices;

    public DropdownQuestion(String statement, String id, ArrayList<String> choices) {
        super(statement, id);
        this.choices = choices;
        this.response = new SingleResponse();
    }

    @Override
    public boolean isValid() {
        return !response.isEmpty();
    }

    @Override
    public void setResponse() {
        ((SingleResponse) this.response).setResponse(
                ((Spinner) this.widget.getWidget()).getSelectedItem().toString()
        );

        Log.d("Dropdown Question", "Set Response: " + ((SingleResponse) this.response).getResponse());
    }

    @Override
    public void buildWidget(Context context) {
        this.widget = new SpinnerWidget(context, statement, response, choices);
        this.widget.setHandler(this::handler);
    }
}
