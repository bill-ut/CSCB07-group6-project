package com.example.b07demosummer2024.questions;

import android.content.Context;
import android.util.Log;
import android.util.Pair;

import com.example.b07demosummer2024.questions.response.MultipleResponse;
import com.example.b07demosummer2024.questions.widget.CheckboxWidget;

import java.util.ArrayList;
import java.util.LinkedHashMap;

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

    public ArrayList<String> getChoices() {
        return choices;
    }

    @Override
    public boolean isValid() {
        return response.isValid();
    }

    public void buildWidget(Context context, String defaultValue) {
        Log.d("abcde", "building widget for: " + statement);
        this.widget = new CheckboxWidget(context, statement, response, choices);
        if (defaultValue != null) {
            this.widget.setDisplay(defaultValue);
        }
        this.widget.setHandler(this::handler);
    }

    @Override
    public void updateBranch() {
        if (branches.isEmpty()) {
            return;
        }

        for (LinkedHashMap.Entry<String, Pair<String, Question>> entry : branches.entrySet()) {
            branchLayout.removeView(entry.getValue().second.getWidget().getView());
            branchLayout.removeView(entry.getValue().second.getBranchLayout());

            Log.d("Selection", "Adding branches<validResponse=" + response.isValid() + " target=" + entry.getValue().first);
            if (((MultipleResponse) response).getResponse().contains(entry.getValue().first)
                    && response.isValid()) {
                Log.d("Selection", "Added branch");
                branchLayout.addView(entry.getValue().second.getWidget().getView());
                branchLayout.addView(entry.getValue().second.getBranchLayout());
            }
        }
    }
}
