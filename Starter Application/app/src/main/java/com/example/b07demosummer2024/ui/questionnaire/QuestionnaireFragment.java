package com.example.b07demosummer2024.ui.questionnaire;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.b07demosummer2024.R;
import com.example.b07demosummer2024.questions.DropdownQuestion;
import com.example.b07demosummer2024.questions.Question;
import com.example.b07demosummer2024.QuestionParser; // temp
import com.example.b07demosummer2024.questions.SelectionQuestion;

import java.util.HashMap;

public class QuestionnaireFragment extends Fragment {
    HashMap<String, Question> questions;
    private LinearLayout layout;


    public QuestionnaireFragment() {
        // empty constructor
    }

    @Override
    public View onCreateView(
        @NonNull LayoutInflater inflater,
        ViewGroup container,
        Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_questionnaire, container, false);
    }

    public void onViewCreated(
        @NonNull View view,
        @Nullable Bundle savedInstanceState
    ) {
        super.onViewCreated(view, savedInstanceState);

        layout = getView().findViewById(R.id.questionnaireLayout);

        this.questions = QuestionParser.readFile();
        for (Question q: questions.values()) {
            q.buildWidget(getContext());
        }
        Log.d("Questionnaire", "Array size: " + questions.size());

        for (Question q: questions.values()) {
            displayQuestion(q);
        }
    }

    private void displayQuestion(Question q) {
        Log.d("Questionnaire", "displayQuestion() called");

        // display text
        TextView text = new TextView(getContext());
        text.setText(q.getStatement());
        text.setTextSize(16.0F);
        text.setVisibility(View.VISIBLE);
        text.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )
        );

        layout.addView(text);
        layout.addView(q.getWidget().getView());
    }
}
