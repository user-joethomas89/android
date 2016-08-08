package com.example.joe.bignerd_1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity {

    private Button showAnswerButton;
    private TextView cheatTextView;
    private Intent data = new Intent();

    private boolean mAnswerIsTrue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        showAnswerButton = (Button) findViewById(R.id.showAnswerButton);
        cheatTextView = (TextView) findViewById(R.id.cheatTextView);

        mAnswerIsTrue = getIntent().getBooleanExtra("ExtraAnswerTrue", false);

        data.putExtra("cheated",false);


        showAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (mAnswerIsTrue){
                   cheatTextView.setText(R.string.true_button);
               }
                else{
                   cheatTextView.setText(R.string.false_button);
               }
                data.putExtra("cheated",true);

                setResult(0,data);
            }
        });
    }

    @Override
    protected void onStop() {

        if(!data.getBooleanExtra("cheated",false)){
            data.putExtra("cheated",false);
        }
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
    }
}
