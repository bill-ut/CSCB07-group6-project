package com.example.b07demosummer2024.questions.widget;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;

import com.example.b07demosummer2024.R;
import com.example.b07demosummer2024.data.DataHandler;
import com.example.b07demosummer2024.questions.response.MultipleResponse;
import com.example.b07demosummer2024.questions.response.Response;

import java.util.ArrayList;

/**
 * Defines the multiple choices type question. No addition methods are defined.
 */
public class CheckboxWidget extends Widget {
    ArrayList<CheckBox> checkboxes;
    int maxResponses;

    /**
     * Generic constructor setting up the layout using an array of <code>CheckBox</code> views. Additionally
     * defines the array of choices the user can choose from. See
     * {@link Widget#Widget(Context, String)} for the parent constructor.
     *
     * @param context The context to place the displays in.
     * @param statement The question statement.
     * @param response The locally stored response object.
     * @param choices The question choices.
     */
    public CheckboxWidget(Context context, String statement, Response response,
                          ArrayList<String> choices) {
        super(context, statement);
        assert response instanceof MultipleResponse;

        this.checkboxes = new ArrayList<>();
        this.maxResponses = ((MultipleResponse) response).getMaxSelections();
        this.widget = new LinearLayout(context);
        ((LinearLayout) this.widget).setOrientation(LinearLayout.VERTICAL);
        for (String choice: choices) {
            CheckBox checkbox = new CheckBox(context);
            checkbox.setText(choice);
            ((LinearLayout) this.widget).addView(checkbox);
            checkboxes.add(checkbox);
        }


        buildLayout(statement, response);
    }

    /**
     * Provides implementation for {@link Widget#setHandler(Runnable)}. Callback is made when user
     * ticks any checkbox.
     *
     * @param handler The function to run after a user interaction with the view.
     */
    @Override
    public void setHandler(Runnable handler) {
        for (CheckBox checkbox: this.checkboxes) {
            checkbox.setOnCheckedChangeListener(
                    (buttonView, isChecked) -> handler.run()
            );
        }
    }

    /**
     * Provides implementation for {@link Widget#setDisplay(String)}. Decodes the string and updates
     * each checkbox individually depending on the responses present.
     *
     * @param response The selected response to display as a string or encoded string.
     */
    @Override
    public void setDisplay(String response) {
        ArrayList<String> responses = DataHandler.stringToArray(response);

        for (CheckBox checkbox : checkboxes) {
            if (responses.contains(checkbox.getText().toString())) {
                checkbox.setChecked(true);
            }
        }
    }

    /**
     * Provides implementation for {@link Widget#setResponseValue(Response)}. Sets response value
     * using the internal response object.
     *
     * @param response The response object to set.
     */
    @Override
    public void setResponseValue(Response response) {
        for (CheckBox checkbox: checkboxes) {
            if (checkbox.isChecked())
                ((MultipleResponse) response).addResponse(checkbox.getText().toString());
            else
                ((MultipleResponse) response).removeResponse(checkbox.getText().toString());
        }
    }

    /**
     * Provides implementation for {@link Widget#setWarning()}.
     */
    @Override
    protected void setWarning() {
        warning.setText(
            context
             .getResources()
             .getQuantityString(R.plurals.checkbox_warning, maxResponses, maxResponses)
        );
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
}
