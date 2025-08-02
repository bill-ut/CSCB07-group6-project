package com.example.b07demosummer2024.tips;

public class TipsModel {
    String text;
    int image;


    public TipsModel(String text, int image) {
        this.text = text;
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public int getImage() {
        return image;
    }
}
