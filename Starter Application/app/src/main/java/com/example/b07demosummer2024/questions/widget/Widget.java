package com.example.b07demosummer2024.questions.widget;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.b07demosummer2024.questions.response.Response;

/**
 * A generic widget object that serves as a container for displaying and managing the lower level
 * apis for a question. Responsible for managing the display components of the question.
 * <p>
 * Overall acts as a intermediary to facilitate communication between {@link Response} and
 * {@link com.example.b07demosummer2024.questions.Question}.
 */
public abstract class Widget {
    protected Context context;
    protected TextView text;
    protected View widget;
    protected TextView warning;
    private final LinearLayout layout;

    /**
     * Constructor which sets up the view elements of the widget.
     *
     * @param ctx The context to place the elements in.
     * @param statement The question statement.
     */
    public Widget(Context ctx, String statement) {
        this.context = ctx;
//        this.response = response;
        text = new TextView(ctx);
        layout = new LinearLayout(ctx);
        warning = new TextView(ctx);
        warning.setId(View.generateViewId());
        layout.setId(View.generateViewId());
        setQuestionHeader(statement);
        layout.setId(View.generateViewId());
    }

    /**
     * Sets the on click handler for the questions.
     *
     * @param handler The function to run after a user interaction with the view.
     */
    public abstract void setHandler(Runnable handler);

    /**
     * Sets the display in the user entry field to a default value.
     *
     * @param response The selected response to display as a string or encoded string.
     */
    public abstract void setDisplay(String response);

    /**
     * Sets the value of the underlying response object.
     *
     * @param response The response object to set.
     */
    public abstract void setResponseValue(Response response);

    /**
     * Initializes the warning label for invalid responses.
     */
    protected abstract void setWarning();

    /**
     * Places the display elements into the layout and initializes the text values
     *
     * @see #setQuestionHeader(String)
     * @see #updateNotes(Response)
     * @param statement The question statement.
     * @param response The response object.
     */
    public void buildLayout(String statement, Response response) {
        layout.setOrientation(LinearLayout.VERTICAL);
        setQuestionHeader(statement);

        layout.addView(text);
        layout.addView(widget);
        updateNotes(response);
    }

    /**
     * Changes the visibility of the warning note depending on the validity of the response.
     *
     * @param response The response object.
     */
    public void updateNotes(Response response) {
        setWarning();
        if (response.isValid()) {
            if (layout.findViewById(warning.getId()) != null) {
                layout.removeView(warning);
            }
        } else {
            if (layout.findViewById(warning.getId()) == null) {
                layout.addView(warning);
            }
        }
    }

    /**
     * Initializes the question statement header.
     *
     * @param statement The question statement.
     */
    private void setQuestionHeader(String statement) {
        text.setText(statement);
        text.setTextSize(16.0F);
        text.setVisibility(View.VISIBLE);
        text.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )
        );
    }

    /**
     * Gets the widget; that is the component of the display users can interact with.
     */
    public View getWidget() {
        return widget;
    }

    /**
     * Gets the entire display element.
     */
    public LinearLayout getView() {
        return layout;
    }
}
