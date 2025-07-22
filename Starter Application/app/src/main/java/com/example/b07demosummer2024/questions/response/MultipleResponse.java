package com.example.b07demosummer2024.questions.response;

import java.util.HashSet;

public class MultipleResponse extends Response {
    private HashSet<String> response;

    public MultipleResponse() {
        this.response = new HashSet<>();
    }

    public MultipleResponse(HashSet<String> response) {
        this.response = response;
    }

    public HashSet<String> getResponse() {
        return response;
    }

    public void addResponse(String response) {
        this.response.add(response);
    }

    public void removeResponse(String response) {
        this.response.remove(response);
    }
}
