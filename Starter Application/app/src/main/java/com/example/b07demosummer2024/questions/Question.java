package com.example.b07demosummer2024.questions;

import android.content.Context;
import android.view.View;

import com.example.b07demosummer2024.questions.response.Response;
import com.example.b07demosummer2024.questions.widget.Widget;

public abstract class Question {
    protected final String id;
    protected final String statement;
    protected Response response;
    protected Widget widget;

    public Question(String statement, String id) {
        this.id = id;
        this.statement = statement;
        this.response = null;
    }

    public Response getResponse() {
        return response;
    }

    public abstract void setResponse();

    public String getStatement() {
        return statement;
    }

    public abstract void buildWidget(Context context);

    public Widget getWidget() {
        return this.widget;
    }
}
