package com.example.b07demosummer2024.questions;

import java.util.ArrayList;

public class MultipleResponse extends Response {
    private ArrayList<String> response;

    public MultipleResponse() {
        this.response = null;
    }

    public MultipleResponse(ArrayList<String> response) {
        this.response = response;
    }

    public ArrayList<String> getResponse() {
        return response;
    }

    public void addResponse(String response) {
        this.response.add(response);
    }

    public void removeResponse(String response) {
        this.response.remove(response);
    }
}
