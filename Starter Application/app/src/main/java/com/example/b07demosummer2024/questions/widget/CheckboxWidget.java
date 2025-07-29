package com.example.b07demosummer2024.questions.widget;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.LinearLayout;

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
