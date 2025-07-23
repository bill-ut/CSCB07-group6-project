package com.example.b07demosummer2024;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;
public class AnswerSaver {
    public static void saveAnswer(String questionId, String answer) {
        String uid = getUid();
        if (uid == null) return;
        DatabaseReference ref = FirebaseDatabase.getInstance()
            .getReference("users")
            .child(uid)
            .child("answers")
            .child("warmup")
            .child(questionId);

        ref.setValue(answer);
    }

    // Save all answers at once to /users/{uid}/answers/warmup
    public static void saveAllAnswers(Map<String, String> answers) {
        String uid = getUid();
        if (uid == null) return;

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(uid)
                .child("answers")
                .child("warmup");

        ref.setValue(answers);
    }

    // Helper to get current user ID
    private static String getUid() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            return auth.getCurrentUser().getUid();
        }
        return null;
    }
}
