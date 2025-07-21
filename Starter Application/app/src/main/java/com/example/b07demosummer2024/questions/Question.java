package com.example.b07demosummer2024.questions;

public abstract class Question {
    protected final String id;
    protected final String statement;
    protected Response response;

    public Question(String statement, String id) {
        this.id = id;
        this.statement = statement;
        this.response = null;
    }

    public Response getResponse() {
        return response;
    }

    public String getStatement() {
        return statement;
    }
}
