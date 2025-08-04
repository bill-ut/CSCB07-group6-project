package com.example.b07demosummer2024.ui.auth;

import android.text.TextUtils;

/**
 * Presenter handles input validation and delegates auth calls.
 */
public class LoginPresenter implements LoginContract.Presenter {
    private LoginContract.View view;
    private final AuthRepository repo;

    public LoginPresenter(AuthRepository repo) {
        this.repo = repo;
    }

    @Override
    public void attachView(LoginContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public void onLoginClicked(String email, String password) {
        if (view == null) return;

        boolean valid = true;
        if (TextUtils.isEmpty(email)) {
            view.showEmailError("Email is required");
            valid = false;
        }
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            view.showPasswordError("Password must be at least 6 characters");
            valid = false;
        }
        if (!valid) return;

        view.showProgress(true);
        repo.signIn(email, password, new AuthRepository.AuthCallback() {
            @Override
            public void onSuccess(com.google.firebase.auth.AuthResult result) {
                if (view == null) return;
                view.showProgress(false);
                view.onLoginSuccess();
            }

            @Override
            public void onFailure(Exception e) {
                if (view == null) return;
                view.showProgress(false);
                view.showGeneralError(e.getMessage());
            }
        });
    }
}
