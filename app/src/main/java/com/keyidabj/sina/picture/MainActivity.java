package com.keyidabj.sina.picture;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.keyidabj.sina.picture.widget.MyViewPager;
import com.keyidabj.sina.picture.widget.NestedParent;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static String TAG_TOUCH_EVENT = "tag_touch_event";

    String TAG = "MainActivity_";

    protected FrameLayout fl_container;
    protected LinearLayout ll_content_container;
    protected RelativeLayout rl_recyclerview_container;

    protected NestedParent mScrollView;

    private MyPagerAdapter0 mAdapter0;

    private RecyclerView mRecyclerview;


    private MyViewPager mViewPager0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fl_container = findViewById(R.id.ll_container);
        ll_content_container = findViewById(R.id.ll_content_container);
        rl_recyclerview_container = findViewById(R.id.rl_recyclerview_container);

        mScrollView = findViewById(R.id.nested_parent);

        mRecyclerview = findViewById(R.id.recyclerview);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        List<String> objects = new ArrayList<>();
        for (int i = 0; i<100; i++){
            objects.add("pager --" + i);
        }
        MyAdapter myAdapter = new MyAdapter(this, objects);
        mRecyclerview.setAdapter(myAdapter);

        mViewPager0 = findViewById(R.id.viewpager_0);
        List<View> aList = new ArrayList<View>();
        LayoutInflater li = getLayoutInflater();
        aList.add(li.inflate(R.layout.item0, null, false));
        aList.add(li.inflate(R.layout.item0, null, false));
        aList.add(li.inflate(R.layout.item0, null, false));
        mAdapter0 = new MyPagerAdapter0(aList);
        mViewPager0.setAdapter(mAdapter0);
        mViewPager0.setBrotherScrollView(mScrollView);

        fl_container.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                int containerHeight = fl_container.getMeasuredHeight();
                fl_container.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                int titleHeight = (int)getResources().getDimension(R.dimen.title_height);
                int dp_40 = DensityUtil.dip2px(MainActivity.this, 40);

                ViewGroup.LayoutParams layoutParamsPager = ll_content_container.getLayoutParams();
                int contentContainerHeight = containerHeight - dp_40;
                layoutParamsPager.height = contentContainerHeight;
                ll_content_container.setLayoutParams(layoutParamsPager);

                ViewGroup.LayoutParams layoutParamsPager2 = rl_recyclerview_container.getLayoutParams();
                layoutParamsPager2.height = containerHeight - titleHeight;
                rl_recyclerview_container.setLayoutParams(layoutParamsPager2);

                int maxScrollHeight = contentContainerHeight - titleHeight;
                mScrollView.setMaxScrollHeight(maxScrollHeight);
                mViewPager0.setMaxScrollHeight(maxScrollHeight);

                TLog.i(TAG, "containerHeight : " + containerHeight);
                TLog.i(TAG, "contentContainerHeight : " + contentContainerHeight);
                TLog.i(TAG, "maxScrollHeight:" + maxScrollHeight);
            }
        });
    }

    private NestedParent.OnScrollListener myScrollListener = new NestedParent.OnScrollListener() {

        @Override
        public void onScroll(int scrollY) {
//            TLog.i(TAG, "scrollY: " + scrollY);
//            if (scrollY >= heightForScroll) {
//                tv_menu_1.setVisibility(View.GONE);
//                tv_menu_2.setVisibility(View.VISIBLE);
//            } else {
//                tv_menu_1.setVisibility(View.VISIBLE);
//                tv_menu_2.setVisibility(View.GONE);
//            }
        }

    };

    public class MyPagerAdapter0 extends PagerAdapter {
        private List<View> viewLists;

        public MyPagerAdapter0(List<View> viewLists) {
            super();
            this.viewLists = viewLists;
        }

        @Override
        public int getCount() {
            return viewLists.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(viewLists.get(position));
            return viewLists.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(viewLists.get(position));
        }
    }


    public static String getActionType(int action) {
        switch (action) {
            case 0:
                return "action_down";
            case 1:
                return "action_up";
            case 2:
                return "action_move";
        }
        return "other--" + action;
    }
}