package com.example.b07demosummer2024.questions;

import java.util.ArrayList;

public class SelectionQuestion extends Question {
    protected ArrayList<String> choices;
    protected final int maxSelections;

    public SelectionQuestion(String statement,
                             String id,
                             ArrayList<String> choices,
                             int maxSelections

    ) {
        super(statement, id);
        this.maxSelections = maxSelections;
        this.choices = choices;
        this.response = new MultipleResponse();
    }
}
