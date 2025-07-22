package com.example.b07demosummer2024.questions.widget;

import android.view.View;

public abstract class Widget {
    View widget;

    public View getView() {
        return widget;
    }

    public abstract void setHandler(Runnable handler);
}
