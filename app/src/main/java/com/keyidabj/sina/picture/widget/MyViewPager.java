package com.keyidabj.sina.picture.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.keyidabj.sina.picture.MainActivity;
import com.keyidabj.sina.picture.TLog;

public class MyViewPager extends ViewPager {

    String TAG = "MyViewPager_";
    String TAG_ = MainActivity.TAG_TOUCH_EVENT;

    private final int MAXIMUM_FLING_VELOCITY = 8000;
    private final int MINIMUM_FLING_VELOCITY = 50;

    private NestedParent mScrollView;

    private VelocityTracker mVelocityTracker;

    private int maxScrollHeight;

    public MyViewPager(@NonNull Context context) {
        super(context);
        init();
    }

    public MyViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mVelocityTracker = VelocityTracker.obtain();
    }

    public void setMaxScrollHeight(int maxScrollHeight) {
        this.maxScrollHeight = maxScrollHeight;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        TLog.i(TAG_, getClass().getSimpleName() + "::dispatchTouchEvent() -- ev: " + MainActivity.getActionType(ev.getAction()));
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        TLog.i(TAG_, getClass().getSimpleName() + "::onInterceptTouchEvent() -- ev: " + MainActivity.getActionType(ev.getAction()));
        return super.onInterceptTouchEvent(ev);
//        return true;
    }

    public void setBrotherScrollView(NestedParent mScrollView) {
        this.mScrollView = mScrollView;
    }

   /* @Override
    public boolean onTouchEvent(MotionEvent ev) {
        TLog.i(TAG, getClass().getSimpleName() + "::onTouchEvent() -- ev: " + MainActivity.getActionType(ev.getAction()));
//        return super.onTouchEvent(ev);
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            return true;
        }
        return false;
    }*/


    // 分别记录上次滑动的坐标
    private int mLastX = 0;
    private int mLastY = 0;

    //
    boolean firstMove;
    boolean consumeBySelf;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        TLog.i(TAG_, getClass().getSimpleName() + "::onTouchEvent() -- ev: " + MainActivity.getActionType(ev.getAction()));
        TLog.i(TAG, "onTouchEvent() -- firstMove: " + firstMove + " -- consumeBySelf:" + consumeBySelf);
        mVelocityTracker.addMovement(ev);

        int x = (int) ev.getX();
        int y = (int) ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                super.onTouchEvent(ev);
                firstMove = true;
                consumeBySelf = true;
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                int deltaX = x - mLastX;
                int deltaY = y - mLastY;
                TLog.i(TAG, "move, deltaX:" + deltaX + " deltaY:" + deltaY);
                if (Math.abs(deltaX) == Math.abs(deltaY)) {
                    super.onTouchEvent(ev);
                    break;
                }
                if (firstMove) {
                    if (Math.abs(deltaX) < Math.abs(deltaY)) {
                        consumeBySelf = false;
                        mScrollView.scrollBy(0, -deltaY);
                    } else {
                        super.onTouchEvent(ev);
                    }
                    firstMove = false;
                } else {
                    if (consumeBySelf) {
                        super.onTouchEvent(ev);
                    } else {
                        mScrollView.scrollBy(0, -deltaY);
                    }
                }
                break;
            }
            case MotionEvent.ACTION_UP: {
                if (consumeBySelf) {
                    super.onTouchEvent(ev);
                } else {
                    mVelocityTracker.computeCurrentVelocity(1000, MAXIMUM_FLING_VELOCITY);
                    int yVelocity = (int) mVelocityTracker.getYVelocity();
//                    if ((Math.abs(yVelocity) > MINIMUM_FLING_VELOCITY)) {
//                        mScrollView.fling(-yVelocity);
//                    }

                    //根据yVelocity判断是向下滑还是向上滑
                    if (yVelocity < 0) { //向上滑动, 则向上滑动到顶部
                        mScrollView.smoothScrollTo(0, maxScrollHeight);
                    } else { //向下滑动
                        mScrollView.smoothScrollTo(0, 0);
                    }
                    Log.i(TAG, "yVelocity: " + yVelocity);
                }
                mVelocityTracker.clear();
                break;
            }
            default:
                break;
        }
        mLastX = x;
        mLastY = y;
        return true;
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
        super.onDetachedFromWindow();
    }
}
