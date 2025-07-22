package com.example.b07demosummer2024.data;

import android.content.Context;
import android.util.Log;

import com.example.b07demosummer2024.questions.*;

import org.json.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;

public class JsonReader {

    public String loadJSONFromAsset(Context context, String fileName) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            Log.e("JsonReader", "Error reading JSON file from assets", ex);
            return null;
        }
        return json;
    }

    public HashMap<String, Question> getQuestionMap(Context context, String fileName) {
        HashMap<String, Question> questions = new HashMap<>();

        try {
            String jsonString = loadJSONFromAsset(context, fileName);
            if (jsonString == null) {
                return questions;
            }

            JSONObject root = new JSONObject(jsonString);
            Iterator<String> keys = root.keys();

            while (keys.hasNext()) {
                String key = keys.next();
                JSONObject questionObject = root.getJSONObject(key);

                String type = questionObject.getString("type");
                String questionText = questionObject.getString("question");

                Question question = null;

                switch (type) {
                    case "choices": {
                        JSONArray choicesArray = questionObject.getJSONArray("choices");
                        ArrayList<String> choices = new ArrayList<>();
                        for (int i = 0; i < choicesArray.length(); i++) {
                            choices.add(choicesArray.getString(i));
                        }

                        int maxSelections = questionObject.getInt("selections");

                        question = new SelectionQuestion(questionText, key, choices, maxSelections);
                        break;
                    }
                    case "dropdown": {
                        JSONArray choicesArray = questionObject.getJSONArray("choices");
                        ArrayList<String> choices = new ArrayList<>();
                        for (int i = 0; i < choicesArray.length(); i++) {
                            choices.add(choicesArray.getString(i));
                        }

                        question = new DropdownQuestion(questionText, key, choices);
                        break;
                    }
                    case "freeform":
                        String format = questionObject.getString("format");
                        question = new FreeformQuestion(questionText, key, format);
                        break;
                    default:
                        Log.e("JsonReader", "Unknown question type: " + type);
                        break;
                }

                if (question != null) {
                    questions.put(key, question);
                }
            }
        } catch (JSONException e) {
            Log.e("JsonReader", "Error parsing JSON", e);
        }

        return questions;
    }
}
