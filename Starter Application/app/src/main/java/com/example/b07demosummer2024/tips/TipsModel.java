package com.example.b07demosummer2024.tips;

/**
 * Encapsulates the necessary data to display a tip.
 */
public class TipsModel {
    String text;
    int image;

    /**
     * Standard parameterized constructor.
     *
     * @param text The text to display for the tip.
     * @param image The image to display for the tip.
     */
    public TipsModel(String text, int image) {
        this.text = text;
        this.image = image;
    }

    /**
     * Gets the display text.
     */
    public String getText() {
        return text;
    }

    /**
     * Gets the display image.
     */
    public int getImage() {
        return image;
    }
}
