package com.example.b07demosummer2024.questions.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;

import com.example.b07demosummer2024.R;
import com.example.b07demosummer2024.questions.response.Response;

/**
 * Defines the freeform text based response widget. Includes additional getters to interact with the
 * <code>TextEdit</code> display.
 */
public class TextWidget extends Widget {

    /**
     * Generic constructor setting up the layout using the <code>TextEdit</code> view. See
     * {@link Widget#Widget(Context, String)} for the parent constructor.
     *
     * @param context The context to place the displays in.
     * @param statement The question statement.
     * @param response The locally stored response object.
     */
    public TextWidget(Context context, String statement, Response response) {
        super(context, statement);
        this.widget = new EditText(context);
        buildLayout(statement, response);
    }

    /**
     * Provides implementation for {@link Widget#setHandler(Runnable)}. Callback is made when text
     * is changed by the user.
     *
     * @param handler The function to run after a user interaction with the view.
     */
    @Override
    public void setHandler(Runnable handler) {
        ((EditText) this.widget).addTextChangedListener(
                new TextWatcher() {

                    @Override
                    public void afterTextChanged(Editable s) {
                        handler.run();
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}
                }
        );
    }

    /**
     * Provides implementation for {@link Widget#setDisplay(String)}.
     *
     * @param response The selected response to display as a string or encoded string.
     */
    @Override
    public void setDisplay(String response) {
        ((EditText) this.widget).setText(response);
    }

    /**
     * Provides implementation for {@link Widget#setResponseValue(Response)}. Sets response value
     * using the internal response object.
     *
     * @param response The response object to set.
     */
    @Override
    public void setResponseValue(Response response) {
        response.setValue(
                ((EditText) this.widget).getText().toString().trim()
        );
    }

    /**
     * Provides implementation for {@link Widget#setWarning()}.
     */
    @Override
    protected void setWarning() {
        warning.setText(R.string.text_warning);
        warning.setTextColor(ContextCompat.getColor(context, R.color.red));
        warning.setTextSize(12.0F);
        warning.setVisibility(View.VISIBLE);
        warning.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )
        );
    }

    /**
     * Gets the parsed user input from the <code>EditText</code> view.
     *
     * @return The parsed input from the user.
     */
    public String getText() {
        return ((EditText) this.widget).getText().toString().trim();
    }
}
