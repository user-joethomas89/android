package com.example.joe.listview_image_glide;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import android.support.v4.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends FragmentActivity {

    private String[] filePathStrings;
    private String[] fileNameStrings;
    private String[] fileDateStrings;
    private List<File> listFile = new ArrayList<>();;
    ListView listView;
    ListViewAdapter adapter;
    File file;
    Date lastModDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "Error! No SDCARD Found!", Toast.LENGTH_LONG)
                    .show();
        } else {
            Log.e("SD card", " present!!!");
            file = new File((Environment.getExternalStorageDirectory().getPath()));
            Log.e("File is: ", file.toString());
        }

        load_image_files(file);

        filePathStrings = new String[listFile.size()];
        fileNameStrings = new String[listFile.size()];
        fileDateStrings = new String[listFile.size()];

        Collections.sort(listFile, new Comparator<File>() {
            @Override
            public int compare(File lhs, File rhs) {
                return -Long.valueOf(lhs.lastModified()).compareTo(rhs.lastModified());
            }
        });


        for (int i = 0; i < listFile.size(); i++) {
            filePathStrings[i] = listFile.get(i).getAbsolutePath();
            fileNameStrings[i] = listFile.get(i).getName();
            lastModDate = new Date(listFile.get(i).lastModified());
            fileDateStrings[i] = lastModDate.toString();
        }

        listView = (ListView) findViewById(R.id.listView);
        adapter = new ListViewAdapter(this, filePathStrings, fileNameStrings, fileDateStrings);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Fragment frag;
                Bundle bundle = new Bundle();
                bundle.putString("path",filePathStrings[position]);
                frag= new BlankFragment();
                frag.setArguments(bundle);
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                        .replace(R.id.fragment, frag)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    private void load_image_files(File dir) {
        String extention = ".jpg";
        File[] listFile2 = dir.listFiles();
        if (listFile2 != null) {
            for (File file: listFile2) {
                if (file.isDirectory() && !file.getName().contains(".thumbnails")) {
                    load_image_files(file);
                } else {
                    if (file.getName().endsWith(extention)) {
                        listFile.add(file);
                    }
                }
            }
        }
    }

    public void share(View view) {

        int position = listView.getPositionForView(view);
        String imagePath = listFile.get(position).toString();
        File file = new File(imagePath);
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        this.startActivity(intent);

    }
}



