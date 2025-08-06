package com.example.b07demosummer2024.questions.response;

import com.example.b07demosummer2024.data.DataHandler;

import java.util.HashSet;

/**
 * Implements a response type that can store multiple responses.
 */
public class MultipleResponse extends Response {
    private HashSet<String> response;
    private final int maxSelections;

    /**
     * Default constructor, sets the response to an empty <code>HashSet</code>. Also sets the max
     * number of allowed selections.
     *
     * @param maxSelections The maximum number of selections allowed.
     */
    public MultipleResponse(int maxSelections) {
        this.response = new HashSet<>();
        this.maxSelections = maxSelections;
    }

    /**
     * Provides implementation for {@link Response#isEmpty()}.
     *
     * @return <code>true</code> iff response is empty, <code>false</code> otherwise.
     */
    @Override
    public boolean isEmpty() {
        return response.isEmpty();
    }

    /**
     * Provides implementation for {@link Response#isValid()}. Validity is when the response is
     * non-empty and contains up to the max number of selections.
     *
     * @return <code>true</code> iff the response is valid, <code>false</code> otherwise.
     */
    @Override
    public boolean isValid() {
        return !isEmpty() && response.size() <= maxSelections;
    }

    /**
     * Builds the response using a string encoded by comma separated values.
     *
     * @param value The the string encoding of a response; specifically an array.
     */
    @Override
    public void setValue(String value) {
        this.response = new HashSet<>(DataHandler.stringToArray(value));
    }

    /**
     * Adds a response to the list of selected responses.
     *
     * @param response The response to add.
     */
    public void addResponse(String response) {
        this.response.add(response);
    }

    /**
     * Removes a response from the list of selected responses.
     *
     * @param response The response to remove.
     */
    public void removeResponse(String response) {
        this.response.remove(response);
    }

    /**
     * Gets the HashSet of responses.
     */
    public HashSet<String> getResponse() {
        return response;
    }

    /**
     * Gets the max number of responses allowed.
     */
    public int getMaxSelections() {
        return maxSelections;
    }
}
