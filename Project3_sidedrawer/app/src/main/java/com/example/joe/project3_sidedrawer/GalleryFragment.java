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
    private int animationFlag = -1;
    Animation animation;
    View rootView;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            animationFlag = savedInstanceState.getInt("flag");
        }
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
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
                if (animationFlag == -1) {
                    Log.d(TAG, "Flag -1, Jio animation played.");
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
                    animationFlag = -1;
                    Log.d(TAG, "Flag set to -1");

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
        outState.putInt("flag", 111);
        Log.d(TAG, "Flag set to 111 in \"onSaveInstanceState\"");
        super.onSaveInstanceState(outState);
    }
}
