package com.example.joe.project3_sidedrawer;

import android.os.Bundle;
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

public class GalleryFragment extends Fragment {

    private static final String TAG = GalleryFragment.class.getSimpleName();
    private boolean animationPlayed = false;
    private View rootView;
    private ImageView imageView;
    private TextView textView;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        imageView = (ImageView) rootView.findViewById(R.id.imageJio);
        textView = (TextView) rootView.findViewById(R.id.textJio);
        if (savedInstanceState != null) {
            animationPlayed = savedInstanceState.getBoolean("animationPlayed");
        }
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        Animation animation;

        if (nextAnim != 0) {
            animation = AnimationUtils.loadAnimation(getActivity(), nextAnim);

        } else {
            animation = AnimationUtils.loadAnimation(getActivity(), R.anim.exit);
        }
        animation.setAnimationListener(new Animation.AnimationListener() {
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
                if (!animationPlayed) {
                    Log.d(TAG, "Animation played is false, Jio animation played.");

                    Animation imageAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.enter_image_jio);
                    imageView.startAnimation(imageAnimation);
                    imageView.setVisibility(View.VISIBLE);
                    imageAnimation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
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
                    animationPlayed = false;
                    Log.d(TAG, "Animation played set to false");
                }
            }
        });
        return animation;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_gallery, container, false);
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("animationPlayed", true);
        Log.d(TAG, "Animation played set to true in \"onSaveInstanceState\"");
        super.onSaveInstanceState(outState);
    }
}
