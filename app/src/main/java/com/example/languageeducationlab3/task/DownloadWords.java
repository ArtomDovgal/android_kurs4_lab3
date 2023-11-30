package com.example.languageeducationlab3.task;

import android.content.res.Resources;

import com.example.languageeducationlab3.R;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DownloadWords{

public static Map<String,String> englishWords;
public static Map<String,String> germanWords;
private Resources resources;

public DownloadWords(Resources resources){
    this.resources = resources;
}
    public Boolean downloadWords() {
        loadWordsFromFile(resources);
        return true;
    }

    private void loadWordsFromFile(Resources resources) {
        String[] wordsEng,wordsGerm, translateEng,translateGerm;

        wordsEng = resources.getStringArray(R.array.english_word);
        translateEng = resources.getStringArray(R.array.english_translate);
        wordsGerm = resources.getStringArray(R.array.german_word);
        translateGerm = resources.getStringArray(R.array.german_translate);

        this.englishWords = IntStream.range(0, wordsEng.length).boxed()
                .collect(Collectors.toMap(i -> wordsEng[i], i -> translateEng[i]));

        this.germanWords = IntStream.range(0, wordsGerm.length).boxed()
                .collect(Collectors.toMap(i -> wordsGerm[i], i -> translateGerm[i]));


    }
}
