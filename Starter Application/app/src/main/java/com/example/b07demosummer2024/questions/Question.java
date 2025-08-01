package com.example.b07demosummer2024.questions;

import android.content.Context;
import android.util.Pair;

import com.example.b07demosummer2024.questions.response.Response;
import com.example.b07demosummer2024.questions.widget.Widget;

import java.util.LinkedHashMap;

public abstract class Question {
    protected final String id;
    protected final String statement;
    protected Response response;
    protected Widget widget;
    Pair<String, Question> branch;

    public Question(String statement, String id) {
        this.id = id;
        this.statement = statement;
        this.response = null;
        this.branch = null;
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

    public void setResponse() {
        widget.setResponseValue(response);
    }

    public void handler() {
        setResponse();
        widget.updateNotes(response);
        updateBranch();
    }

    public String getStatement() {
        return statement;
    }

    public abstract void buildWidget(Context context, String defaultValue);

    public Widget getWidget() {
        return this.widget;
    }

    public abstract void updateBranch();

    public Response getBranchedResponse() {
        if (branch != null) {
            return branch.second.response;
        }
        return null;
    }

    public void setBranch(String linkedResponse, Question question) {
        branch = new Pair<>(linkedResponse, question);
    }

    public Pair<String, Question> getBranch() {
        return branch;
    }
}
