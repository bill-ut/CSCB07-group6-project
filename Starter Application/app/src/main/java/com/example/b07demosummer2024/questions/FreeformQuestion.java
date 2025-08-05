package com.example.b07demosummer2024.questions;

import android.content.Context;

import com.example.b07demosummer2024.questions.response.Response;
import com.example.b07demosummer2024.questions.response.SingleResponse;
import com.example.b07demosummer2024.questions.widget.DateWidget;
import com.example.b07demosummer2024.questions.widget.TextWidget;

/**
 * Defines the generic text input type of questions. Allows user to input an arbitrary response.
 * Also includes the date based responses.
 */
public class FreeformQuestion extends Question {
    private final String type;

    /**
     * Standard parameterized constructor. See {@link Question#Question(String, String)} for the
     * generic constructor.
     *
     * @param statement The statement of the question.
     * @param id The question id.
     * @param type Either "text" if the response is generic or "date" if the response is a date.
     */
    public FreeformQuestion(String statement, String id, String type) {
        super(statement, id);
        this.type = type;
        this.response = new SingleResponse();
    }

    /**
     * Provides implementation for {@link Question#buildWidget(Context, String)}. Type of widget
     * depends on the <code>type</code> given at construction.
     *
     * @param context The context the widget should be placed in.
     * @param defaultValue A default response to be displayed and locally stored.
     */
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

    /**
     * Not supported. No implementation is provided.
     */
    @Override
    public void updateBranch() {
        // does nothing, FreeformQuestions have no branching logic
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
