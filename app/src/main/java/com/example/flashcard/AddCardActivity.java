package com.example.flashcard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class AddCardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        ImageButton cancelBtn = findViewById(R.id.cancel_button);
        EditText questionText = findViewById(R.id.question);
        EditText answerText = findViewById(R.id.answer);
        EditText option1 = findViewById(R.id.option1);
        EditText option2 = findViewById(R.id.option2);
        ImageButton saveBtn = findViewById(R.id.save_button);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String question = questionText.getText().toString();
                String answer = answerText.getText().toString();
                if(!(question.isEmpty() || answer.isEmpty())){
                    Intent intent = new Intent(AddCardActivity.this, MainActivity.class);
                    intent.putExtra("question", question);
                    intent.putExtra("answer", answer);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                else if (question.isEmpty()){
                    Toast.makeText(AddCardActivity.this, "Please enter a question.",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(AddCardActivity.this, "Please enter an answer.",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}