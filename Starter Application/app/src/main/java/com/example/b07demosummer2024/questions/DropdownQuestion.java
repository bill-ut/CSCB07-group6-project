package com.example.b07demosummer2024.questions;

import android.content.Context;

import com.example.b07demosummer2024.questions.response.Response;
import com.example.b07demosummer2024.questions.response.SingleResponse;
import com.example.b07demosummer2024.questions.widget.SpinnerWidget;

import java.util.ArrayList;

/**
 * Defines the dropdown type of question. Allows user select a single response from a dropdown list
 * of options.
 */
public class DropdownQuestion extends Question {
    protected ArrayList<String> choices;

    /**
     * Standard parameterized constructor. See {@link Question#Question(String, String)} for the
     * generic constructor.
     *
     * @param statement The statement of the question.
     * @param id The question id.
     * @param choices The choices which can be chosen from.
     */
    public DropdownQuestion(String statement, String id, ArrayList<String> choices) {
        super(statement, id);
        this.choices = choices;
        this.response = new SingleResponse();
    }

    /**
     * Provides implementation for {@link Question#buildWidget(Context, String)}.
     *
     * @param context The context the widget should be placed in.
     * @param defaultValue A default response to be displayed and locally stored.
     */
    @Override
    public void buildWidget(Context context, String defaultValue) {
        this.widget = new SpinnerWidget(context, statement, response, choices);
        if (defaultValue != null) {
            this.widget.setDisplay(defaultValue);
        }
        this.widget.setHandler(this::handler);
    }

    /**
     * Not supported. No implementation is provided.
     */
    @Override
    public void updateBranch() {
        // does nothing
    }

    /**
     * Provides implementation for {@link Question#isValid()}. Implemented using
     * {@link Response#isEmpty()}.
     *
     * @return <code>true</code> iff the response is not empty, <code>false</code> otherwise.
     */
    @Override
    public boolean isValid() {
        return !response.isEmpty();
    }
}
