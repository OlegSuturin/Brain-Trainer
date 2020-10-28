package com.example.btaintrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ActivityEndOfGame extends AppCompatActivity {
    TextView textViewScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_of_game);
        textViewScore = findViewById(R.id.textViewScore);

        Intent intent = getIntent();
        if (intent.hasExtra("scorecurrent")){
            int scorecurrent, record;

            scorecurrent = intent.getIntExtra("scorecurrent", 0);
            record = intent.getIntExtra("record", 0);

            textViewScore.setText(String.format("Ваш результат: %d\n Рекорд: %d", scorecurrent, record));

        }
    }

    public void onClickButton(View view) { // начать новую игру
        Intent intentBack = new Intent(this, MainActivity.class);
        startActivity(intentBack);

    }
}