package com.example.b07demosummer2024.questions.response;

public class SingleResponse extends Response {
    private String response;

    @Override
    public boolean isEmpty() {
        return response.isEmpty();
    }

    @Override
    public boolean isValid() {
        return !isEmpty();
    }

    public SingleResponse() {
        this.response = "";
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @Override
    public void setValue(String value) {
        this.response = value;
    }
}
