package com.example.b07demosummer2024.questions;

import android.content.Context;
import android.widget.LinearLayout;

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
    public void buildWidget(Context context, String defaultValue) {
        this.widget = new SpinnerWidget(context, statement, response, choices);
        if (defaultValue != null) {
            this.widget.setDisplay(defaultValue);
        }
        this.widget.setHandler(this::handler);
    }

    @Override
    public void updateBranch() {
        if (branch == null)
            return;

        LinearLayout ll = (LinearLayout) widget.getView().getParent();
        if (((SingleResponse) response).getResponse().equals(branch.first)) {
            ll.addView(branch.second.getWidget().getView(), ll.indexOfChild(widget.getView()));
        } else {
            ll.removeView(branch.second.getWidget().getView());
        }
    }
}
