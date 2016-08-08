package com.example.joe.android_tutorial_2;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button chkCmd = (Button) findViewById(R.id.bResults);
        final ToggleButton passTog = (ToggleButton) findViewById(R.id.tbPassword);
        final EditText input = (EditText) findViewById(R.id.etCommands);
        final TextView display = (TextView) findViewById(R.id.tvResults);

        assert passTog!=null;
        passTog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (passTog.isChecked()) {
                    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                }
            }

        });

        assert chkCmd != null;
        chkCmd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String check = input.getText().toString();
                if(check.contentEquals("Joe")){

                    display.setGravity(Gravity.LEFT);

                } else if (check.contentEquals("joe")){

                    display.setGravity(Gravity.RIGHT);
                } else {

                    Random crazy = new Random();
                    display.setText("WTF!!!!");
                    display.setTextSize(crazy.nextInt(100));
                    display.setGravity(Gravity.CENTER);
                    display.setTextColor(Color.RED);
                }
            }
        });
    }
}

