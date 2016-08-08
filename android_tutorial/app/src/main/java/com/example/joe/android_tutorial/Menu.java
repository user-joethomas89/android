package com.example.joe.android_tutorial;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

/**
 * Created by joe on 7/28/2016.
 */
public class Menu extends ListActivity {

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
}
