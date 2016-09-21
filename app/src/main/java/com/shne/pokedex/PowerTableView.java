package com.shne.pokedex;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;

/**
 * Created by Shlomo on 01-Sep-16.
 */
public class PowerTableView extends Fragment {
    ImageView imageView;
    Matrix matrix = new Matrix();
    Float scale = 1f;
    ScaleGestureDetector SGD;
    private static final float MIN_SCALE = 0.95f;

    private float mLastScaleFactor = 0;
    private float mTouchY;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        WebView view = (WebView) inflater.inflate(R.layout.powertable_view, null);
        view.loadUrl("https://img.pokemondb.net/images/typechart.png");
        return view;
    }



}