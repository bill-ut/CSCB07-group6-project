package com.example.b07demosummer2024.questions.response;

public class SingleResponse extends Response {
    private String response;

    public SingleResponse() {
        this.response = "";
    }

    public SingleResponse(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
