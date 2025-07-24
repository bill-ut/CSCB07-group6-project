package com.example.b07demosummer2024;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.net.Uri;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(
                R.layout.activity_home_fragment,   // or your existing home layout file
                container,
                false
        );

        Button questionRedirect = view.findViewById(R.id.questionnaireRedirect);
        Button logoutBtn = view.findViewById(R.id.logoutButton);
        questionRedirect.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_home_to_questionnaireFragment)
        );
        logoutBtn.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_home_to_login)
        );

        Button testSaveBtn = view.findViewById(R.id.testSaveButton);
        testSaveBtn.setOnClickListener(v -> {
            Map<String, String> answers = new HashMap<>();
            answers.put("q1", "Sometimes");
            answers.put("q2", "Not In Relationship");
            answers.put("q3", "Never");
            AnswerSaver.saveAllAnswers(answers);
        });

        return view;
    }
}
