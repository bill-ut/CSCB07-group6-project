package com.example.b07demosummer2024.ui.questionnaire;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;


import com.example.b07demosummer2024.R;
import com.example.b07demosummer2024.data.DataHandler;
import com.example.b07demosummer2024.data.JsonReader;
import com.example.b07demosummer2024.questions.Question;
import com.example.b07demosummer2024.questions.response.Response;
import com.example.b07demosummer2024.AnswerSaver;
import com.example.b07demosummer2024.questions.response.SingleResponse;

import java.util.Arrays;
import java.util.LinkedHashMap;

import java.util.List;
import java.util.Map;

import android.widget.TextView;
import android.widget.Toast;

public class QuestionnaireFragment extends Fragment {
    static final String QUESTIONS_FILE = "questions.json";
    static final String BRANCH_QUESTIONS = "questions-branches.json";
    static final String FOLLOWUP_QUESTIONS = "questions-followup.json";
    LinkedHashMap<String, Question> questions;
    LinkedHashMap<String, Question> branchQuestions;
    LinkedHashMap<String, Question> followupQuestions;
    private LinearLayout layout;
    TextView header2;


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

    @RequiresApi(api = Build.VERSION_CODES.VANILLA_ICE_CREAM)
    public void onViewCreated(
        @NonNull View view,
        @Nullable Bundle savedInstanceState
    ) {
        super.onViewCreated(view, savedInstanceState);
        new DataHandler(getContext(), dh -> {
            if (getView() == null) {
                Log.e("QuestionnaireFragment", "View not found");
            }

            layout = getView().findViewById(R.id.questionnaireLayout);

            this.branchQuestions = JsonReader.getQuestionMap(getContext(), BRANCH_QUESTIONS);

            this.questions = JsonReader.getQuestionMap(getContext(), QUESTIONS_FILE);
            this.followupQuestions = JsonReader.getQuestionMap(getContext(), FOLLOWUP_QUESTIONS);

            // setup questions
            addHeader("Section 1:");
            setupQuestions(questions, dh);
            header2 = addHeader("Section 2:");
            Question first = questions.firstEntry().getValue();
            layout.removeView(first.getBranchLayout());
            layout.addView(first.getBranchLayout(), layout.indexOfChild(header2) + 1);
            addHeader("Section 3:");
            setupQuestions(followupQuestions, dh);

            View submitButton = view.findViewById(R.id.submitAnswersButton);
            View.OnClickListener saver = v -> {
                if (Question.hasInvalid(questions) || Question.hasInvalid(followupQuestions)) {
                    Toast.makeText(getContext(), "Invalid response to one or more questions", Toast.LENGTH_SHORT).show();
                    return;
                }

                Map<String, Response> answers = new LinkedHashMap<>();

                List<Map<String, Question>> allQuestions = Arrays.asList(
                        questions,
                        branchQuestions,
                        followupQuestions
                );

                for (Map<String, Question> questionMap : allQuestions) {
                    for (Map.Entry<String, Question> entry : questionMap.entrySet()) {
                        Question q = entry.getValue();
                        q.setResponse(); // updates internal response from UI
                        if (entry.getKey().equals("leave_timing")) {
                            SingleResponse response = (SingleResponse) q.getResponse();
                            if (response.getResponse().equals("0/0/0")) { continue; }
                        }

                        if (q.getResponse() != null) {
                            answers.put(entry.getKey(), q.getResponse());
                        }
                    }
                }

                AnswerSaver.saveAllAnswers(answers);
                Toast.makeText(getContext(), "Answers saved successfully!", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(v)
                        .navigate(R.id.action_questionnaireFragment_to_home);
            };

            submitButton.setOnClickListener(saver);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.VANILLA_ICE_CREAM)
    private void setupQuestions(LinkedHashMap<String, Question> questionList, DataHandler dh) {

        for (Question q: questionList.values()) {
            setupQuestion(q, false, dh);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.VANILLA_ICE_CREAM)
    private void setupQuestion(Question q, boolean isBranch, DataHandler dh) {
        String savedResponse = DataHandler.cleanString(dh.getAnswerByQuestion(q).toString());

        q.buildWidget(getContext(), savedResponse);
        q.setResponseValue(savedResponse);
        q.buildBranch(getContext());

        if (!q.getBranches().isEmpty()) {
            for (Map.Entry<String, Pair<String, Question>> entry : q.getBranches().sequencedEntrySet()) {
                Question branchQuestion = branchQuestions.get(entry.getKey());
                entry.setValue(new Pair<>(entry.getValue().first, branchQuestion));
                assert branchQuestion != null;
                setupQuestion(branchQuestion, true, dh);
            }
        }

        if (!isBranch) {
            layout.addView(q.getWidget().getView());
            layout.addView(q.getBranchLayout(),
                    layout.indexOfChild(q.getWidget().getView()) + 1);
        }

        q.updateBranch();
        q.getWidget().updateNotes(q.getResponse());
    }

    private TextView addHeader(String header) {
        TextView text = new TextView(getContext());

        text.setText(header);
        text.setTextSize(32.0F);
        text.setVisibility(View.VISIBLE);
        text.setLayoutParams(
            new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        );
        text.setId(View.generateViewId());
        layout.addView(text);
        return text;
    }
}
