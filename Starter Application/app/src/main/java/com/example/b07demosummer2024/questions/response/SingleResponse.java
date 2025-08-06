package com.example.b07demosummer2024.questions.response;

/**
 * Implements a response type that stores a single response as a string.
 */
public class SingleResponse extends Response {
    private String response;

    /**
     * Default constructor, sets the response to an empty string.
     */
    public SingleResponse() {
        this.response = "";
    }

    /**
     * Provides implementation for {@link Response#isEmpty()}.
     *
     * @return <code>true</code> iff the response is the empty string, <code>false</code> otherwise.
     */
    @Override
    public boolean isEmpty() {
        return response.isEmpty();
    }

    /**
     * Provides implementation for {@link Response#isValid()}.
     *
     * @return <code>true</code> iff the response is non-empty, <code>false</code> otherwise.
     */
    @Override
    public boolean isValid() {
        return !isEmpty();
    }

    /**
     * Sets the response value.
     *
     * @param value The string or the string encoding of a response.
     */
    @Override
    public void setValue(String value) {
        this.response = value;
    }

    /**
     * Gets the response value.
     */
    public String getResponse() {
        return response;
    }
}
