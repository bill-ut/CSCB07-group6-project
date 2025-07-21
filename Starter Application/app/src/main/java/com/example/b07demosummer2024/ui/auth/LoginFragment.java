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
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.fragment.NavHostFragment;

import com.example.b07demosummer2024.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends Fragment {

    private FirebaseAuth mAuth;
    private TextInputEditText emailEt, passwordEt;
    private MaterialButton signInBtn;
    private View toSignupLink;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState
    ) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        emailEt       = view.findViewById(R.id.emailEditText);
        passwordEt    = view.findViewById(R.id.passwordEditText);
        signInBtn     = view.findViewById(R.id.signInButton);
        toSignupLink  = view.findViewById(R.id.toSignupText);

        // Navigate to signup
        toSignupLink.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_login_to_signup)
        );

        // Attempt login
        signInBtn.setOnClickListener(v -> attemptSignIn());
    }

    private void attemptSignIn() {
        String email    = emailEt.getText().toString().trim();
        String password = passwordEt.getText().toString();

        if (TextUtils.isEmpty(email)) {
            emailEt.setError("Email is required");
            return;
        }
        if (password.length() < 6) {
            passwordEt.setError("Min 6 characters");
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), task -> {
                    boolean isFirstLogin = false; // TODO: replace with whether user has answered any questions.

                    if (task.isSuccessful()) {
                        NavController nav = NavHostFragment.findNavController(this);

                        if (isFirstLogin) {
                            nav.navigate(R.id.action_login_to_questionnaireFragment);
                        }
                        nav.navigate(R.id.action_login_to_home);
                    } else {
                        Toast.makeText(
                                getContext(),
                                "Login failed: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }
}
