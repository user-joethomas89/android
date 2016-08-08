package com.example.joe.android_tutorial;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

/**
 * Created by joe on 7/28/2016.
 */
public class Splash extends Activity {

    static MediaPlayer mysong;
    public Splash(){

   }
    protected MediaPlayer getSong(){

        return mysong;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        mysong = MediaPlayer.create(Splash.this,R.raw.witharmswideopen);


        Thread timer = new Thread(){

            public void run(){
                try{
                    sleep(2000);
                } catch (InterruptedException e){
                    e.printStackTrace();
                } finally {
                    Intent intent = new Intent(Splash.this,MainActivity.class);
                    startActivity(intent);
                }
            }
        };
        timer.start();


    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
