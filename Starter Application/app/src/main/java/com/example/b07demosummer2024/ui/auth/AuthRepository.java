package com.example.b07demosummer2024.ui.auth;

import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;

/**
 * Wraps FirebaseAuth into a testable interface.
 */
public interface AuthRepository {
    /**
     * Attempt to sign in with email/password.
     * @param email    user email
     * @param password user password
     * @param callback to notify success/failure
     */
    void signIn(
            String email,
            String password,
            AuthCallback callback
    );

    interface AuthCallback {
        void onSuccess(AuthResult result);
        void onFailure(Exception e);
    }
}
