package com.example.b07demosummer2024.ui.auth;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import androidx.annotation.NonNull;

/**
 * Real implementation that calls FirebaseAuth.
 */
public class FirebaseAuthRepository implements AuthRepository {
    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    public void signIn(
            String email,
            String password,
            AuthCallback callback
    ) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            callback.onSuccess(task.getResult());
                        } else {
                            callback.onFailure(task.getException());
                        }
                    }
                });
    }
}
