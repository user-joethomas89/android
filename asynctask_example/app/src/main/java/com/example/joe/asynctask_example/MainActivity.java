package com.example.joe.asynctask_example;

import android.app.DownloadManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.text1);
        editText = (EditText) findViewById(R.id.edit1);

    }

    private class download extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... urls) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(urls[0])
                    .build();
            Response response = null;
            try {
                response = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {

                if (response.isSuccessful()) {

                    try {
                        return response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }catch (NullPointerException e){
                e.printStackTrace();
            }

            return "Download Failed!!";
        }

        @Override
        protected void onPostExecute(String result) {
            textView.setText(result);
        }
    }
    public void onClick(View view){
        download task = new download();
        String url = editText.getText().toString();
        try {
            if (URLUtil.isHttpUrl(url) && !url.contains(" ")) {
                task.execute(editText.getText().toString());
            } else {
                textView.setText("Wrong Format!!");
            }
        }
        catch (IllegalArgumentException e){
            e.printStackTrace();
        }

    }
}
