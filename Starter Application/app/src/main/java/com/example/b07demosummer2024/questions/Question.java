package com.example.b07demosummer2024.questions;

import android.content.Context;

import com.example.b07demosummer2024.questions.response.Response;
import com.example.b07demosummer2024.questions.widget.Widget;

import java.util.LinkedHashMap;

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

    public abstract boolean isValid();

    public static boolean areAllValid(LinkedHashMap<String, Question> questions) {
        for (Question q: questions.values()) {
            if (!q.isValid())
                return false;
        }
        return true;
    }

    public Response getResponse() {
        return response;
    }

    // public abstract void setResponse(String response);

    public void setResponse() {
        widget.setResponse(response);
    }

    public void handler() {
        setResponse();
        widget.updateNotes(response);
    }

    public String getStatement() {
        return statement;
    }

    public abstract void buildWidget(Context context);

    public Widget getWidget() {
        return this.widget;
    }
}
