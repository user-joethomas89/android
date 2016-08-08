package com.example.joe.listview_image_glide;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class BlankFragment extends Fragment {

    ImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        String filePath = this.getArguments().getString("path");
        imageView = (ImageView) view.findViewById(R.id.full_image_view);

        Glide.with(this)
                .load(filePath)
                .into(imageView);
        return view;

    }
}