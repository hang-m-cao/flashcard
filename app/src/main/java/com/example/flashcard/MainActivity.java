package com.example.flashcard;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private boolean choicesShow;
    private TextView question;
    private TextView answer;
    private ArrayList<TextView> choices;
    private ToggleButton viewChoices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        question = findViewById(R.id.question1);
        answer = findViewById(R.id.answer1);
        TextView answerChoice = findViewById(R.id.answer1_choice);
        TextView choice1 = findViewById(R.id.choice1);
        TextView choice2 = findViewById(R.id.choice2);
        viewChoices = findViewById(R.id.view_choices);
        ImageButton addBtn = findViewById(R.id.add_button);
        choicesShow = true;

        choices = new ArrayList<>();
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
                hideChoices(choicesShow);
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

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddCardActivity.class);
                MainActivity.this.startActivityForResult(intent, 100);
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

    public void hideChoices(Boolean choicesShow){
        int viewVisibility;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null && requestCode == RESULT_OK){
            String new_question = data.getExtras().getString("question");
            String new_answer = data.getExtras().getString("answer");
            question.setText(new_question);
            answer.setText(new_answer);
            answer.setVisibility(View.INVISIBLE);
            viewChoices.setVisibility(View.INVISIBLE);
            choicesShow = false;
            hideChoices(choicesShow);
            Snackbar.make(findViewById(R.id.screen),
                    "New flashcard added!",
                    Snackbar.LENGTH_SHORT)
                    .show();
        }
    }
}