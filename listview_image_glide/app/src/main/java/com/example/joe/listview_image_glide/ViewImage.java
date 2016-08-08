package com.example.joe.listview_image_glide;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import com.bumptech.glide.Glide;

public class ViewImage extends Activity {

    ImageView imageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_image);

        Intent intent = getIntent();

        int position = intent.getExtras().getInt("position");
        String[] filepath = intent.getStringArrayExtra("filepath");
        imageView = (ImageView) findViewById(R.id.full_image_view);

        Glide.with(this)
                .load(filepath[position])
                .into(imageView);
    }
}