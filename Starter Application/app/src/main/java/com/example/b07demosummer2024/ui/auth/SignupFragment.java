package com.example.b07demosummer2024.ui.auth;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.b07demosummer2024.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import com.google.firebase.auth.FirebaseAuth;

public class SignupFragment extends Fragment {

    private FirebaseAuth mAuth;
    private TextInputEditText emailEt, passwordEt;
    private MaterialButton createAccBtn;
    private View toLoginLink;

    public SignupFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState
    ) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Wire up views
        emailEt      = view.findViewById(R.id.emailEditText);
        passwordEt   = view.findViewById(R.id.passwordEditText);
        createAccBtn = view.findViewById(R.id.createAccountButton);
        toLoginLink  = view.findViewById(R.id.toLoginText);

        // “Already have account? Sign in” → LoginFragment
        toLoginLink.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_signup_to_login)
        );

        // Attempt signup on button click
        createAccBtn.setOnClickListener(v -> attemptSignup());
    }

    private void attemptSignup() {
        String email    = emailEt.getText().toString().trim();
        String password = passwordEt.getText().toString();

        // Basic validation
        if (TextUtils.isEmpty(email)) {
            emailEt.setError("Email is required");
            return;
        }
        if (password.length() < 6) {
            passwordEt.setError("Min 6 characters");
            return;
        }

        // Firebase signup
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        // Navigate to HomeFragment
                        NavHostFragment.findNavController(this)
                                .navigate(R.id.action_signup_to_home);
                    } else {
                        Toast.makeText(
                                getContext(),
                                "Signup failed: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }
}
