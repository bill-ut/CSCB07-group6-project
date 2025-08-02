package com.example.b07demosummer2024;

import android.util.Log;
import com.example.b07demosummer2024.questions.Question;
import com.example.b07demosummer2024.questions.response.Response;
import com.example.b07demosummer2024.questions.response.SingleResponse;
import com.example.b07demosummer2024.questions.response.MultipleResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
public class AnswerSaver {
    //Save one single response to a question
    public static void saveAnswer(String questionId, Response response) {
        String uid = getUid();
        if (uid == null || response == null) return;
        DatabaseReference ref = FirebaseDatabase.getInstance()
            .getReference("users")
            .child(uid)
            .child(questionId);

        Object value = convertResponseToStorableValue(response);
        if (value != null) {
            ref.setValue(value);
        }
    }

    // Save multiple responses at once
    public static void saveAllAnswers(Map<String, Response> responses) {
        String uid = getUid();
        if (uid == null) return;

        Map<String, Object> converted = new HashMap<>();
        for (Map.Entry<String, Response> entry : responses.entrySet()) {
            Object value = convertResponseToStorableValue(entry.getValue());
            if (value != null) {
                converted.put(entry.getKey(), value);
            }
        }

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(uid)
                .child("answers");

        ref.setValue(converted);
    }

    // Convert a Response object to something Firebase can store; Get the actual response inside Response Object
    private static Object convertResponseToStorableValue(Response response) {
        if (response instanceof SingleResponse) {
            return ((SingleResponse) response).getResponse();
        } else if (response instanceof MultipleResponse) {
            return new ArrayList<>(((MultipleResponse) response).getResponse());
        }
        return null;
    }

    // Firebase user ID helper
    private static String getUid() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            return auth.getCurrentUser().getUid();
        }
        return null;
    }
}
