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
import java.util.LinkedHashMap;

/**
 * Defines retrieval operations specifically for <code>.json</code> files. For a more general data
 * I/O class, see below.
 *
 * @see DataHandler
 */
public class JsonReader {

    /**
     * Loads a json file from Android assets.
     *
     * @param context The context containing the assets.
     * @param fileName The name of the file.
     * @return A string representing the j.son file.
     */
    public static String loadJSONFromAsset(Context context, String fileName) {
        String json;
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            int throwaway = is.read(buffer);
            Log.d("JsonReader", "Bytes Read: " + throwaway);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            Log.e("JsonReader", "Error reading JSON file from assets", ex);
            return null;
        }
        return json;
    }

    /**
     * Loads a json file and constructs a map of the questions defined by it.
     *
     * @param context The context containing the assets.
     * @param fileName The name of the file
     * @return An ordered map containing the question id and its corresponding question object.
     */
    public static LinkedHashMap<String, Question> getQuestionMap(Context context, String fileName) {
        LinkedHashMap<String, Question> questions = new LinkedHashMap<>();

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

                        if (questionObject.isNull("branches")) {
                            break;
                        }
                        JSONObject branchOptions = questionObject.getJSONObject("branches");

                        for (String s : ((SelectionQuestion) question).getChoices()) {
                            if (!branchOptions.isNull(s)) {
                                JSONArray branches = branchOptions.getJSONArray(s);
                                for (int i = 0; i < branches.length(); i++) {
                                    Log.d("JSONParser", "Added branch to " + branches.getString(i) + " from " + s);
                                    question.addBranch(s, branches.getString(i), null);
                                }
                            }
                        }

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
                        String format = questionObject.getString("selections");
                        question = new FreeformQuestion(questionText, key, format);
                        break;
                    default:
                        Log.e("JsonReader", "Unknown question type: " + type);
                        break;
                }

                if (question != null) {
                    Log.d("JsonReader", "Question of type " + question.getClass() +
                            " added, branches=" + question.getBranches().size());
                    questions.put(key, question);
                }
            }
        } catch (JSONException e) {
            Log.e("JsonReader", "Error parsing JSON", e);
        }

        Log.d("JsonReader", "Questions read: " + questions.size());

        return questions;
    }
}
