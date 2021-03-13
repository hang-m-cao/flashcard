package com.example.flashcard;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AddCardActivity extends AppCompatActivity {
    ArrayList<String> optionText = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        ImageButton cancelBtn = findViewById(R.id.cancel_button);
        ImageButton saveBtn = findViewById(R.id.save_button);
        EditText question = findViewById(R.id.question);
        ArrayList<EditText> options = new ArrayList<>();
        options.add((findViewById(R.id.answer)));
        options.add((findViewById(R.id.option1)));
        options.add((findViewById(R.id.option2)));
        Intent intent = getIntent();

        if(intent.hasExtra("question") && intent.hasExtra("choices")){
            String new_q = intent.getExtras().getString("question");
            ArrayList<String> new_choices = intent.getExtras().getStringArrayList("choices");
            question.setText(new_q);
            for(int i = 0; i < new_choices.size(); i++){
                options.get(i).setText(new_choices.get(i));
            }
        }

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int emptyIndex = textEmpty(options, optionText);
                String questionText = question.getText().toString();

                if(emptyIndex == -1 && !questionText.isEmpty()){
                    Intent intent = new Intent(AddCardActivity.this, MainActivity.class);
                    intent.putExtra("question", questionText);
                    intent.putExtra("options", optionText);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                else if (questionText.isEmpty()){
                    Toast.makeText(AddCardActivity.this, "Please enter a question.",Toast.LENGTH_SHORT).show();
                }
                else if (emptyIndex == 0){
                    Toast.makeText(AddCardActivity.this, "Please enter the corresponding answer.",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(AddCardActivity.this, "Please enter another answer option.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private int textEmpty(ArrayList<EditText> text, ArrayList<String> optionText){
        for(int i = 0; i < text.size(); i++){
            String t = text.get(i).getText().toString();
            if(t.isEmpty()){
                return i;
            }
            else{
                optionText.add(t);
            }
        }
        return -1;
    }
}