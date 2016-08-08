package com.example.joe.project3_sidedrawer;


import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;


public class GalleryFragment extends Fragment {

    private int flag = -1;
    Animation anim;
    private static final String TAG = "MyFragment";
    View rootView;

    public GalleryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            flag = savedInstanceState.getInt("flag");
        }
        Log.e("flag onactivity ", String.valueOf(flag));
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (nextAnim != 0) {
            anim = AnimationUtils.loadAnimation(getActivity(), nextAnim);
        } else {
            anim = AnimationUtils.loadAnimation(getActivity(), R.anim.exit);
        }
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.d(TAG, "Animation started.");
                // additional functionality
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                Log.d(TAG, "Animation repeating.");
                // additional functionality
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (flag == -1) {
                    ImageView imageView = (ImageView) rootView.findViewById(R.id.imageJio);
                    Animation imageAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.enter_image_jio);
                    imageView.startAnimation(imageAnimation);
                    imageView.setVisibility(View.VISIBLE);
                    imageAnimation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            TextView textView = (TextView) rootView.findViewById(R.id.textJio);
                            Animation textAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.enter_jio_text);
                            textView.startAnimation(textAnimation);
                            textView.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                } else {
                    ImageView imageView = (ImageView) rootView.findViewById(R.id.imageJio);
                    imageView.setVisibility(View.VISIBLE);
                    TextView textView = (TextView) rootView.findViewById(R.id.textJio);
                    textView.setVisibility(View.VISIBLE);
                    flag = -1;
                }
            }
        });
        return anim;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_gallery, container, false);
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("flag", 111);
        super.onSaveInstanceState(outState);
    }
}
