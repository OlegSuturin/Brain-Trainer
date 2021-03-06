package com.example.btaintrainer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView textViewTimer;
    private TextView textViewScore;
    private TextView textViewExample;
    private TextView textViewAnswer0;
    private TextView textViewAnswer1;
    private TextView textViewAnswer2;
    private TextView textViewAnswer3;
    private ArrayList<TextView> arrayTextView;

    private int valueA, valueB, valueResult;
    private int randomButtonRightAnswer;
    private final int MIN = -100, MAX = 100;
    private int scoreIndividual, scoreIndividualAll;
    private int scoreRecord;

    private boolean gameOver = false;

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewTimer = findViewById(R.id.textViewTimer);
        textViewScore = findViewById(R.id.textViewScore);
        textViewExample = findViewById(R.id.textViewExample);
        textViewAnswer0 = findViewById(R.id.textViewAnswer0);
        textViewAnswer1 = findViewById(R.id.textViewAnswer1);
        textViewAnswer2 = findViewById(R.id.textViewAnswer2);
        textViewAnswer3 = findViewById(R.id.textViewAnswer3);

        arrayTextView = new ArrayList<>();
        arrayTextView.add(textViewAnswer0);
        arrayTextView.add(textViewAnswer1);
        arrayTextView.add(textViewAnswer2);
        arrayTextView.add(textViewAnswer3);


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        scoreRecord = preferences.getInt("score", 0);
        Toast.makeText(MainActivity.this, "Действующий рекорд " + scoreRecord, Toast.LENGTH_SHORT).show();


        scoreIndividualAll = scoreIndividual = 0;
        // textViewScore.setText(scoreIndividual + "/" + scoreIndividualAll);
        setPlay();


        CountDownTimer timer = new CountDownTimer(90000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {


                int second = (int) (millisUntilFinished / 1000);
                textViewTimer.setText(getTime(millisUntilFinished));
                if (second <= 10) {
                    textViewTimer.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                }
                else
                    textViewTimer.setTextColor(getResources().getColor(android.R.color.black));
            }

            @Override
            public void onFinish() {
                gameOver = true;
                if (scoreIndividual > scoreRecord) {
                    preferences.edit().putInt("score", scoreIndividual).apply();
                    //Toast.makeText(MainActivity.this, "Новый рекорд " +scoreIndividual, Toast.LENGTH_SHORT).show();

                }
                Intent intent = new Intent(MainActivity.this, ActivityEndOfGame.class);
                intent.putExtra("scorecurrent", scoreIndividual);
                intent.putExtra("record", scoreRecord);
                startActivity(intent);

            }
        };
        timer.start();

    }

    public void setPlay() {                    // установкаа примера на экран

        valueA = randomFunction(MIN, MAX);
        valueB = randomFunction(MIN, MAX);
        valueResult = valueA + valueB;
        if (valueB >= 0) {
            textViewExample.setText("" + valueA + " +" + valueB + " =");
        } else {
            textViewExample.setText("" + valueA + " " + valueB + " =");
        }

        randomButtonRightAnswer = (int) (Math.random() * arrayTextView.size());   //установка правильного ответа в случайную кнопку, в остальные кн. -неправильные значения
        //Toast.makeText(this, ""+randomButtonRightAnswer, Toast.LENGTH_SHORT).show();
        int wrongInt = 0;
        for (int i = 0; i < arrayTextView.size(); i++) {
            if (i == randomButtonRightAnswer)
                arrayTextView.get(i).setText(Integer.toString(valueResult));
            else{
                do {     //если случайно получилось, что неправильный ответ = правильному, повторить
                    wrongInt = randomFunction(2 * MIN, 2 * MAX);
                }while (wrongInt == valueResult);
                arrayTextView.get(i).setText(Integer.toString(wrongInt));
            }

        }
        textViewScore.setText(scoreIndividual + "/" + scoreIndividualAll);

    }


    public void onClickAnswer(View view) {
        if (!gameOver) {
            scoreIndividualAll++;
            TextView textView = (TextView) view;
            String tag = textView.getTag().toString();
            if (Integer.parseInt(tag) == randomButtonRightAnswer) {
                Toast.makeText(this, "Правильный ответ!", Toast.LENGTH_SHORT).show();
                scoreIndividual++;

            } else {
                Toast.makeText(this, "Неправильный ответ!", Toast.LENGTH_SHORT).show();
            }

            setPlay();
        }

    }

    public int randomFunction(int a, int b) {   // поиск случайного числа из диапазона значений от a до b

        int result = (int) ((Math.random() * (b - a)) + a);
        return result;
    }

    public String getTime(long millis){  // перевод миллис в формат мин/секс
        int seconds = (int) (millis/1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;

        return String.format("%02d:%02d", minutes, seconds);
    }

}