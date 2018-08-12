package com.ashlikun.calendarview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;

/**
 * 作者　　: 李坤
 * 创建时间: 2018/8/12 0012 13:13
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：周视图滑动ViewPager，需要动态固定高度
 * 周视图是连续不断的视图，因此不能简单的得出每年都有52+1周，这样会计算重叠的部分
 * WeekViewPager需要和CalendarView关联:
 */
public final class WeekViewPager extends ViewPager {
    private boolean isUpdateWeekView;
    private int mWeekCount;
    private CalendarViewDelegate mDelegate;

    /**
     * 日历布局，需要在日历下方放自己的布局
     */
    CalendarLayout mParentLayout;

    /**
     * 是否使用滚动到某一天
     */
    private boolean isUsingScrollToCalendar = false;

    public WeekViewPager(Context context) {
        this(context, null);
    }

    public WeekViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    void setup(CalendarViewDelegate delegate) {
        this.mDelegate = delegate;
        init();
    }

    private void init() {
        mWeekCount = CalendarUtil.getWeekCountBetweenYearAndYear(mDelegate.getMinYear(),
                mDelegate.getMinYearMonth(),
                mDelegate.getMaxYear(),
                mDelegate.getMaxYearMonth(),
                mDelegate.getWeekStart());
        setAdapter(new WeekViewPagerAdapter());
        addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //默认的显示星期四，周视图切换就显示星期4
                if (getVisibility() != VISIBLE) {
                    isUsingScrollToCalendar = false;
                    return;
                }
                WeekView view = (WeekView) findViewWithTag(position);
                if (view != null) {
                    view.performClickCalendar(mDelegate.getSelectMode() == CalendarViewDelegate.SELECT_MODE_SINGLE ?
                            mDelegate.mIndexCalendar : mDelegate.mSelectedCalendar, !isUsingScrollToCalendar);
                }
                isUsingScrollToCalendar = false;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    void notifyDataSetChanged() {
        mWeekCount = CalendarUtil.getWeekCountBetweenYearAndYear(mDelegate.getMinYear(), mDelegate.getMinYearMonth(),
                mDelegate.getMaxYear(), mDelegate.getMaxYearMonth(), mDelegate.getWeekStart());
        getAdapter().notifyDataSetChanged();
    }

    void updateWeekViewClass() {
        isUpdateWeekView = true;
        getAdapter().notifyDataSetChanged();
        isUpdateWeekView = false;
    }

    /**
     * 滚动到指定日期
     *
     * @param year  年
     * @param month 月
     * @param day   日
     */
    void scrollToCalendar(int year, int month, int day, boolean smoothScroll) {
        isUsingScrollToCalendar = true;
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setCurrentDay(calendar.equals(mDelegate.getCurrentDay()));
        LunarCalendar.setupLunarCalendar(calendar);
        mDelegate.mIndexCalendar = calendar;
        mDelegate.mSelectedCalendar = calendar;
        updateSelected(calendar, smoothScroll);
        if (mDelegate.mInnerListener != null) {
            mDelegate.mInnerListener.onWeekDateSelected(calendar, false);
        }
        if (mDelegate.mDateSelectedListener != null) {
            mDelegate.mDateSelectedListener.onDateSelected(calendar, false);
        }
        int i = CalendarUtil.getWeekFromDayInMonth(calendar, mDelegate.getWeekStart());
        mParentLayout.updateSelectWeek(i);
    }

    /**
     * 滚动到当前
     */
    void scrollToCurrent(boolean smoothScroll) {
        isUsingScrollToCalendar = true;
        int position = CalendarUtil.getWeekFromCalendarBetweenYearAndYear(mDelegate.getCurrentDay(),
                mDelegate.getMinYear(),
                mDelegate.getMinYearMonth(),
                mDelegate.getWeekStart()) - 1;
        int curItem = getCurrentItem();
        if (curItem == position) {
            isUsingScrollToCalendar = false;
        }
        setCurrentItem(position, smoothScroll);
        WeekView view = (WeekView) findViewWithTag(position);
        if (view != null) {
            view.performClickCalendar(mDelegate.getCurrentDay(), false);
            view.setSelectedCalendar(mDelegate.getCurrentDay());
            view.invalidate();
        }
        if (mDelegate.mDateSelectedListener != null && getVisibility() == VISIBLE) {
            mDelegate.mDateSelectedListener.onDateSelected(mDelegate.createCurrentDate(), false);
        }
        if (getVisibility() == VISIBLE) {
            mDelegate.mInnerListener.onWeekDateSelected(mDelegate.getCurrentDay(), false);
        }
        int i = CalendarUtil.getWeekFromDayInMonth(mDelegate.getCurrentDay(), mDelegate.getWeekStart());
        mParentLayout.updateSelectWeek(i);
    }

    /**
     * 更新任意一个选择的日期
     */
    void updateSelected(Calendar calendar, boolean smoothScroll) {
        int position = CalendarUtil.getWeekFromCalendarBetweenYearAndYear(calendar,
                mDelegate.getMinYear(),
                mDelegate.getMinYearMonth(),
                mDelegate.getWeekStart()) - 1;
        int curItem = getCurrentItem();
        if (curItem == position) {
            isUsingScrollToCalendar = false;
        }
        setCurrentItem(position, smoothScroll);
        WeekView view = (WeekView) findViewWithTag(position);
        if (view != null) {
            view.setSelectedCalendar(calendar);
            view.invalidate();
        }
    }


    /**
     * 更新单选模式
     */
    void updateSingleSelect() {
        if (mDelegate.getSelectMode() == CalendarViewDelegate.SELECT_MODE_DEFAULT) {
            return;
        }
        for (int i = 0; i < getChildCount(); i++) {
            WeekView view = (WeekView) getChildAt(i);
            view.updateSingleSelect();
        }
    }

    /**
     * 更新为默认选择模式
     */
    void updateDefaultSelect() {
        WeekView view = (WeekView) findViewWithTag(getCurrentItem());
        if (view != null) {
            view.setSelectedCalendar(mDelegate.mSelectedCalendar);
            view.invalidate();
        }
    }

    /**
     * 更新选择效果
     */
    void updateSelected() {
        for (int i = 0; i < getChildCount(); i++) {
            WeekView view = (WeekView) getChildAt(i);
            view.setSelectedCalendar(mDelegate.mSelectedCalendar);
            view.invalidate();
        }
    }


    /**
     * 更新标记日期
     */
    void updateScheme() {
        for (int i = 0; i < getChildCount(); i++) {
            WeekView view = (WeekView) getChildAt(i);
            view.update();
        }
    }

    /**
     * 更新当前日期，夜间过度的时候调用这个函数，一般不需要调用
     */
    void updateCurrentDate() {
        for (int i = 0; i < getChildCount(); i++) {
            WeekView view = (WeekView) getChildAt(i);
            view.updateCurrentDate();
        }
    }

    /**
     * 更新显示模式
     */
    void updateShowMode() {
        for (int i = 0; i < getChildCount(); i++) {
            WeekView view = (WeekView) getChildAt(i);
            view.updateShowMode();
        }
    }

    /**
     * 更新周起始
     */
    void updateWeekStart() {
        int count = getAdapter().getCount();
        mWeekCount = CalendarUtil.getWeekCountBetweenYearAndYear(mDelegate.getMinYear(),
                mDelegate.getMinYearMonth(),
                mDelegate.getMaxYear(),
                mDelegate.getMaxYearMonth(),
                mDelegate.getWeekStart());
        /*
         * 如果count发生变化，意味着数据源变化，则必须先调用notifyDataSetChanged()，
         * 否则会抛出异常
         */
        if (count != mWeekCount) {
            getAdapter().notifyDataSetChanged();
        }
        for (int i = 0; i < getChildCount(); i++) {
            WeekView view = (WeekView) getChildAt(i);
            view.updateWeekStart();
        }
        updateSelected(mDelegate.mSelectedCalendar, false);
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return mDelegate.isWeekViewScrollable() && super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mDelegate.isWeekViewScrollable() && super.onInterceptTouchEvent(ev);
    }

    /**
     * 周视图的高度应该与日历项的高度一致
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(mDelegate.getCalendarItemHeight(), MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 周视图切换
     */
    private class WeekViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mWeekCount;
        }

        @Override
        public int getItemPosition(Object object) {
            return isUpdateWeekView ? POSITION_NONE : super.getItemPosition(object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Calendar calendar = CalendarUtil.getFirstCalendarFromWeekCount(mDelegate.getMinYear(),
                    mDelegate.getMinYearMonth(),
                    position + 1,
                    mDelegate.getWeekStart());
            WeekView view;
            if (TextUtils.isEmpty(mDelegate.getWeekViewClass())) {
                view = new DefaultWeekView(getContext());
            } else {
                try {
                    Class<?> cls = Class.forName(mDelegate.getWeekViewClass());
                    Constructor constructor = cls.getConstructor(Context.class);
                    view = (WeekView) constructor.newInstance(getContext());
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
            view.mParentLayout = mParentLayout;
            view.setup(mDelegate);
            view.setup(calendar);
            view.setTag(position);
            view.setSelectedCalendar(mDelegate.mSelectedCalendar);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            BaseView view = (BaseView) object;
            view.onDestroy();
            container.removeView(view);
        }

    }
}
