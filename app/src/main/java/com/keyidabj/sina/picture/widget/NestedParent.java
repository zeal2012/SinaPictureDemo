package com.keyidabj.sina.picture.widget;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.keyidabj.sina.picture.MainActivity;
import com.keyidabj.sina.picture.TLog;

import java.lang.reflect.Method;

public class NestedParent extends NestedScrollView {

    String TAG = "NestedParent_";
    String TAG_ = MainActivity.TAG_TOUCH_EVENT;

    //方便测试先固定。
    private int maxScrollHeight = 0;
    //    private View maxHeightView;
    private RecyclerView mRecyclerView;

    private Handler mHandler;

    private boolean isChildFlingInvoked;
    private int scrollYAtActionDown;

    public NestedParent(Context context) {
        super(context);
        init();
    }

    public NestedParent(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NestedParent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        mHandler = new Handler();
    }

//    public void setMaxHeightView(View view) {
//        this.maxHeightView = view;
//    }

    public void setMaxScrollHeight(int maxHeight) {
        this.maxScrollHeight = maxHeight;
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (maxHeightView != null){
//            if (maxHeight == 0){
//                //int titleBarHeight = getContext().getResources().getDimensionPixelSize(R.dimen.home_title_height);
//                int titleBarHeight = 0;
//                int StatusBar = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? StatusBarUtil.getStatusBarHeight(getContext()) : 0;
//                maxHeight = maxHeightView.getHeight() - StatusBar - titleBarHeight;
//                TTLog.i.i(TAG, "maxHeight: " + maxHeight);
//            }
//        }
//        return super.dispatchTouchEvent(ev);
//    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        TLog.i(TAG_, getClass().getSimpleName() + "::onInterceptTouchEvent() -- ev: " + MainActivity.getActionType(ev.getAction()));
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN: {
//                if (!mScroller.isFinished()) {
//                    mScroller.abortAnimation();
//                }
                if (mRecyclerView != null) {
                    mRecyclerView.stopScroll();
                }
                abortAnimatedScroll();
                break;
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    //对应子view 的dispatchNestedPreScroll方法， 最后一个数组代表消耗的滚动量，下标0代表x轴，下标1代表y轴
    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        //super.onNestedPreScroll(target, dx, dy, consumed, type);
        TLog.i(TAG, "onNestedPreScroll --dy :" + dy + " -- type : " + type);
//        TLog.i.i(TAG, "onNestedPreScroll --- dy: " +dy);
        if (maxScrollHeight == 0) {
            super.onNestedPreScroll(target, dx, dy, consumed);
            return;
        }
        int scrollY = getScrollY();
        //recyclerView向下fling时，如果滑倒了maxScrollHeight的高度，则停止滑动
        if (type == ViewCompat.TYPE_NON_TOUCH
                && dy < 0
                && scrollY < maxScrollHeight) {

            if (mRecyclerView == null) {
                mRecyclerView = (RecyclerView) target;
            }
            mRecyclerView.stopScroll();
            //多滑出来的部分，回弹回去
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    smoothScrollTo(0, maxScrollHeight);
                }
            });

            TLog.i(TAG, "onNestedPreScroll --scrollY :" + scrollY + " -- maxScrollHeight : " + maxScrollHeight);
            return;
        }

        //向下滑动时
        if (dy > 0) {
            if (scrollY + dy <= maxScrollHeight) {
                offset(dy, consumed);
            } else {
                offset(maxScrollHeight - scrollY, consumed);
            }
        }
    }

    //0 TYPE_TOUCH
    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, @NonNull int[] consumed) {
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type, consumed);
        //TLog.i(TAG, "onNestedScroll --dyConsumed :" + dyConsumed + " -- dyUnconsumed : " + dyUnconsumed + " -- consumed: " + consumed[1] + " -- type : " + type);
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        TLog.i(TAG, "onNestedFling ----------------velocityY : " + velocityY);
        return super.onNestedFling(target, velocityX, velocityY, consumed);
    }

    //返回true代表父view消耗滑动速度，子View将不会滑动
    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        TLog.i(TAG, "onNestedPreFling ----------------");
        if (maxScrollHeight == 0) {
            return super.onNestedPreFling(target, velocityX, velocityY);
        }

        int scrollY = getScrollY();
        TLog.i(TAG, "onNestedPreFling --- velocityY: " + velocityY);
        TLog.i(TAG, "onNestedPreFling --- scrollY: " + scrollY);

        if (scrollY < maxScrollHeight) {
            if (velocityY < 0) {
                smoothScrollTo(0, 0);
            } else {
                smoothScrollTo(0, maxScrollHeight);
            }
            isChildFlingInvoked = true;
            return true;
        }
        return super.onNestedPreFling(target, velocityX, velocityY);
    }

    private void offset(int dy, int[] consumed) {
        if (dy != 0) {
            scrollBy(0, dy);
            consumed[1] = dy;  //dy： 父view消耗了多少
        }
    }

    private void abortAnimatedScroll() {
        try {
            Method abortAnimatedScroll = getClass().getSuperclass().getDeclaredMethod("abortAnimatedScroll");
            abortAnimatedScroll.setAccessible(true);
            abortAnimatedScroll.invoke(this);
            TLog.i(TAG, "abortAnimatedScroll() -- onInterceptTouchEvent()");
        } catch (Exception e) {
            e.printStackTrace();
            TLog.i(TAG, "abortAnimatedScroll() -- Exception: " + e.getMessage());
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        TLog.i(TAG, getClass().getSimpleName() + "::onTouchEvent() -- ev: " + MainActivity.getActionType(ev.getAction()));
        super.onTouchEvent(ev);
        return false;
    }

    public void actionDownInChildView(){
        Log.i(TAG, "actionDownInChildView");
        isChildFlingInvoked = false;
        scrollYAtActionDown = getScrollY();
    }

    public void actionUpInChildView(){
        Log.i(TAG, "actionUpInChildView -- isChildFlingInvoked: " + isChildFlingInvoked);
        if (!isChildFlingInvoked){
            float scrollY = getScrollY();
            if (scrollY > 0 && scrollY < maxScrollHeight){
                if (scrollY > scrollYAtActionDown){
                    smoothScrollTo(0, maxScrollHeight);
                }else{
                    smoothScrollTo(0, 0);
                }
            }
        }
    }

    /********************  滑动监听 *******************************/

    private OnScrollListener listener;

    public interface OnScrollListener {
        void onScroll(int scrollY);
    }

    public void setOnScrollListener(OnScrollListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (listener != null) {
            listener.onScroll(t);
        }
    }

    /********************  滑动监听end *******************************/
}
