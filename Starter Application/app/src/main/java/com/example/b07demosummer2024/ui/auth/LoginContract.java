package com.example.b07demosummer2024.ui.auth;

/**
 * MVP contract between the View (LoginFragment) and Presenter.
 */
public interface LoginContract {
    interface View {
        void showEmailError(String msg);
        void showPasswordError(String msg);
        void showProgress(boolean visible);
        void showGeneralError(String msg);
        void onLoginSuccess();
    }

    interface Presenter {
        void attachView(View view);
        void detachView();
        void onLoginClicked(String email, String password);
    }
}
