package com.example.flashcard;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    private boolean choicesShow;
    private TextView question;
    private TextView answer;
    private ArrayList<TextView> choices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        question = findViewById(R.id.question1);
        answer = findViewById(R.id.answer1);
        TextView answerChoice = findViewById(R.id.answer1_choice);
        TextView choice1 = findViewById(R.id.choice1);
        TextView choice2 = findViewById(R.id.choice2);
        ToggleButton viewChoices = findViewById(R.id.view_choices);
        ImageButton addBtn = findViewById(R.id.add_button);
        ImageButton editBtn = findViewById(R.id.edit_button);
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

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddCardActivity.class);
                MainActivity.this.startActivityForResult(intent, 100);
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("adding new","intent started");
        if (data != null && resultCode == RESULT_OK) {
            Log.d("adding new", "data received");
            String new_question = data.getExtras().getString("question");
            ArrayList<String> new_options = data.getExtras().getStringArrayList("options");
            question.setText(new_question);
            answer.setText(new_options.get(0));
            Log.d("adding new", new_question);
            setChoices(new_options);
            question.setVisibility(View.VISIBLE);
            answer.setVisibility(View.INVISIBLE);

            Snackbar.make(findViewById(R.id.screen),"New flashcard added!", Snackbar.LENGTH_SHORT).show();
        }
    }

    private void setChoices(ArrayList<String> options){
        //TODO: randomize choices
        for(int i = 0; i < choices.size(); i++){
            choices.get(i).setText(options.get(i));
        }
    }
}