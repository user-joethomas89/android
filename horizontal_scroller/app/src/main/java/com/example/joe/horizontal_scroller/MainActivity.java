package com.example.joe.horizontal_scroller;

import android.app.Dialog;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private ImageButton imageButton;
    private ImageButton imageButton1;
    private ImageButton imageButton2;
    private ImageButton imageButton3;
    private ImageButton imageButton4;
    private ImageButton imageButton5;
    private int color;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.lertlayout);
        dialog.setTitle("Horizontal Scroll for Jio");
        dialog.show();

        imageButton = (ImageButton) dialog.findViewById(R.id.imageButton);
        imageButton1 = (ImageButton) dialog.findViewById(R.id.imageButton1);
        imageButton2 = (ImageButton) dialog.findViewById(R.id.imageButton2);
        imageButton3 = (ImageButton) dialog.findViewById(R.id.imageButton3);
        imageButton4 = (ImageButton) dialog.findViewById(R.id.imageButton4);
        imageButton5 = (ImageButton) dialog.findViewById(R.id.imageButton5);

        color = imageButton.getSolidColor();

        imageButton.setOnClickListener(this);
        imageButton1.setOnClickListener(this);
        imageButton2.setOnClickListener(this);
        imageButton3.setOnClickListener(this);
        imageButton4.setOnClickListener(this);
        imageButton5.setOnClickListener(this);


    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.imageButton:
                deselect();
                imageButton.setBackgroundColor(getResources().getColor(R.color.backgroundSelect));
                break;
            case R.id.imageButton1:
                deselect();
                imageButton1.setBackgroundColor(getResources().getColor(R.color.backgroundSelect));
                break;
            case R.id.imageButton2:
                deselect();
                imageButton2.setBackgroundColor(getResources().getColor(R.color.backgroundSelect));
                break;
            case R.id.imageButton3:
                deselect();
                imageButton3.setBackgroundColor(getResources().getColor(R.color.backgroundSelect));
                break;
            case R.id.imageButton4:
                deselect();
                imageButton4.setBackgroundColor(getResources().getColor(R.color.backgroundSelect));
                break;
            case R.id.imageButton5:
                deselect();
                imageButton5.setBackgroundColor(getResources().getColor(R.color.backgroundSelect));
                break;
        }
    }

    private void deselect(){
        imageButton.setBackgroundColor(color);
        imageButton1.setBackgroundColor(color);
        imageButton2.setBackgroundColor(color);
        imageButton3.setBackgroundColor(color);
        imageButton4.setBackgroundColor(color);
        imageButton5.setBackgroundColor(color);

    }
}
