package com.ashlikun.calendarview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * 作者　　: 李坤
 * 创建时间: 2018/8/12 0012 13:13
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：年份+月份选择布局
 * ViewPager + RecyclerView
 */

public final class YearSelectLayout extends ViewPager {
    private int mYearCount;
    private CalendarViewDelegate mDelegate;
    private YearRecyclerView.OnMonthSelectedListener mListener;

    public YearSelectLayout(Context context) {
        this(context, null);
    }

    public YearSelectLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    void setup(CalendarViewDelegate delegate) {
        this.mDelegate = delegate;
        this.mYearCount = mDelegate.getMaxYear() - mDelegate.getMinYear() + 1;
        setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return mYearCount;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                YearRecyclerView view = new YearRecyclerView(getContext());
                container.addView(view);
                view.setup(mDelegate);
                view.setOnMonthSelectedListener(mListener);
                view.init(position + mDelegate.getMinYear());
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                if (object instanceof YearRecyclerView) {
                    container.removeView((YearRecyclerView) object);
                }
            }
        });
        setCurrentItem(mDelegate.getCurrentDay().getYear() - mDelegate.getMinYear());
    }

    void notifyDataSetChanged() {
        this.mYearCount = mDelegate.getMaxYear() - mDelegate.getMinYear() + 1;
        getAdapter().notifyDataSetChanged();
    }

    void scrollToYear(int year, boolean smoothScroll) {
        setCurrentItem(year - mDelegate.getMinYear(), smoothScroll);
    }


    /**
     * 更新界面
     */
    void update() {
        for (int i = 0; i < getChildCount(); i++) {
            YearRecyclerView view = (YearRecyclerView) getChildAt(i);
            view.getAdapter().notifyDataSetChanged();
        }
    }


    /**
     * 更新周起始
     */
    void updateWeekStart() {
        for (int i = 0; i < getChildCount(); i++) {
            YearRecyclerView view = (YearRecyclerView) getChildAt(i);
            view.updateWeekStart();
            view.getAdapter().notifyDataSetChanged();
        }
    }

    public void setOnMonthSelectedListener(YearRecyclerView.OnMonthSelectedListener listener) {
        this.mListener = listener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(getHeight(getContext(), this), MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 计算相对高度
     *
     * @param context context
     * @param view    view
     * @return 月视图选择器最适合的高度
     */
    private static int getHeight(Context context, View view) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        assert manager != null;
        Display display = manager.getDefaultDisplay();
        int h = display.getHeight();
        int[] location = new int[2];
        view.getLocationInWindow(location);
        view.getLocationOnScreen(location);
        return h - location[1];
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return mDelegate.isYearViewScrollable() && super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mDelegate.isYearViewScrollable() && super.onInterceptTouchEvent(ev);
    }
}
