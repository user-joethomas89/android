package com.example.joe.android_tut3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = (ProgressBar) findViewById(R.id.progress1);
        textView = (TextView) findViewById(R.id.text1);


        }


    public void startProgress(View view){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i<=10; i++){
                   final int value = i;
                    doFakeWork();
                    progressBar.post(new Runnable() {
                        @Override
                       public void run() {
                            textView.setText("Updating...");
                            progressBar.setProgress(value);
                            if(value==10){
                                textView.setText("Completed!!");
                            }
                        }
                    });
                }
            }
        };
       new Thread(runnable).start();
    }

    private void doFakeWork(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
