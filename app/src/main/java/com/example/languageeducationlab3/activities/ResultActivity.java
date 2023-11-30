package com.example.languageeducationlab3.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.languageeducationlab3.R;

public class ResultActivity extends AppCompatActivity {
    TextView numWrongAnswers, numCorrectAnswers;
    Button toMainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        numWrongAnswers = findViewById(R.id.number_wrong_answers);
        numCorrectAnswers = findViewById(R.id.number_correct_answers);
        toMainActivity = findViewById(R.id.to_main_page);

        Integer correct = getIntent().getIntExtra("number_correct_answers",0);
        numCorrectAnswers.setText(correct.toString());
        Integer wrong = getIntent().getIntExtra("number_wrong_answers",0);
        numWrongAnswers.setText(wrong.toString());


        toMainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResultActivity.this, SettingsActivity.class);

                startActivity(intent);
            }
        });
    }
}