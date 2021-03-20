package com.example.flashcard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private TextView question;
    private TextView answer;
    private TextView answerChoice;
    private TextView choice1;
    private TextView choice2;
    Boolean choicesShow;
    FlashcardDatabase flashcardDatabase;
    List<Flashcard> allFlashcards;
    private int flashCardIndex;
    private ArrayList<Integer> possibleIndex;
    private ImageButton next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flashcardDatabase = new FlashcardDatabase(getApplicationContext());
        allFlashcards = flashcardDatabase.getAllCards();
        flashCardIndex = 0;
        possibleIndex = resetPossibleIndex();

        question = findViewById(R.id.question_card);
        answer = findViewById(R.id.answer_card);
        answerChoice = findViewById(R.id.answer_choice);
        choice1 = findViewById(R.id.choice1);
        choice2 = findViewById(R.id.choice2);
        choicesShow = false;
        TextView[] choices = new TextView[]{answerChoice, choice1, choice2};

        next = findViewById(R.id.next);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flashCardIndex = getRandomIndex();
                showCard();
            }
        });

        findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                flashcardDatabase.deleteCard(question.getText().toString());
                allFlashcards = flashcardDatabase.getAllCards();
                possibleIndex = resetPossibleIndex();
                if(!possibleIndex.isEmpty()){
                    flashCardIndex = getRandomIndex();
                }
                else {
                    flashCardIndex = 0;
                }

                showCard();

                Snackbar.make(findViewById(R.id.screen), "Flashcard deleted", Snackbar.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.add_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                possibleIndex.add(allFlashcards.size());
                Intent intent = new Intent(MainActivity.this, AddCardActivity.class);
                MainActivity.this.startActivityForResult(intent, 1);
            }
        });

        findViewById(R.id.edit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddCardActivity.class);
                intent.putExtra("question", question.getText());
                intent.putExtra("choices", getChoices(choices));
                MainActivity.this.startActivityForResult(intent, 5);
            }
        });

        setChoiceClick(answerChoice, "Correct!", R.color.light_green);
        setChoiceClick(choice1, "Incorrect! Try again.", R.color.red);
        setChoiceClick(choice2, "Incorrect! Try again.", R.color.red);

        findViewById(R.id.view_choices).setOnClickListener(new View.OnClickListener(){
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
                for(TextView c : choices){
                    c.setVisibility(viewVisibility);
                }
            }
        });

        question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!choicesShow){
                    question.setVisibility(View.INVISIBLE);
                }
            }
        });

        answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                question.setVisibility(View.VISIBLE);
            }
        });

        showCard();
    }

    public void setChoiceClick(TextView btn, String msg, int color){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn.setBackgroundColor( getResources().getColor(color));
                btn.setTextColor(getResources().getColor(R.color.black));
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {

                    public void run() {
                        btn.setBackgroundColor( getResources().getColor(R.color.lavender));
                        btn.setTextColor(getResources().getColor(R.color.mantee));
                    }
                }, 1000);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String display = "";
        if (data != null && resultCode == RESULT_OK) {
            String new_question = data.getExtras().getString("question");
            ArrayList<String> new_options = data.getExtras().getStringArrayList("options");
            question.setVisibility(View.VISIBLE);
            if (requestCode == 1) {
                flashcardDatabase.insertCard(new Flashcard(new_question, new_options.get(0), new_options.get(1), new_options.get(2)));
                display = "New flashcard added!";
                next.setVisibility(View.VISIBLE);
            } else if (requestCode == 5) {
                Flashcard cardToEdit = allFlashcards.get(flashCardIndex);
                updateCard(cardToEdit, new_question, new_options);
                flashcardDatabase.updateCard(cardToEdit);
                display = "Flashcard updated!";
            }
            allFlashcards = flashcardDatabase.getAllCards();
            showCard();
            Snackbar.make(findViewById(R.id.screen), display, Snackbar.LENGTH_SHORT).show();
        }
    }

    private void showCard(){
        if(allFlashcards.isEmpty()){
            next.setVisibility(View.INVISIBLE);
            Intent intent = new Intent(MainActivity.this, EmptyStateActivity.class);
            MainActivity.this.startActivityForResult(intent, 1);
        }
        else{
            if (allFlashcards.size() == 1){
                next.setVisibility(View.INVISIBLE);
            }
            else{
                next.setVisibility(View.VISIBLE);
            }
            Flashcard f = allFlashcards.get(flashCardIndex);
            question.setText(f.getQuestion());
            answer.setText(f.getAnswer());
            answerChoice.setText(f.getAnswer());
            choice1.setText(f.getWrongAnswer1());
            choice2.setText(f.getWrongAnswer2());
        }
    }

    private ArrayList<String> getChoices(TextView[] choices){
        ArrayList<String> choicesText = new ArrayList<>();
        for(TextView c: choices){
            choicesText.add(c.getText().toString());
        }
        return choicesText;
    }

    private int getRandomIndex(){
        int max = allFlashcards.size();
        int old = flashCardIndex;

        if(max == 1){
            return 0;
        }
        if(possibleIndex.isEmpty()){
            possibleIndex = resetPossibleIndex();
        }
        Random random = new Random();
        int newIndex = old;
        int posIndex = 0;
        while(newIndex == old){
            posIndex = random.nextInt(possibleIndex.size());
            newIndex = possibleIndex.get(posIndex);
        }
        possibleIndex.remove(posIndex);
        return newIndex;
    }

    private void updateCard(Flashcard f, String q, ArrayList<String> options){
        f.setQuestion(q);
        f.setAnswer(options.get(0));
        f.setWrongAnswer1(options.get(1));
        f.setWrongAnswer2(options.get(2));
        showCard();
    }

    private ArrayList<Integer> resetPossibleIndex(){
        ArrayList<Integer> arr = new ArrayList<>();
        for(int i = 0; i < allFlashcards.size(); i++){
            arr.add(i);
        }
        return arr;
    }

}