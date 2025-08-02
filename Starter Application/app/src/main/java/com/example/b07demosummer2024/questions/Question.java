package com.example.b07demosummer2024.questions;

import android.content.Context;
import android.util.Pair;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.example.b07demosummer2024.questions.response.Response;
import com.example.b07demosummer2024.questions.widget.Widget;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public abstract class Question {
    protected final String id;
    protected final String statement;
    protected Response response;
    protected Widget widget;
    LinkedHashMap<String, Pair<String, Question>> branches; // maps id to <response, question>
    LinearLayout branchLayout;

    public Question(String statement, String id) {
        this.id = id;
        this.statement = statement;
        this.response = null;
        this.branches = new LinkedHashMap<>();
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

    public ArrayList<Response> getBranchedResponses() {
        ArrayList<Response> responses = new ArrayList<>();
        if (!branches.isEmpty()) {
            branches.forEach(
                    (k,v) -> responses.add(v.second.getResponse())
            );
            return responses;
        }
        return null;
    }

    public void addBranch(String linkedResponse, String id, final Question question) {
        branches.put(id, new Pair<>(linkedResponse, question));
    }

    public LinkedHashMap<String, Pair<String, Question>> getBranches() {
        return branches;
    }

    public void buildBranch(Context context) {
        branchLayout = new LinearLayout(context);
        branchLayout.setOrientation(LinearLayout.VERTICAL);
    }

    public LinearLayout getBranchLayout() {
        return branchLayout;
    }

    @NonNull
    @Override
    public String toString() {
        return "<Question: id=" + id + ", Statement=" + statement + ">";
    }
}
