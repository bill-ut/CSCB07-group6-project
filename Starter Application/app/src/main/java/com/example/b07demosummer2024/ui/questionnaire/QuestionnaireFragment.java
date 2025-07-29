package com.example.b07demosummer2024.ui.questionnaire;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.b07demosummer2024.R;
import com.example.b07demosummer2024.data.JsonReader;
import com.example.b07demosummer2024.questions.Question;
import com.example.b07demosummer2024.questions.response.Response;
import com.example.b07demosummer2024.AnswerSaver;

import java.util.LinkedHashMap;

import java.util.Map;
import android.widget.Toast;

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

        View submitButton = view.findViewById(R.id.submitAnswersButton);
        submitButton.setOnClickListener(v -> {
            if (!Question.areAllValid(questions)) {
                Toast.makeText(getContext(), "Invalid response to one or more questions", Toast.LENGTH_SHORT).show();
                return;
            }

            Map<String, Response> answers = new LinkedHashMap<>();

            for (Map.Entry<String, Question> entry : questions.entrySet()) {
                Question q = entry.getValue();
                q.setResponse(); // updates internal response from UI
                if (q.getResponse() != null) {
                    answers.put(entry.getKey(), q.getResponse());
                }
            }

            AnswerSaver.saveAllAnswers(answers);
            Toast.makeText(getContext(), "Answers saved successfully!", Toast.LENGTH_SHORT).show();
        });
    }

    private void displayQuestion(Question q) {
        layout.addView(q.getWidget().getView());
    }
}
