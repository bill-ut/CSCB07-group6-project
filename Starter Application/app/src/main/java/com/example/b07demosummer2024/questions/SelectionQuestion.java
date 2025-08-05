package com.example.b07demosummer2024.questions;

import android.content.Context;
import android.util.Log;
import android.util.Pair;

import com.example.b07demosummer2024.questions.response.MultipleResponse;
import com.example.b07demosummer2024.questions.response.Response;
import com.example.b07demosummer2024.questions.widget.CheckboxWidget;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Defines the multiple choice of type questions. Enables user to pick from a selection of
 * responses, allowing for more than one valid selection at a time.
 */
public class SelectionQuestion extends Question {
    protected ArrayList<String> choices;

    /**
     * Standard parameterized constructor. See {@link Question#Question(String, String)} for the
     * generic constructor.
     *
     * @param statement The statement of the question.
     * @param id The question id.
     * @param choices The choices which can be chosen from.
     * @param maxSelections The max number of choices that can be made.
     */
    public SelectionQuestion(String statement,
                             String id,
                             ArrayList<String> choices,
                             int maxSelections

    ) {
        super(statement, id);
        this.choices = choices;
        this.response = new MultipleResponse(maxSelections);
    }

    /**
     * Provides implementation for {@link Question#buildWidget(Context, String)}.
     *
     * @param context The context the widget should be placed in.
     * @param defaultValue A default response to be displayed and locally stored.
     */
    public void buildWidget(Context context, String defaultValue) {
        this.widget = new CheckboxWidget(context, statement, response, choices);
        if (defaultValue != null) {
            this.widget.setDisplay(defaultValue);
        }
        this.widget.setHandler(this::handler);
    }

    /**
     * Provides implementation for {@link Question#updateBranch()}.
     */
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

    /**
     * Provides implementation for {@link Question#isValid()}. Delegates responsibility to
     * {@link Response#isValid()}.
     *
     * @return <code>true</code> iff the response is valid, <code>false</code> otherwise.
     */
    @Override
    public boolean isValid() {
        return response.isValid();
    }

    /**
     * Get the array of choices.
     */
    public ArrayList<String> getChoices() {
        return choices;
    }
}
