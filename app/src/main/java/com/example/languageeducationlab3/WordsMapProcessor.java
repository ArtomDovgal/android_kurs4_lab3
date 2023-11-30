package com.example.languageeducationlab3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class WordsMapProcessor {

    public static Map<String,String> wordsMap;

    public WordsMapProcessor(Map<String, String> wordsMap) {
        this.wordsMap = wordsMap;
    }

    public static String[] getRandomEntry(String[] words) {
        if (wordsMap.isEmpty()) {
            return null;
        }
        Map<String,String> wordsMapAll = wordsMap;
        deleteWordsForKey(words);

        String[] keys = wordsMap.keySet().toArray(new String[0]);
        Random random = new Random();
        int randomIndex = random.nextInt(keys.length);
        String randomKey = keys[randomIndex];
        String randomValue = wordsMap.get(randomKey);
        wordsMap = wordsMapAll;
        return new String[]{randomKey,randomValue};
    }

    public static ArrayList<String> getNonAssociatedElements( String key) {
        int count = 3;
        ArrayList<String> allValues = new ArrayList<>(wordsMap.values());
        allValues.remove(wordsMap.get(key));

        if (allValues.size() <= count) {
            return allValues;
        }

        ArrayList<String> randomNonAssociatedElements = new ArrayList<>();
        Random random = new Random();

        while (randomNonAssociatedElements.size() < count) {
            int randomIndex = random.nextInt(allValues.size());
            String randomValue = allValues.get(randomIndex);

            if (!randomNonAssociatedElements.contains(randomValue)) {
                randomNonAssociatedElements.add(randomValue);
            }
        }

        return randomNonAssociatedElements;
    }

    public static void deleteWordsForKey(String[] words){
        if(words.length == 0) return;

        for(String str : Arrays.asList(words)){
            wordsMap.remove(str);
        }
    }

    public static String[] getRandomNonAssociatedElements(String key) {
        List<String> nonAssociatedElements = getNonAssociatedElements(key);

        if (nonAssociatedElements.size() <= 3) {
            return nonAssociatedElements.toArray(new String[0]);
        } else {
            String[] randomElements = new String[3];
            Random random = new Random();

            for (int i = 0; i < 3; i++) {
                int randomIndex = random.nextInt(nonAssociatedElements.size());
                randomElements[i] = nonAssociatedElements.get(randomIndex);
                nonAssociatedElements.remove(randomIndex);
            }
            return randomElements;
        }
    }

}
