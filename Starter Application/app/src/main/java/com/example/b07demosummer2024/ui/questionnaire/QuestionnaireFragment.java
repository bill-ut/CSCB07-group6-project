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
import com.example.b07demosummer2024.data.JsonReader;
import com.example.b07demosummer2024.questions.Question;

import java.util.LinkedHashMap;

public class QuestionnaireFragment extends Fragment {
    static final String QUESTIONS_FILE = "questions.json";
    LinkedHashMap<String, Question> questions;
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

        if (getView() == null) {
            Log.e("QuestionnaireFragment", "View not found");
        }

        layout = getView().findViewById(R.id.questionnaireLayout);

        this.questions = JsonReader.getQuestionMap(getContext(), QUESTIONS_FILE);
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
