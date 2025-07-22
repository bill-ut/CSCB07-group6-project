package com.example.b07demosummer2024.questions.widget;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class CheckboxWidget extends Widget {
    ArrayList<CheckBox> checkboxes;

    public CheckboxWidget(Context context, ArrayList<String> choices) {
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
