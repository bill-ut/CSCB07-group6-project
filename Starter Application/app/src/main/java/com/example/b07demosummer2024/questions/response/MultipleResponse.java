package com.example.b07demosummer2024.questions.response;

import com.example.b07demosummer2024.data.DataHandler;

import java.util.HashSet;

public class MultipleResponse extends Response {
    private HashSet<String> response;
    private final int maxSelections;

    @Override
    public boolean isEmpty() {
        return response.isEmpty();
    }

    @Override
    public boolean isValid() {
        return !isEmpty() && response.size() <= maxSelections;
    }

    public MultipleResponse(int maxSelections) {
        this.response = new HashSet<>();
        this.maxSelections = maxSelections;
    }

    public HashSet<String> getResponse() {
        return response;
    }

    @Override
    public void setValue(String value) {
        this.response = new HashSet<>(DataHandler.stringToArray(value));
    }

    public int getMaxSelections() {
        return maxSelections;
    }

    public void addResponse(String response) {
        this.response.add(response);
    }

    public void removeResponse(String response) {
        this.response.remove(response);
    }
}
