package com.example.languageeducationlab3.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.languageeducationlab3.R;
import com.example.languageeducationlab3.WordsMapProcessor;
import com.example.languageeducationlab3.task.DownloadWords;
import com.example.languageeducationlab3.task.HandlerThreadTask;
import com.example.languageeducationlab3.task.Task;
import com.example.languageeducationlab3.task.TaskListener;

import java.util.Map;

public class SettingsActivity extends AppCompatActivity{
    private Button buttonStartTestWithVariants, buttonStartTestWithInput;
    private RadioButton english,german;
    private RadioGroup languageGroup;
    private SeekBar seekBarCountQuestions;
    static Map<String,String> engWords,germWords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        if(this.english == null || this.german == null){
        createTask().execute(new TaskListener() {
            @Override
            public void onResult() {
                engWords = DownloadWords.englishWords;
                germWords = DownloadWords.germanWords;
                Log.d("Downloading words","success");
            }
        });
        }

        buttonStartTestWithInput = findViewById(R.id.test_with_input);
        buttonStartTestWithVariants = findViewById(R.id.test_with_variants);
        languageGroup = findViewById(R.id.language_group);

        english = findViewById(R.id.english_radio_button);
        german = findViewById(R.id.german_radio_button);
        english.setChecked(true);

        RadioButton time20 = findViewById(R.id.time20);
        RadioButton time30 = findViewById(R.id.time30);
        RadioButton time60 = findViewById(R.id.time60);
        time20.setChecked(true);

        seekBarCountQuestions = findViewById(R.id.count_questions_seekbar);
        TextView countQuestions = findViewById(R.id.count_questions_text);
        customSeekBar(countQuestions,seekBarCountQuestions);

        buttonStartTestWithVariants.setOnClickListener(new View.OnClickListener() {
            Intent intent = new Intent(SettingsActivity.this, TestWithVariantsActivity.class);
            Integer time;

            @Override
            public void onClick(View view) {
                setCorrectMap();

                if(time20.isChecked()) time = 20;
                else if (time30.isChecked()) time = 30;
                else time = 60;

                intent.putExtra("number_of_question_left",seekBarCountQuestions.getProgress())
                        .putExtra("time",time);

                startActivity(intent);
            }
        });

        buttonStartTestWithInput.setOnClickListener(new View.OnClickListener() {
            Intent intent = new Intent(SettingsActivity.this, TestWithInputActivity.class);

            Integer time;

            @Override
            public void onClick(View view) {
                setCorrectMap();

                if(time20.isChecked()) time = 20;
                else if (time30.isChecked()) time = 30;
                else time = 30;

                intent.putExtra("number_of_question_left",seekBarCountQuestions.getProgress())
                        .putExtra("time",time);

                startActivity(intent);

            }
        });
    }

    void customSeekBar(TextView textView, SeekBar seekBar){
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                textView.setText("Кількість запитаннь : " + String.valueOf(progress));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }




    public void saveWordMap(Map<String, String> engWords,Map<String,String> germWords) {
        this.engWords = engWords;
        this.germWords = germWords;
    }

    public void setCorrectMap(){
        if(languageGroup.getCheckedRadioButtonId() == R.id.english_radio_button){
            WordsMapProcessor.wordsMap = engWords;
        }else{
            WordsMapProcessor.wordsMap = germWords;
        }
    }

    private Task createTask(){
        return new HandlerThreadTask(new DownloadWords(getResources()));
    }
}