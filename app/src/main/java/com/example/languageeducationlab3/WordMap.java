package com.example.languageeducationlab3;

import java.io.Serializable;
import java.util.Map;

public class WordMap implements Serializable {
    private Map<String, String> words;

    public WordMap(Map<String, String> wordMap) {
        this.words = wordMap;
    }

    public Map<String, String> getWords() {
        return words;
    }
}
