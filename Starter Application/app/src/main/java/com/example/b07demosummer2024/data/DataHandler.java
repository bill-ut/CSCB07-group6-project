package com.example.b07demosummer2024.data;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.example.b07demosummer2024.questions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.*;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Defines various operations to facilitate I/O between both <code>.json</code> files and Firebase.
 * Also bundles together many parsing tools to encode and decode data for Firebase storage and
 * retrieval or <code>.json</code> files. For lower level <code>.json</code> access, see below.
 *
 * @see JsonReader
 */
public class DataHandler {

    static final String[] QUESTIONS_FILES = {
            "questions.json",
            "questions-branches.json",
            "questions-followup.json"
    };
    private Map<String, JSONObject> questionsById;

    /**
     * Removes leading and trailing brackets and double quotes from the string. Intended to clean up
     * artifacts after reading from Firebase.
     * @param s The string to clean.
     * @return The same string with leading and trailing brackets and double quotes removed.
     */
    public static String cleanString(String s) {
        if (s == null || s.length() < 2) {
            return "";
        }

        String trimmed = s.substring(1, s.length() - 1);
        String[] parts = trimmed.split(",");

        List<String> cleaned = new ArrayList<>();
        for (String part : parts) {
            cleaned.add(part.replaceAll("[\"\\\\]", "").trim());
        }

        return String.join(", ", cleaned);
    }

    /**
     * Correction method to adjust for Android's 0 indexed month to display in tips.
     *
     * @param dateStr The encoded string of a date.
     * @return The same string with the month incremented by one.
     */
    @SuppressLint("DefaultLocale")
    public static String fixZeroIndexedMonth(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            Log.d("DataHandler", "Input date string is null or empty.");
        }

        assert dateStr != null;
        String[] parts = dateStr.split("/");

        if (parts.length != 3) {
            Log.d("DataHandler", "Invalid date format. Expected format: dd/MM/yyyy");
        }

        try {
            int day = Integer.parseInt(parts[0].trim());
            int month = Integer.parseInt(parts[1].trim()) + 1;  // fix the 0-indexed month
            int year = Integer.parseInt(parts[2].trim());

            return String.format("%02d/%02d/%04d", day, month, year);
        } catch (NumberFormatException e) {
            Log.d("DataHandler", "Date string contains non-numeric values.");
            return null;
        }
    }

    /**
     * Joins a list of strings with commands and "and"'s.
     *
     * @param values The array of strings to join.
     * @return The string representing the joined array of strings.
     */
    private static String joinWithCommasAnd(List<String> values) {
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

    /**
     * Converts a comma separated string to an <code>ArrayList</code>.
     *
     * @param s The comma separated string.
     * @return An array with the strings.
     */
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

    /**
     * Coverts a date encoded string described by the return of {@link Date#getDate()} (these are
     * inverse operations) to a Date
     * object.
     *
     * @param s The encoded date string.
     * @return A date object representing the string.
     */
    public static Date stringToDate(String s) {
        Log.d("Data", "Raw string: " + s);
        String cleaned = s.replaceAll("[\\[\\]\"]", "").replace("\\/", "/");
        Log.d("Data", "Parsed string: " + cleaned);
        String[] components = cleaned.split(String.valueOf(Date.sep));
        Log.d("Data", "Split string: " + Arrays.toString(components));

        int day = Integer.parseInt(components[0]);
        int month = Integer.parseInt(components[1]);
        int year = Integer.parseInt(components[2]);

        return new Date(day, month, year);
    }

    /**
     * Sets up Firebase access and loads all questions. Note that Firebase demands async access.
     * Usage is as follows:
     * <pre>
     *     {@code
     * new DataHandler(getContext(), dh -> {
     *     ...
     *     dh.getAnswer(question);
     *     ...
     * });
     *     }
     * </pre>
     *
     * @param context The context for questions.
     * @param onComplete The method to run on completion.
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

        questionsById = new LinkedHashMap<>();
        for (String filename : QUESTIONS_FILES) {
            String jsonString = JsonReader.loadJSONFromAsset(context, filename);
            if (jsonString == null) {
                Log.e("DataHandler", "Error reading JSON file");
                return;
            }

            try {
                JSONObject root = new JSONObject(jsonString);
                Iterator<String> keys = root.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    JSONObject questionObject = root.getJSONObject(key);
                    questionsById.put(key, questionObject);
                }
            } catch (JSONException e) {
                Log.e("DataHandler", "Error parsing JSON", e);
            }

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

    }

    /**
     * Substitutes the correct values into a templated string.
     *
     * @param tipTemplate The template.
     * @return The template after substitutions are made.
     */
    public String populateTemplate(String tipTemplate) {
        String patternString = "\\{(\\w+)\\}";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(tipTemplate);

        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            String varName = matcher.group(1);
            List<String> values = getAnswerById(varName);

            String replacement;
            if (values == null || values.isEmpty() || values.get(0).trim().isEmpty()) {
                replacement = "[missing answer]";
            } else if (values.size() == 1) {
                replacement = values.get(0).replaceAll("\"", "");
                assert varName != null;
                if (varName.equals("leave_timing")) {
                    Date date = DataHandler.stringToDate(replacement);
                    replacement = DataHandler.fixZeroIndexedMonth(date.getDate());
                }
            } else {
                replacement = joinWithCommasAnd(values);
            }
            assert replacement != null;
            matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(result);
        return result.toString();
    }

    /**
     * Gets the response to a question; delegates responsibility to {@link #getAnswerById(String)}.
     *
     * @param question The question to query.
     * @return An array of responses.
     */
    public ArrayList<String> getAnswerByQuestion(Question question) {
        return getAnswerById(question.getId());
    }

    /**
     * Gets the response to a question by a question id.
     *
     * @param questionId The id of the question.
     * @return An array of responses.
     */
    public ArrayList<String> getAnswerById(String questionId) {
        JSONObject questionObject = questionsById.get(questionId);
        if (questionObject == null) {
            return new ArrayList<>();
        }

        String rawAnswer = questionObject.optString("answer", "[]");
        return stringToArray(rawAnswer);
    }

    /**
     * Gets the array of question ids for the branches of a question. Delegates
     * responsibility to {@link #getBranchIdsById}.
     *
     * @param question The question for the branches.
     * @return An array of question ids.
     */
    public ArrayList<String> getBranchIdsByQuestion(Question question) {
        return getBranchIdsById(question.getId());
    }

    /**
     * Gets the array of question ids for the branches of a question by its id.
     *
     * @param questionId The question id for the branches.
     * @return An array of question ids.
     */
    public ArrayList<String> getBranchIdsById(String questionId) {
        JSONObject questionObject = questionsById.get(questionId);
        if (questionObject == null) {
            return new ArrayList<>();
        }

        try {
            if (!questionObject.isNull("branches")) {
                JSONObject branches = questionObject.getJSONObject("branches");
                String raw = branches.getString(getAnswerById(questionId).get(0).replaceAll("\"", ""));
                return stringToArray("[" + DataHandler.cleanString(raw) + "]");
            }
        } catch (JSONException e) {
            Log.e("DataHandler", "Error getting branches for question: " + questionId, e);
        }

        return new ArrayList<>();
    }

    /**
     * Gets the tip corresponding to the question; delegates responsibility to
     * {@link #getTipById(String)}.
     *
     * @param question The question for the tip.
     * @return The tip in question.
     */
    public String getTipByQuestion(Question question) {
        return getTipById(question.getId());
    }

    /**
     * Gets the tip corresponding to the question by id.
     *
     * @param questionId The id of the question.
     * @return The tip in question.
     */
    public String getTipById(String questionId) {
        JSONObject questionObject = questionsById.get(questionId);
        if (questionObject == null) {
            Log.d("abcde", "No question object found for: " + questionId);
            return "";
        }

        if (getAnswerById(questionId).isEmpty() ||
            getAnswerById(questionId).get(0).trim().isBlank() ||
            getAnswerById(questionId).get(0).trim().equals("\"\"") ||
            getAnswerById(questionId).get(0).equals("null")) {
            return "";
        }


        String template;

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
}
