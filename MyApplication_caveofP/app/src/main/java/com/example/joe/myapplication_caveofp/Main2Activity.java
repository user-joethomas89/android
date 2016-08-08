package com.example.joe.myapplication_caveofp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class Main2Activity extends AppCompatActivity {

    private TextView textView;
    private Button button;
    private File imageFile;
    private static final int PHOTO_TAKEN = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imageFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"fromMyApp");
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
                if (imageFile != null){
                    Log.e("image not null","!!");
                    startActivityForResult(i,PHOTO_TAKEN);
                }
                else{

                    Toast.makeText(Main2Activity.this,"Image file is null!!",Toast.LENGTH_LONG).show();

                }


            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_TAKEN) {
            Bitmap photo = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            if (photo != null){
                ImageView imageView = (ImageView) findViewById(R.id.image1);
                imageView.setImageBitmap(photo);
            }
            else{
                Toast.makeText(this, "Unable to save file!!", Toast.LENGTH_LONG).show();
            }

        }
        setResult(RESULT_OK);
    }
}
