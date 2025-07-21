package com.example.b07demosummer2024.questions;

import java.util.ArrayList;

public class FreeformQuestion extends Question {
    private final String type;

    public FreeformQuestion(String statement, String id, String type) {
        super(statement, id);
        this.type = type;
        this.response = new SingleResponse();
    }
}
