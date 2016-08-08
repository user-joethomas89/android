package com.example.joe.android_tutorial;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ActionMenuItemView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    int counter;
    Button add, sub;
    TextView display;
    MediaPlayer mySong;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Splash obj = new Splash();
        mySong = obj.getSong();

        counter = 0;
        mySong.start();
        add = (Button) findViewById(R.id.bAdd);
        sub = (Button) findViewById(R.id.bSub);
        display = (TextView) findViewById((R.id.tvDisplay));

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                counter++;
                display.setText("Your total is "+counter);
            }
        });

        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                counter--;
                display.setText("Your total is "+counter);
            }
        });

    }

    @Override
    protected void onDestroy() {

        mySong.release();
        super.onDestroy();
    }
}
