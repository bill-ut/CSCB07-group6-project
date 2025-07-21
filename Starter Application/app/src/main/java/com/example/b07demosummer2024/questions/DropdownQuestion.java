package com.example.b07demosummer2024.questions;

import java.util.ArrayList;

public class DropdownQuestion extends Question {
    protected ArrayList<String> choices;

    public DropdownQuestion(String statement, ArrayList<String> choices) {
        super(statement);
        this.choices = choices;
        this.response = new MultipleResponse();
    }
}
