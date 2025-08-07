package com.example.b07demosummer2024.questions;

import android.content.Context;
import android.util.Pair;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.example.b07demosummer2024.questions.response.MultipleResponse;
import com.example.b07demosummer2024.questions.response.Response;
import com.example.b07demosummer2024.questions.widget.Widget;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Generic class which represents a general question type.
 */
public abstract class Question {

    protected final String id;
    protected final String statement;
    protected Response response;
    protected Widget widget;
    protected LinkedHashMap<String, Pair<String, Question>> branches; // maps id to <response, question>
    protected LinearLayout branchLayout;

    /**
     * Determines whether the <code>LinkedHashMap</code> of <code>Question</code>'s has an invalid response.
     * Validity is determined on each iteration using {@link #isValid()}. If the question contains
     * branches, then the validity of the branched questions are also assessed.
     *
     * @param questions The hash map of questions to verify.
     * @return <code>true</code> iff all questions are valid according to {@link #isValid()},
     * <code>false</code> otherwise.
     */
    public static boolean hasInvalid(LinkedHashMap<String, Question> questions) {
        if (questions.isEmpty()) {
            return false;
        }

        for (Question q: questions.values()) {
            LinkedHashMap<String, Question> branch = new LinkedHashMap<>();
            for (Map.Entry<String, Pair<String, Question>> branchData : q.getBranches().entrySet()) {
                if (((MultipleResponse) q.getResponse()).getResponse().contains(branchData.getValue().first))
                    branch.put(branchData.getKey(), branchData.getValue().second);
            }

            if (!q.isValid() || hasInvalid(branch))
                return true;
        }
        return false;
    }

    /**
     * Constructor for for the base <code>Question</code> class. Sets default values for shared data
     * across the question subclasses.
     *
     * @param statement The statement of the question to be displayed.
     * @param id The underlying question id used for I/O.
     */
    protected Question(String statement, String id) {
        this.id = id;
        this.statement = statement;
        this.response = null;
        this.branches = new LinkedHashMap<>();
    }

    /**
     * Determines whether the stored response to the question is valid or not. To be
     * implemented by subclasses depending on what type of response is stored.
     *
     * @return <code>true</code> iff the response is valid, <code>false</code> otherwise
     */
    public abstract boolean isValid();

    /**
     * Builds a {@link com.example.b07demosummer2024.questions.widget.Widget}
     * object in the given <code>context</code>. Sets up all necessary tools to display, modify and
     * retrieve data from the user.
     *
     * @param context The context the widget should be placed in.
     * @param defaultValue A default response to be displayed and locally stored.
     */
    public abstract void buildWidget(Context context, String defaultValue);

    /**
     * Callback method to update the display state of branched questions in <code>branches</code>.
     * Implementation depends on type of {@link com.example.b07demosummer2024.questions.Question}.
     */
    public abstract void updateBranch();


    /**
     * Generic callback to be invoked on user interaction with the question elements. Updates the
     * states of the corresponding widgets and display elements along with locally updating the
     * user's response.
     *
     * @see  #setResponse()
     * @see com.example.b07demosummer2024.questions.widget.Widget#updateNotes(Response)
     * @see #updateBranch()
     */
    public void handler() {
        setResponse();
        widget.updateNotes(response);
        updateBranch();
    }

    /**
     * @return a string representation for debugging purposes.
     */
    @NonNull
    @Override
    public String toString() {
        return "<Question: id=" + id + ", Statement=" + statement + ">";
    }

    /**
     * Inserts a new question into <code>branches</code>.
     *
     * @param linkedResponse The response that corresponds to the new branch.
     * @param id The id of the new question.
     * @param question The question object.
     */
    public void addBranch(String linkedResponse, String id, final Question question) {
        branches.put(id, new Pair<>(linkedResponse, question));
    }

    /**
     * Gets the question id.
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the question statement.
     */
    public String getStatement() {
        return statement;
    }

    /**
     * Internally updates <code>response</code> using the users response; delegates responsibility
     * to {@link com.example.b07demosummer2024.questions.widget.Widget#setResponseValue(Response)}.
     */
    public void setResponse() {
        widget.setResponseValue(response);
    }

    /**
     * Internally sets stored value of <code>response</code> by delegating responsibility to
     * {@link com.example.b07demosummer2024.questions.response.Response#setValue(String)}. In
     * contrast to {@link #setResponse()}, this method updates the <code>Response</code> object
     * directly without retrieval from <code>widget</code>.
     *
     * @param value What to set <code>response</code> to.
     */
    public void setResponseValue(String value) {
        response.setValue(value);
    }

    /**
     * Gets the local user response.
     */
    public Response getResponse() {
        return response;
    }

    /**
     * Gets the display widget.
     */
    public Widget getWidget() {
        return this.widget;
    }

    /**
     * Initializes the display element for branches.
     *
     * @param context The context for displaying.
     */
    public void buildBranch(Context context) {
        branchLayout = new LinearLayout(context);
        branchLayout.setOrientation(LinearLayout.VERTICAL);
    }

    /**
     * Gets the <code>LinkedHashMap</code> of branched questions.
     */
    public LinkedHashMap<String, Pair<String, Question>> getBranches() {
        return branches;
    }

    /**
     * Gets the display layout which displays branches.
     */
    public LinearLayout getBranchLayout() {
        return branchLayout;
    }
}
