package com.example.b07demosummer2024;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import java.util.HashMap;
import java.util.Map;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.example.b07demosummer2024.R;

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

        // 1. Find the TextView
        TextView tvEmail = view.findViewById(R.id.tvEmail);

        // 2. Get current user and display their email
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null && user.getEmail() != null) {
            tvEmail.setText(user.getEmail());
        } else {
            tvEmail.setText("Not signed in");
        }

        Button questionRedirect = view.findViewById(R.id.questionnaireRedirect);
        Button tipsRedirectBtn = view.findViewById(R.id.tipsRedirect);
        Button logoutBtn = view.findViewById(R.id.logoutButton);

        questionRedirect.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_home_to_questionnaireFragment)
        );
        tipsRedirectBtn.setOnClickListener(v->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_home_to_tips));
        logoutBtn.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_home_to_login)
        );
        // inside onCreateView or onViewCreated, after findViewById for testSaveBtn:
        Button supportBtn = view.findViewById(R.id.supportButton);
        supportBtn.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_home_to_support)
        );


        Button emergencyInfoButton = view.findViewById(R.id.emergencyInfoRedirect);
        emergencyInfoButton.setOnClickListener(v -> {
            NavHostFragment.findNavController(HomeFragment.this)
                    .navigate(R.id.action_homeFragment_to_emergencyInfoFragment);
        });

        Button remindersButton = view.findViewById(R.id.remindersRedirect);
        remindersButton.setOnClickListener(v -> {
            NavHostFragment.findNavController(HomeFragment.this)
                    .navigate(R.id.action_homeFragment_to_reminderFragment);
        });

        return view;
    }
}
