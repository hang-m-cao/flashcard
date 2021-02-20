package com.example.flashcard;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public boolean choicesShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView question = findViewById(R.id.question1);
        TextView answer = findViewById(R.id.answer1);
        TextView answerChoice = findViewById(R.id.answer1_choice);
        TextView choice1 = findViewById(R.id.choice1);
        TextView choice2 = findViewById(R.id.choice2);
        ToggleButton viewChoices = findViewById(R.id.view_choices);
        choicesShow = true;
        ArrayList<TextView> choices = new ArrayList<>();
        choices.add(answerChoice);
        choices.add(choice1);
        choices.add(choice2);


        answerChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answerChoice.setBackgroundColor( getResources().getColor(R.color.light_green));
                answerChoice.setTextColor(getResources().getColor(R.color.black));
                Toast.makeText(MainActivity.this, "Correct!", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {

                    public void run() {
                        answerChoice.setBackgroundColor( getResources().getColor(R.color.purple_200));
                        answerChoice.setTextColor(getResources().getColor(R.color.white));
                    }
                }, 1000);
            }
        });
        
        choice1.setOnClickListener(this::wrongClick);
        choice2.setOnClickListener(this::wrongClick);

        viewChoices.setOnClickListener(new View.OnClickListener(){
            public int viewVisibility;
            @Override
            public void onClick(View v) {
                choicesShow = !choicesShow;
                if(choicesShow){
                    viewVisibility = View.VISIBLE;
                }
                else{
                    viewVisibility = View.INVISIBLE;
                }
                for(int i = 0; i < choices.size(); i++){
                    choices.get(i).setVisibility(viewVisibility);
                }
            }
        });

        question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!choicesShow){
                    answer.setVisibility(View.VISIBLE);
                    question.setVisibility(View.INVISIBLE);
                }
            }
        });

        answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                question.setVisibility(View.VISIBLE);
                answer.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void wrongClick(View v){
        TextView text = findViewById(v.getId());
        text.setBackgroundColor( getResources().getColor(R.color.red));
        text.setTextColor(getResources().getColor(R.color.black));
        Toast.makeText(MainActivity.this, "Incorrect. Try again!", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {

            public void run() {
                text.setBackgroundColor( getResources().getColor(R.color.purple_200));
                text.setTextColor(getResources().getColor(R.color.white));
            }
        }, 1000);
    }
}