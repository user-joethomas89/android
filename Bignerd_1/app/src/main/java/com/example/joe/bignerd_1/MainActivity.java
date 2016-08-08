package com.example.joe.bignerd_1;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button trueButton;
    private Button falseButton;
    private TextView textView;
    private Button nextButton;
    private Button prevButton;
    private Button cheatButton;
    private int color;
    private boolean cheated;


    private TrueFalse[] mQuestionBank = new TrueFalse[]{
            new TrueFalse(R.string.question_oceans, true),
            new TrueFalse(R.string.question_mideast, false),
            new TrueFalse(R.string.question_africa, false),
            new TrueFalse(R.string.question_americas, true),
            new TrueFalse(R.string.question_asia, true)
    };

    private int mCurrentIndex;

    private void updateQuestion(){
        int question = mQuestionBank[mCurrentIndex].getQuestion();
        textView.setText(question);
    }

    private boolean checkAnswer(boolean userPressedTrue){
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isTrueQuestion();
        int messageResult = 0;
        if(userPressedTrue == answerIsTrue){
            messageResult = R.string.correct_toast;
            Toast.makeText(this, messageResult,Toast.LENGTH_SHORT).show();
            return true;
        } else {
            messageResult = R.string.incorrect_toast;
            Toast.makeText(this, messageResult,Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        trueButton = (Button) findViewById(R.id.trueButton);
        falseButton = (Button) findViewById(R.id.falseButton);
        textView = (TextView) findViewById(R.id.text);
        nextButton = (Button) findViewById(R.id.nextButton);
        prevButton = (Button) findViewById(R.id.prevButton);
        cheatButton = (Button) findViewById(R.id.cheatButton);

        color = prevButton.getSolidColor();

        if(savedInstanceState != null){
            mCurrentIndex = savedInstanceState.getInt("Index");
            cheated = savedInstanceState.getBoolean("cheated");
            cheatButton.setBackgroundColor(getResources().getColor(R.color.colorRed));
        }
        updateQuestion();

        trueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                falseButton.setBackgroundColor(color);
                boolean answer = checkAnswer(true);
                if (answer){
                    trueButton.setBackgroundColor(getResources().getColor(R.color.colorGreen));
                }
                else{
                    trueButton.setBackgroundColor(getResources().getColor(R.color.colorRed));

                }
            }
        });

        falseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean answer = checkAnswer(false);
                trueButton.setBackgroundColor(color);
                if (answer){
                    falseButton.setBackgroundColor(getResources().getColor(R.color.colorGreen));
                }
                else{
                    falseButton.setBackgroundColor(getResources().getColor(R.color.colorRed));

                }
            }
        });


        nextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
                trueButton.setBackgroundColor(color);
                falseButton.setBackgroundColor(color);


            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mCurrentIndex = (mCurrentIndex - 1) % mQuestionBank.length;
                updateQuestion();

            }
        });


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });
        cheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (MainActivity.this, Main2Activity.class);
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isTrueQuestion();
                intent.putExtra("ExtraAnswerTrue",answerIsTrue);
                startActivityForResult(intent, 0);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        cheated = data.getBooleanExtra("cheated",false);
        if(cheated){
            cheatButton.setBackgroundColor(getResources().getColor(R.color.colorRed));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("Index",mCurrentIndex);
        outState.putBoolean("cheated",cheated);
        super.onSaveInstanceState(outState);
    }
}
