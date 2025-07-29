package com.example.b07demosummer2024.questions.widget;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.b07demosummer2024.R;
import com.example.b07demosummer2024.questions.response.Response;

public abstract class Widget {
    protected TextView text;
    protected View widget;
    protected TextView tips;
    private final LinearLayout layout;

    public Widget(Context ctx, String statement, Response response) {
        text = new TextView(ctx);
        layout = new LinearLayout(ctx);
        tips = new TextView(ctx);
        tips.setId(View.generateViewId());
        setQuestionHeader(statement);
    }

    public LinearLayout getView() {
        return layout;
    }

    public View getWidget() {
        return widget;
    }

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

    public void setTips(Response response) { // TODO: parameters will likely change
        if (!response.isEmpty()) {
            tips.setText(R.string.no_tips);
            tips.setTextSize(16.0F);
            tips.setVisibility(View.VISIBLE);
            tips.setLayoutParams(
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    )
            );
        }
    }

    public TextView getTips() {
        return tips;
    }

    public void buildLayout(String statement, Response response) {
        layout.setOrientation(LinearLayout.VERTICAL);
        setQuestionHeader(statement);

        layout.addView(text);
        layout.addView(widget);
        updateTips(response);
    }

    public void updateTips(Response response) {
        setTips(response);
        if (response.isEmpty()) {
            if (layout.findViewById(tips.getId()) != null)
                layout.removeView(getTips());
        } else {
            if (layout.findViewById(tips.getId()) == null) {
                layout.addView(getTips());
            }
        }
    }

    public abstract void setHandler(Runnable handler);
}
