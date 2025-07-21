package com.example.b07demosummer2024.questions;

public class SingleResponse extends Response {
    private String response;

    public SingleResponse() {
        this.response = null;
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
