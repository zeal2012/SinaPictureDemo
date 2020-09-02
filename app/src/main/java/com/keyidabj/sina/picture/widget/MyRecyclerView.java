package com.keyidabj.sina.picture.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class MyRecyclerView extends RecyclerView {

    String TAG = "MyRecyclerView_";

    public MyRecyclerView(@NonNull Context context) {
        super(context);
    }

    public MyRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
        Log.i(TAG, "onScrolled -- dy: " + dy);
    }

    @Override
    public boolean fling(int velocityX, int velocityY) {
        Log.i(TAG, "fling -- velocityY: " + velocityY);
        return super.fling(velocityX, velocityY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        return super.onTouchEvent(e);
    }
}
