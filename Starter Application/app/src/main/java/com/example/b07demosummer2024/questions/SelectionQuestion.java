package com.example.b07demosummer2024.questions;

import android.content.Context;
import android.util.Log;
import android.widget.LinearLayout;

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

    public void buildWidget(Context context, String defaultValue) {
        this.widget = new CheckboxWidget(context, statement, response, choices);
        if (defaultValue != null) {
            this.widget.setDisplay(defaultValue);
        }
        this.widget.setHandler(this::handler);
    }

    @Override
    public void updateBranch() {
        Log.d("SelectionQuestion", "Attempting to update branch");
        if (branch == null)
            return;

        LinearLayout ll = (LinearLayout) widget.getView().getParent();
        if (((MultipleResponse) response).getResponse().contains("Yes") && response.isValid()) {
            ll.addView(branch.second.getWidget().getView(), ll.indexOfChild(widget.getView()) + 1);
        } else {
            ll.removeView(branch.second.getWidget().getView());
        }
    }
}
