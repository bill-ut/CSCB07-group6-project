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
import androidx.navigation.fragment.NavHostFragment;

import com.example.b07demosummer2024.R;
import com.example.b07demosummer2024.data.EncryptedPrefsProvider;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class LoginFragment extends Fragment implements LoginContract.View {
    private LoginContract.Presenter presenter;
    private TextInputEditText emailEt, passwordEt;
    private MaterialButton signInBtn;
    private View toSignupLink;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new LoginPresenter(new FirebaseAuthRepository());
    }

    @Nullable
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

        emailEt      = view.findViewById(R.id.emailEditText);
        passwordEt   = view.findViewById(R.id.passwordEditText);
        signInBtn    = view.findViewById(R.id.signInButton);
        toSignupLink = view.findViewById(R.id.toSignupText);

        toSignupLink.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_login_to_signup)
        );

        signInBtn.setOnClickListener(v ->
                presenter.onLoginClicked(
                        emailEt.getText().toString().trim(),
                        passwordEt.getText().toString()
                )
        );
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.attachView(this);
    }

    @Override
    public void onStop() {
        presenter.detachView();
        super.onStop();
    }

    // --- LoginContract.View methods ---

    @Override
    public void showEmailError(String msg) {
        emailEt.setError(msg);
    }

    @Override
    public void showPasswordError(String msg) {
        passwordEt.setError(msg);
    }

    @Override
    public void showProgress(boolean visible) {
        // TODO: toggle a ProgressBar in your layout
    }

    @Override
    public void showGeneralError(String msg) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLoginSuccess() {
        // After Firebase auth, decide PIN vs Home
        NavController nav = NavHostFragment.findNavController(this);
        try {
            EncryptedPrefsProvider prefs = new EncryptedPrefsProvider(requireContext());
            String savedPin = prefs.getPin();
            if (TextUtils.isEmpty(savedPin)) {
                nav.navigate(R.id.action_login_to_pin);
            } else {
                nav.navigate(R.id.action_login_to_home);
            }
        } catch (Exception e) {
            // fallback
            nav.navigate(R.id.action_login_to_home);
        }
    }
}
