package com.example.languageeducationlab3.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.languageeducationlab3.R;
import com.example.languageeducationlab3.WordsMapProcessor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestWithVariantsActivity extends AppCompatActivity {

    TextView wordTextView,displayTime;
    Button firstButton, secondButton, thirdButton,fourthButton, buttonGoToNext;
    Integer numCorrectAnswers,numberOfQuestionsLeft;
    WordsMapProcessor wordsMapProcessor = new WordsMapProcessor(WordsMapProcessor.wordsMap);
    CountDownTimer countDownTimer;
    Long timeRemainingMillis;
    String word, translate;

    static int[] correctAndWrongButtonIds = new int[2];

    static ArrayList<String> usedWords;
    ArrayList<String> textButtons;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_with_variants);


        firstButton = findViewById(R.id.button_variant1);
        secondButton = findViewById(R.id.button_variant2);
        thirdButton = findViewById(R.id.button_variant3);
        fourthButton = findViewById(R.id.button_variant4);
        wordTextView = findViewById(R.id.test_word_variant);
        buttonGoToNext = findViewById(R.id.go_to_next_fragment_variants);
        displayTime = findViewById(R.id.time_display_variant);

        if (savedInstanceState != null) {
            textButtons = savedInstanceState.getStringArrayList("textButtons");
            numCorrectAnswers = savedInstanceState.getInt("numCorrectAnswers");
            numberOfQuestionsLeft = savedInstanceState.getInt("numberOfQuestionsLeft");
            buttonGoToNext.setText(savedInstanceState.getString("textOnButtonGoToNext"));

            firstButton.setText(textButtons.get(0));
            secondButton.setText(textButtons.get(1));
            thirdButton.setText(textButtons.get(2));
            fourthButton.setText(textButtons.get(3));

            int[] arrCorrectAndWrong = savedInstanceState.getIntArray("correctAndWrongButtonsId");

            if(savedInstanceState.getBoolean("buttonsEnabled") == false){
                buttonGoToNext.setVisibility(View.VISIBLE);
                setDefaultButtonsColor();
                if(arrCorrectAndWrong[0] != 0)
                    findViewById(arrCorrectAndWrong[0]).setBackgroundColor(Color.GREEN);
                if(arrCorrectAndWrong[1] != 0)
                    findViewById(arrCorrectAndWrong[1]).setBackgroundColor(Color.RED);

                firstButton.setEnabled(false);
                secondButton.setEnabled(false);
                thirdButton.setEnabled(false);
                fourthButton.setEnabled(false);
            }
            word = savedInstanceState.getString("word");
            translate = savedInstanceState.getString("translate");
            Integer remainTime = (int)savedInstanceState.getLong("timeRemainingMillis");
            if (remainTime > 0) {
                startTimer(remainTime);
            } else {
                displayTime.setText(savedInstanceState.getString("textTimer"));
            }

        }else{
            numCorrectAnswers = 0;
            numberOfQuestionsLeft = intent.getIntExtra("number_of_question_left",0);
            String[] wordAndTranslate = wordsMapProcessor.getRandomEntry(new String[]{});
            word = wordAndTranslate[0];
            translate = wordAndTranslate[1];
            usedWords = new ArrayList<String>();
            usedWords.add(word);
            setVariantButtons(wordsMapProcessor.getNonAssociatedElements(word), translate);
            startTimer(intent.getIntExtra("time",15)*1000);
        }

        wordTextView.setText(word);

        firstButton.setOnClickListener(view1 -> onClicks(view1));
        secondButton.setOnClickListener(view1 -> onClicks(view1));
        thirdButton.setOnClickListener(view1 -> onClicks(view1));
        fourthButton.setOnClickListener(view1 -> onClicks(view1));




        buttonGoToNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (numberOfQuestionsLeft == 1) {
                    Integer numWrongAnswers = usedWords.size() - numCorrectAnswers;

                    Intent intentResult = new Intent(TestWithVariantsActivity.this,ResultActivity.class);
                    intentResult.putExtra("number_correct_answers", numCorrectAnswers);
                    intentResult.putExtra("number_wrong_answers",numWrongAnswers);
                    startActivity(intentResult);

                } else {

                    String[] wordAndTranslate = WordsMapProcessor.getRandomEntry(usedWords.toArray(new String[0]));
                    word = wordAndTranslate[0];
                    translate = wordAndTranslate[1];
                    usedWords.add(word);

                    setVariantButtons(WordsMapProcessor.getNonAssociatedElements(word),translate);
                    setDefaultButtonsColor();
                    setButtonsEnabled();
                    buttonGoToNext.setVisibility(View.INVISIBLE);
                    numberOfQuestionsLeft = numberOfQuestionsLeft -1;
                    wordTextView.setText(word);
                    startTimer(intent.getIntExtra("time",15)*1000);
                }

            }
        });
    }
    public void onClicks(View view){
        countDownTimer.cancel();
        timeRemainingMillis = 0L;
        //displayTime.setText(getResources().getString(R.string.time_is_end_ua));
        Button button = (Button) view;

        if(button.getText().equals(translate)){
            button.setBackgroundColor(Color.GREEN);
            correctAndWrongButtonIds[0] = button.getId();
            numCorrectAnswers++;
        }else{
            button.setBackgroundColor(Color.RED);
            correctAndWrongButtonIds[1] = button.getId();
            if(firstButton.getText().equals(translate)){
                correctAndWrongButtonIds[0] = firstButton.getId();
                firstButton.setBackgroundColor(Color.GREEN);}
            else if(secondButton.getText().equals(translate)){
                correctAndWrongButtonIds[0] = secondButton.getId();
                secondButton.setBackgroundColor(Color.GREEN);
            }
            else if(thirdButton.getText().equals(translate)){
                correctAndWrongButtonIds[0] = thirdButton.getId();
                thirdButton.setBackgroundColor(Color.GREEN);
            }
            else{
                correctAndWrongButtonIds[0] = firstButton.getId();
                fourthButton.setBackgroundColor(Color.GREEN);
            }
        }
        firstButton.setEnabled(false);
        secondButton.setEnabled(false);
        thirdButton.setEnabled(false);
        fourthButton.setEnabled(false);

        if(numberOfQuestionsLeft == 1){
            buttonGoToNext.setText(getResources().getString(R.string.finish_ua));
        }
        buttonGoToNext.setVisibility(View.VISIBLE);

    }

    void setVariantButtons(List<String> variants, String correctAnswer){
        variants.add(correctAnswer);
        Collections.shuffle(variants);
        textButtons = (ArrayList<String>) variants;
        firstButton.setText(variants.get(0));
        secondButton.setText(variants.get(1));
        thirdButton.setText(variants.get(2));
        fourthButton.setText(variants.get(3));

        textButtons = (ArrayList<String>) variants;
    }
    void setButtonsEnabled(){
        firstButton.setEnabled(true);
        secondButton.setEnabled(true);
        thirdButton.setEnabled(true);
        fourthButton.setEnabled(true);
    }
    void setDefaultButtonsColor(){
        int defaultColor = ContextCompat.getColor(this, R.color.main_color);

        firstButton.setBackgroundColor(defaultColor);
        secondButton.setBackgroundColor(defaultColor);
        thirdButton.setBackgroundColor(defaultColor);
        fourthButton.setBackgroundColor(defaultColor);
    }
    void setDefaultButtonsColorForTwo(){
        int defaultColor = ContextCompat.getColor(this, R.color.main_color);
        findViewById(correctAndWrongButtonIds[0]).setBackgroundColor(defaultColor);
        findViewById(correctAndWrongButtonIds[1]).setBackgroundColor(defaultColor);
    }
    void startTimer(int seconds){
        countDownTimer =
                new CountDownTimer(seconds, 1000) {

                    public void onTick(long millisUntilFinished) {
                        timeRemainingMillis = millisUntilFinished;
                        displayTime.setText("Часу залишилось: " + millisUntilFinished / 1000 + " секунд");
                    }

                    public void onFinish() {
                        timeRemainingMillis = 0L;
                        firstButton.setEnabled(false);
                        secondButton.setEnabled(false);
                        thirdButton.setEnabled(false);
                        fourthButton.setEnabled(false);

                        if(numberOfQuestionsLeft == 1){
                            buttonGoToNext.setText(getResources().getString(R.string.finish_ua));
                        }
                        buttonGoToNext.setVisibility(View.VISIBLE);
                        displayTime.setText(getResources().getString(R.string.time_is_end_ua));
                    }
                }.start();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("numCorrectAnswers", numCorrectAnswers);
        outState.putInt("numberOfQuestionsLeft", numberOfQuestionsLeft);

        if(timeRemainingMillis != null && timeRemainingMillis != 0) {
            countDownTimer.cancel();
            outState.putLong("timeRemainingMillis", timeRemainingMillis);
        }
        else{
            outState.putLong("timeRemainingMillis", 0);
        }

        outState.putBoolean("isButtonGoToNextVisible", buttonGoToNext.isCursorVisible());
        outState.putString("textOnButtonGoToNext",buttonGoToNext.getText().toString());
        outState.putBoolean("buttonsEnabled",firstButton.isEnabled());
        outState.putString("word",word);
        outState.putString("translate", translate);
        outState.putIntArray("correctAndWrongButtonsId",correctAndWrongButtonIds);
        outState.putStringArrayList("textButtons", textButtons);
        outState.putString("textTimer",displayTime.getText().toString());

    }

}