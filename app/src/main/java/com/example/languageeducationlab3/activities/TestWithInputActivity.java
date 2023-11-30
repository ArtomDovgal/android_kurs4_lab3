package com.example.languageeducationlab3.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.languageeducationlab3.R;
import com.example.languageeducationlab3.WordsMapProcessor;

import java.util.ArrayList;

public class TestWithInputActivity extends AppCompatActivity {
    TextView wordTextView,displayTime;
    EditText editTextEditText;
    Button buttonCheckAnswer,buttonGoToNext;
    Integer numCorrectAnswers,numberOfQuestionsLeft;
    WordsMapProcessor wordsMapProcessor = new WordsMapProcessor(WordsMapProcessor.wordsMap);
    CountDownTimer countDownTimer;
    String word,translate;
    Long timeRemainingMillis;
    static ArrayList<String> usedWords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_with_input);
        Intent intent = getIntent();

        wordTextView = findViewById(R.id.test_word_input);
        buttonCheckAnswer = findViewById(R.id.check_word_input);
        editTextEditText = findViewById(R.id.input_variant_word);
        buttonGoToNext = findViewById(R.id.go_to_next);
        displayTime = findViewById(R.id.time_display_input);

        if(savedInstanceState != null){
            numCorrectAnswers = savedInstanceState.getInt("numCorrectAnswers");
            numberOfQuestionsLeft = savedInstanceState.getInt("numberOfQuestionsLeft");
            buttonGoToNext.setText(savedInstanceState.getString("textOnButtonGoToNext"));
            word = savedInstanceState.getString("word");
            translate = savedInstanceState.getString("translate");

            String textCheckButton = savedInstanceState.getString("textCheckButton");

            if(textCheckButton.equals(getResources().getString(R.string.сorrect_ua))){
                buttonCheckAnswer.setBackgroundColor(Color.GREEN);
                buttonCheckAnswer.setText(getResources().getString(R.string.сorrect_ua));
                buttonGoToNext.setVisibility(View.VISIBLE);
            }else if(textCheckButton.equals(getResources().getString(R.string.wrong_ua))){
                buttonCheckAnswer.setBackgroundColor(Color.RED);
                buttonCheckAnswer.setText(getResources().getString(R.string.wrong_ua));
                buttonGoToNext.setVisibility(View.VISIBLE);
            }
            if(!savedInstanceState.getBoolean("buttonEnabled")){
                buttonCheckAnswer.setEnabled(false);
            }

            Integer remainTime = (int) savedInstanceState.getLong("timeRemainingMillis");
            if (remainTime > 0) {
                startTimer(remainTime);
            } else {
                displayTime.setText(savedInstanceState.getString("textTimer"));
            }
        }else{
            String[] wordAndTranslate = wordsMapProcessor.getRandomEntry(new String[]{});
            word = wordAndTranslate[0];
            translate = wordAndTranslate[1];
            numberOfQuestionsLeft = intent.getIntExtra("number_of_question_left", 0);
            numCorrectAnswers = 0;
            usedWords = new ArrayList<>();
            usedWords.add(word);
            startTimer(getIntent().getIntExtra("time", 30)*1000);
        }

        wordTextView.setText(word);


        buttonCheckAnswer.setOnClickListener(view -> {
            countDownTimer.cancel();
            if (checkAnswer()) {
                buttonCheckAnswer.setBackgroundColor(Color.GREEN);
                buttonCheckAnswer.setText(getResources().getString(R.string.сorrect_ua));
                numCorrectAnswers++;
            } else {
                buttonCheckAnswer.setBackgroundColor(Color.RED);
                buttonCheckAnswer.setText(getResources().getString(R.string.wrong_ua));
            }
            if(numberOfQuestionsLeft == 1){
                buttonGoToNext.setText(getResources().getString(R.string.finish_ua));
            }
            buttonCheckAnswer.setTextColor(Color.WHITE);
            buttonCheckAnswer.setEnabled(false);
            buttonGoToNext.setVisibility(View.VISIBLE);
            timeRemainingMillis = 0L;
        });

        TestWithInputActivity context = this;
        buttonGoToNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(numberOfQuestionsLeft == 1){
                    Integer numWrongAnswers = usedWords.size() - numCorrectAnswers;
                    Intent intentResult = new Intent(TestWithInputActivity.this, ResultActivity.class);

                    intentResult.putExtra("number_correct_answers",numCorrectAnswers);
                    intentResult.putExtra("number_wrong_answers",numWrongAnswers);

                    startActivity(intentResult);
                }
                else{
                    String[] wordAndTranslate = wordsMapProcessor.getRandomEntry(usedWords.toArray(new String[0]));
                    word = wordAndTranslate[0];
                    translate = wordAndTranslate[1];
                    usedWords.add(word);

                    buttonCheckAnswer.setEnabled(true);
                    buttonCheckAnswer.setText(getResources().getString(R.string.check_ua));
                    buttonCheckAnswer.setBackgroundColor(ContextCompat.getColor(context, R.color.main_color));
                    buttonGoToNext.setVisibility(View.INVISIBLE);

                    numberOfQuestionsLeft = numberOfQuestionsLeft -1;
                    wordTextView.setText(word);
                    buttonGoToNext.setText(getResources().getString(R.string.next_ua));
                    editTextEditText.setText("");
                    startTimer(getIntent().getIntExtra("time", 30)*1000);
                }
            }
        });

    }

    private Boolean checkAnswer(){
        String word = translate;
        String input = editTextEditText.getText().toString().toLowerCase();
        return word.equals(input);
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
                        buttonCheckAnswer.setEnabled(false);
                        if(numberOfQuestionsLeft == 1){
                            buttonGoToNext.setText(getResources().getString(R.string.finish_ua));
                        }
                        buttonGoToNext.setVisibility(View.VISIBLE);
                        displayTime.setText(getResources().getString(R.string.time_is_end_ua));
                    }
                }.start();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("word",word);
        outState.putString("translate", translate);
        outState.putInt("numCorrectAnswers", numCorrectAnswers);
        outState.putInt("numberOfQuestionsLeft", numberOfQuestionsLeft);

        if(timeRemainingMillis != null && timeRemainingMillis != 0){

            countDownTimer.cancel();
            outState.putLong("timeRemainingMillis", timeRemainingMillis);

        } else {
            outState.putLong("timeRemainingMillis", 0);
        }

        outState.putString("textOnButtonGoToNext",buttonGoToNext.getText().toString());
        outState.putBoolean("buttonEnabled",buttonCheckAnswer.isEnabled());
        outState.putString("textCheckButton", buttonCheckAnswer.getText().toString());
        outState.putString("textTimer",displayTime.getText().toString());
    }
}