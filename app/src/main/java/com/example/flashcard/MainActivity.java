package com.example.flashcard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView question = findViewById(R.id.flashcard_question);
        TextView answer = findViewById(R.id.flashcard_answer);

        question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                question.setVisibility(View.INVISIBLE);
                answer.setVisibility(View.VISIBLE);
            }
        });

        answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer.setVisibility(View.INVISIBLE);
                question.setVisibility(View.VISIBLE);
            }
        });
    }
}