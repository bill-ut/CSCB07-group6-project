package com.example.b07demosummer2024.data;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.b07demosummer2024.questions.*;
import com.example.b07demosummer2024.data.JsonReader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.*;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DataHandler {
    static final String QUESTIONS_FILE = "questions.json";
    private Map<String, JSONObject> questionsById;

    /*
    Usage (firebase db forces async access):
        new DataHandler(getContext(), dh -> {
            ...
            dh.getAnswer(question);
            ...
        });
     */
    public DataHandler(Context context, Consumer<DataHandler> onComplete ) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid;
        if (auth.getCurrentUser() != null) {
            uid = auth.getCurrentUser().getUid();
        } else {
            Log.e("DataHandler", "User not logged in");
            return;
        }
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users")
                .child(uid)
                .child("answers");

        String jsonString = JsonReader.loadJSONFromAsset(context, QUESTIONS_FILE);
        if (jsonString == null) {
            Log.e("DataHandler", "Error reading JSON file");
            return;
        }
        try {
            JSONObject root = new JSONObject(jsonString);
            questionsById = new LinkedHashMap<>();
            Iterator<String> keys = root.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                JSONObject questionObject = root.getJSONObject(key);
                questionsById.put(key, questionObject);
            }
            ref.get().addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    Log.e("DataHandler", "Error getting data", task.getException());
                    onComplete.accept(this);
                    return;
                }

                DataSnapshot snapshot = task.getResult();
                for (Map.Entry<String, JSONObject> entry : questionsById.entrySet()) {
                    String questionId = entry.getKey();
                    JSONObject questionObject = entry.getValue();

                    Object value = snapshot.child(questionId).getValue();
                    if (value != null) {
                        try {
                            JSONArray answers;
                            if (value instanceof List) {
                                answers = new JSONArray((List<?>) value);
                            } else if (value instanceof String && ((String) value).startsWith("[") && ((String) value).endsWith("]")) {
                                answers = new JSONArray((String) value);
                            } else {
                                answers = new JSONArray();
                                answers.put(value.toString());
                            }
                            questionObject.put("answer", answers);
                        } catch (JSONException e){
                            Log.e("DataHandler", "Error fetching answer for question:" + questionId, e);
                        }
                    }
                }
                onComplete.accept(this);
            });
        } catch (JSONException e) {
            Log.e("DataHandler", "Error parsing JSON", e);
        }
    }

    public ArrayList<String> getAnswerByQuestion(Question question) {
        return getAnswerById(question.getId());
    }
    public ArrayList<String> getAnswerById(String questionId) {
        JSONObject questionObject = questionsById.get(questionId);
        if (questionObject == null) {
            return new ArrayList<>();
        }

        String rawAnswer = questionObject.optString("answer", "[]");
        return stringToArray(rawAnswer);
    }

    public String getTipByQuestion(Question question) {
        return getTipById(question.getId());
    }
    public String getTipById(String questionId) {
        JSONObject questionObject = questionsById.get(questionId);
        if (questionObject == null) {
            return "";
        }

        String template = null;

        try {
            JSONObject tipObj = questionObject.getJSONObject("tips");

            if (tipObj.getString("type").equals("template")) {
                template = tipObj.getString("template");
            } else if (tipObj.getString("type").equals("per_choice")) {
                JSONObject data = tipObj.getJSONObject("data");
                template = data.getString(getAnswerById(questionId).get(0).replaceAll("\"", ""));
            } else {
                Log.e("DataHandler", "Unknown tip type: " + tipObj.getString("type"));
                return "";
            }

        } catch (JSONException e) {
            Log.d("DataHandler", "Error getting tips for question: " + questionId, e);
            return "";
        }

        return populateTemplate(template);
    }

    public String populateTemplate(String tipTemplate) {
        String patternString = "\\{(\\w+)\\}";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(tipTemplate);

        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            String varName = matcher.group(1);
            List<String> values = getAnswerById(varName);

            String replacement;
            if (values == null || values.isEmpty()) {
                replacement = "[missing answer]";
            } else if (values.size() == 1) {
                replacement = values.get(0).replaceAll("\"", "");
            } else {
                replacement = joinWithCommasAnd(values);
            }
            matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(result);
        return result.toString();
    }

    private String joinWithCommasAnd(List<String> values) {
        if (values.size() <= 1) {
            return String.join("", values);
        }

        if (values.size() == 2) {
            return values.get(0).replaceAll("\"", "")
                    + " and "
                    + values.get(1).replaceAll("\"", "");
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < values.size(); i++) {
            if (i > 0) {
                sb.append(i == values.size() - 1 ? ", and " : ", ");
            }
            sb.append(values.get(i).replaceAll("\"", ""));
        }
        return sb.toString();
    }

    public static ArrayList<String> stringToArray(String s) {
        ArrayList<String> parsed = new ArrayList<>();

        if (s == null || s.trim().isEmpty()) {
            return parsed;
        } else if (s.startsWith("[") && s.endsWith("]")) {
            String[] elements = s.substring(1, s.length() - 1).split(",\\s*");
            for (String el : elements) {
                parsed.add(el.trim());
            }
        } else {
            parsed.add(s);
        }
        return parsed;
    }
}
