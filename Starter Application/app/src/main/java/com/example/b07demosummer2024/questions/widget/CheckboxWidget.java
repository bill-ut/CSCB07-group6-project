package com.example.b07demosummer2024.questions.widget;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;

import com.example.b07demosummer2024.R;
import com.example.b07demosummer2024.questions.response.MultipleResponse;
import com.example.b07demosummer2024.questions.response.Response;

import java.util.ArrayList;

public class CheckboxWidget extends Widget {
    ArrayList<CheckBox> checkboxes;

    public CheckboxWidget(Context context, String statement, Response response,
                          ArrayList<String> choices) {
        super(context, statement, response);
        this.checkboxes = new ArrayList<>();

        LinearLayout ll = new LinearLayout(context);
        ll.setOrientation(LinearLayout.VERTICAL);
        for (String choice: choices) {
            CheckBox checkbox = new CheckBox(context);
            checkbox.setText(choice);
            ll.addView(checkbox);
            checkboxes.add(checkbox);
        }

        this.widget = ll;
        buildLayout(statement, response);
    }

    @Override
    protected void setWarning() {
        int max = ((MultipleResponse) response).getMaxSelections();
        warning.setText(
            context
             .getResources()
             .getQuantityString(R.plurals.checkbox_warning, max, max)
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

    public ArrayList<CheckBox> getChildren() {
        return this.checkboxes;
    }

    @Override
    public void setHandler(Runnable handler) {
        for (CheckBox checkbox: this.checkboxes) {
            checkbox.setOnCheckedChangeListener(
                (buttonView, isChecked) -> handler.run()
            );
        }
    }
}
