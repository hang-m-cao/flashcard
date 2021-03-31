package com.example.flashcard;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.material.snackbar.Snackbar;
import com.plattysoft.leonids.ParticleSystem;

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
    CountDownTimer countDownTimer;
    ToggleButton viewChoices;

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
        viewChoices = findViewById(R.id.view_choices);

        next = findViewById(R.id.next);

        final Animation leftOutAnim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.left_out);
        final Animation rightInAnim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.right_in);

        countDownTimer = new CountDownTimer(16000, 1000) {
            public void onTick(long millisUntilFinished) {
                TextView timer = findViewById(R.id.timer);
                if (millisUntilFinished <= 6000) {
                    timer.setTextColor(getResources().getColor(R.color.red));
                }
                else {
                    timer.setTextColor(getResources().getColor(R.color.mantee));
                }
                timer.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                if(!choicesShow){
                    viewChoices.performClick();
                }

            }
        };

        question.setCameraDistance(30000);
        answer.setCameraDistance(30000);

        leftOutAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        flashCardIndex = getRandomIndex();
                        showCard();
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        question.startAnimation(rightInAnim);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }

                });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                question.startAnimation(leftOutAnim);
                choicesShow = true;
                viewChoices.setChecked(true);
                viewChoices.performClick();
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
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
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
                for(TextView c : choices){
                    c.setVisibility(viewVisibility);
                }
            }
        });

        question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!choicesShow){

                    question.animate()
                            .rotationY(-90)
                            .setDuration(200)
                            .withEndAction(
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            answer.setVisibility(View.VISIBLE);
                                            question.setVisibility(View.INVISIBLE);

                                            answer.setRotationY(90);
                                            answer.animate()
                                                    .rotationY(0)
                                                    .setDuration(200)
                                                    .start();
                                        }
                                    }
                            ).start();

                }
            }
        });

        answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer.animate()
                        .rotationY(-90)
                        .setDuration(200)
                        .withEndAction(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        question.setVisibility(View.VISIBLE);
                                        answer.setVisibility(View.INVISIBLE);
                                        // second quarter turn
                                        question.setRotationY(90);
                                        question.animate()
                                                .rotationY(0)
                                                .setDuration(200)
                                                .start();
                                    }
                                }
                        ).start();
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
                if(msg.equals("Correct!")){
                    new ParticleSystem(MainActivity.this, 100, R.drawable.confetti, 3000)
                            .setSpeedRange(0.2f, 0.5f)
                            .oneShot(btn, 100);
                }
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
            startTimer();
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
            possibleIndex = resetPossibleIndex();
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

    private void startTimer() {
        countDownTimer.cancel();
        countDownTimer.start();
    }

}