package com.example.b07demosummer2024.questions.widget;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.b07demosummer2024.questions.response.Response;

public abstract class Widget {
    protected Context context;
    protected TextView text;
    protected View widget;
    protected TextView warning;
    private final LinearLayout layout;

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

    public LinearLayout getView() {
        return layout;
    }

    public View getWidget() {
        return widget;
    }

    public abstract void setDisplay(String response);

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

    protected abstract void setWarning();

    public void buildLayout(String statement, Response response) {
        layout.setOrientation(LinearLayout.VERTICAL);
        setQuestionHeader(statement);

        layout.addView(text);
        layout.addView(widget);
        updateNotes(response);
    }
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

    public abstract void setResponseValue(Response response);

    public abstract void setHandler(Runnable handler);
}
