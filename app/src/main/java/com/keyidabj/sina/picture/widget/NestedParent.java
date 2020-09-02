package com.keyidabj.sina.picture.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
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

    public NestedParent(Context context) {
        super(context);
    }

    public NestedParent(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NestedParent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
                    mRecyclerView.isAnimating();
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
        super.onNestedPreScroll(target, dx, dy, consumed, type);
//        TLog.i.i(TAG, "onNestedPreScroll --- dy: " +dy);
        if (maxScrollHeight == 0) {
            super.onNestedPreScroll(target, dx, dy, consumed);
            return;
        }

        int scrollY = getScrollY();
        //向下滑动时
        if (dy > 0) {
            if (scrollY + dy <= maxScrollHeight) {
                offset(dy, consumed);
            } else {
                offset(maxScrollHeight - scrollY, consumed);
            }
        }
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        TLog.i(TAG, "onNestedFling ----------------consumed : " + consumed);
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
        TLog.i(TAG_, getClass().getSimpleName() + "::onTouchEvent() -- ev: " + MainActivity.getActionType(ev.getAction()));
        super.onTouchEvent(ev);
        return false;
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
