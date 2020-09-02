package com.keyidabj.sina.picture.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.keyidabj.sina.picture.MainActivity;
import com.keyidabj.sina.picture.TLog;

public class MyFramlatout extends FrameLayout {

    String TAG = MainActivity.TAG_TOUCH_EVENT;

    public MyFramlatout(@NonNull Context context) {
        super(context);
    }

    public MyFramlatout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyFramlatout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        TLog.i(TAG, getClass().getSimpleName() + "::dispatchTouchEvent() -- ev: " + MainActivity.getActionType(ev.getAction()));
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        TLog.i(TAG, getClass().getSimpleName() + "::onInterceptTouchEvent() -- ev: " + MainActivity.getActionType(ev.getAction()));
//        if (ev.getAction() == MotionEvent.ACTION_MOVE){
//            return true;
//        }else{
//            return false;
//        }
        return super.onInterceptTouchEvent(ev);
//        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        TLog.i(TAG, getClass().getSimpleName() + "::onTouchEvent() -- ev: " + MainActivity.getActionType(ev.getAction()));
        return super.onTouchEvent(ev);
//        if (ev.getAction() == MotionEvent.ACTION_DOWN){
//            return true;
//        }
//        return false;
    }


}
