package com.example.b07demosummer2024.questions.widget;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.core.content.ContextCompat;

import com.example.b07demosummer2024.R;
import com.example.b07demosummer2024.questions.response.Response;

import java.util.ArrayList;

/**
 * Defines the dropdown question type widget. No additional methods are defined.
 */
public class SpinnerWidget extends Widget {
    private final ArrayList<String> choices;

    /**
     * Generic constructor setting up the layout using the <code>Spinner</code> view. Additionally
     * defines the array of choices the user can choose from. See
     * {@link Widget#Widget(Context, String)} for the parent constructor.
     *
     * @param context The context to place the displays in.
     * @param statement The question statement.
     * @param response The locally stored response object.
     * @param choices The question choices.
     */
    public SpinnerWidget(Context context, String statement, Response response, ArrayList<String> choices) {
        super(context, statement);
        Spinner sp = new Spinner(context);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        context,
                        android.R.layout.simple_spinner_dropdown_item,
                        choices
                );
        sp.setAdapter(adapter);

        this.widget = sp;
        this.choices = choices;
        buildLayout(statement, response);
    }

    /**
     * Provides implementation for {@link Widget#setHandler(Runnable)}. Callback is made when user
     * selects an item.
     *
     * @param handler The function to run after a user interaction with the view.
     */
    @Override
    public void setHandler(Runnable handler) {
        ((Spinner) this.widget).setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        handler.run();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                        // your code here
                    }
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
        ((Spinner) this.widget).setSelection(choices.indexOf(response));
    }

    /**
     * Provides implementation for {@link Widget#setResponseValue(Response)}. Sets response value
     * using the internal response object.
     *
     * @param response The response object to set.
     */
    @Override
    public void setResponseValue(Response response) {
        (response).setValue(
                ((Spinner) this.widget).getSelectedItem().toString()
        );
    }

    /**
     * Provides implementation for {@link Widget#setWarning()}.
     */
    @Override
    protected void setWarning() {
        warning.setText(R.string.spinner_warning);
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
