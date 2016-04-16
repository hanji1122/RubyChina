package com.luckyhan.rubychina.ui.emoji;

public class Emotion {

    public String key;
    public String value;

    public Emotion(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return "Emotion{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
