package com.example.b07demosummer2024.questions.response;

/**
 * Wrapper class to store responses in either a list or string format
 */
public abstract class Response {
    /**
     * Determines whether the response is empty or not. Decision is made per implementation.
     *
     * @return <code>true</code> iff the response is empty, <code>false</code> otherwise.
     */
    public abstract boolean isEmpty();

    /**
     * Determines whether the response is a valid response or not. Decision is made per
     * implementation.
     *
     * @return <code>true</code> iff the response is valid, <code>false</code> otherwise.
     */
    public abstract boolean isValid();

    /**
     * Sets the value of the response using a string. Either a pure string or encoded string can be
     * passed. Resolution of string encoding is left to the implementation.
     *
     * @param value The string or the string encoding of a response.
     */
    public abstract void setValue(String value);
}
