package com.d8sense.tgic;

/**
 * Created by Jason.z on 2018/10/2.
 * Copyright (c) 2016 BeikePay. All rights reserved.
 */
import java.util.ArrayList;
import java.util.List;

import com.d8sense.tgic.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


public class GuideView extends RelativeLayout
{
    private ViewPager mViewPager;
    private LinearLayout mPoints;
    private View mRedPoint;
    private Button mStartExp;
    private List<ImageView> mIvs = new ArrayList<ImageView>();
    float leftWidth;
    private OnStartExpClickListener onStartExpClickListener;

    /** 设置按钮点击监听接口 */
    public interface OnStartExpClickListener
    {
        void clickStartExp(Button button);
    }

    Context context;

    /** 设置图片资源 */
    public void setImgs(List<ImageView> mIvs) {
        this.mIvs = mIvs;

        initData();
    }

    /** 设置开始体验按钮的监听 */
    public void setOnStartExpClickListener(OnStartExpClickListener onStartExpClickListener) {
        this.onStartExpClickListener = onStartExpClickListener;
    }

    public GuideView(Context context) {
        super(context, null);
    }

    public GuideView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        View.inflate(context, R.layout.guide_view, this);

        initView();
    }

    /** 初始化视图 */
    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.vp_guide);
        mPoints = (LinearLayout) findViewById(R.id.ll_points);
        mRedPoint = findViewById(R.id.v_guide_redpoint);
        mStartExp = (Button) findViewById(R.id.btn_starExp);
    }

    /** 初始化所有数据，前提是设置好图片资源 */
    private void initData() {
        if (mIvs != null) {
            for (int i = 0; i < mIvs.size(); i++) {
                // 根据资源的个数创建灰色的点
                View p = new View(context);
                p.setBackgroundResource(R.drawable.graypoint);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dip2px(10), dip2px(10));
                if (i != 0) {
                    params.leftMargin = dip2px(10);
                }

                p.setLayoutParams(params);

                // 加入线性容器
                mPoints.addView(p);
            }
        }
        mViewPager.setAdapter(new MyPagerAdapter());
        mViewPager.setPageTransformer(true, new DepthPageTransformer());

        initEvent();
    }

    /** 初始化监听，前提是先设置好开始体验按钮的监听，否则点击按钮无效，其他正常 */
    @SuppressWarnings("deprecation")
    public void initEvent() {
        // 监听布局完成，触发结果
        mPoints.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                leftWidth = mPoints.getChildAt(1).getLeft() - mPoints.getChildAt(0).getLeft();
                mPoints.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });

        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (position == mIvs.size() - 1) {
                    mStartExp.setVisibility(View.VISIBLE);
                    if (onStartExpClickListener != null) {
                        onStartExpClickListener.clickStartExp(mStartExp);
                    }
                } else {
                    mStartExp.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                float point_leftMargin = leftWidth * (positionOffset + position);
                RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) mRedPoint
                        .getLayoutParams();
                params.leftMargin = (int) (point_leftMargin + .5f);
                // 重新设置布局
                mRedPoint.setLayoutParams(params);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // TODO Auto-generated method stub
            }
        });

    }

    class MyPagerAdapter extends PagerAdapter
    {
        @Override
        public int getCount() {
            if (mIvs != null) {
                return mIvs.size();
            }
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView iv = mIvs.get(position);

            container.addView(iv);

            return iv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    // dip-->px
    public int dip2px(int dip) {
        // px / dip = density
        float density = getResources().getDisplayMetrics().density;
        int px = (int) (dip * density + .5f);
        return px;
    }

    @SuppressLint("NewApi")
    class DepthPageTransformer implements ViewPager.PageTransformer
    {
        private static final float MIN_SCALE = 0.75f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 0) { // [-1,0]
                // Use the default slide transition when moving to the left page
                view.setAlpha(1);
                view.setTranslationX(0);
                view.setScaleX(1);
                view.setScaleY(1);

            } else if (position <= 1) { // (0,1]
                // Fade the page out.
                view.setAlpha(1 - position);

                // Counteract the default slide transition
                view.setTranslationX(pageWidth * -position);

                // Scale the page down (between MIN_SCALE and 1)
                float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }

}