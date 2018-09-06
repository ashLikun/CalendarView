package com.ashlikun.calendarview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 作者　　: 李坤
 * 创建时间: 2018/8/12 0012 13:06
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：Google规范化的属性委托,
 * 这里基本是没有逻辑的，代码量多，但是不影响阅读性
 */

final class CalendarViewDelegate {

    /**
     * 周起始：周日
     */
    static final int WEEK_START_WITH_SUN = 1;

    /**
     * 周起始：周一
     */
    static final int WEEK_START_WITH_MON = 2;

    /**
     * 周起始：周六
     */
    static final int WEEK_START_WITH_SAT = 7;

    /**
     * 事件标记类型，LIST
     */
    static final int SCHEME_TYPE_LIST = 1;

    /**
     * 事件标记类型，MAP
     */
    static final int SCHEME_TYPE_MAP = 2;

    /**
     * 全部显示
     */
    static final int MODE_ALL_MONTH = 0;
    /**
     * 仅显示当前月份
     */
    static final int MODE_ONLY_CURRENT_MONTH = 1;

    /**
     * 自适应显示，不会多出一行，但是会自动填充
     */
    static final int MODE_FIT_MONTH = 2;

    /**
     * 月份显示模式
     */
    private int mMonthViewShowMode;


    /**
     * 周起始
     */
    private int mWeekStart;

    /**
     * 默认选择模式
     */
    static final int SELECT_MODE_DEFAULT = 0;

    /**
     * 单选模式
     */
    static final int SELECT_MODE_SINGLE = 1;
    /**
     * 多选模式
     */
    static final int SELECT_MODE_MULTIPLE = 2;
    /**
     * 范围选择模式
     */
    static final int SELECT_MODE_RANGE = 3;
    /**
     * 选择模式
     */
    private int mSelectMode;


    /**
     * 支持转换的最小农历年份
     */
    static final int MIN_YEAR = 1900;
    /**
     * 支持转换的最大农历年份
     */
    private static final int MAX_YEAR = 2099;

    /**
     * 各种字体颜色，看名字知道对应的地方
     */
    private int mCurDayTextColor,
            mCurDayLunarTextColor,
            mWeekTextColor,
            mSchemeTextColor,
            mSchemeLunarTextColor,
            mOtherMonthTextColor,
            mCurrentMonthTextColor,
            mSelectedTextColor,
            mSelectedLunarTextColor,
            mCurMonthLunarTextColor,
            mOtherMonthLunarTextColor;

    private boolean preventLongPressedSelected;

    /**
     * 日历内部左右padding
     */
    private int mCalendarPadding;

    /**
     * 年视图字体大小
     */
    private int mYearViewMonthTextSize,
            mYearViewDayTextSize;

    /**
     * 年视图字体和标记颜色
     */
    private int mYearViewMonthTextColor,
            mYearViewDayTextColor,
            mYearViewSchemeTextColor;

    /**
     * 星期栏的背景、线的背景、年份背景
     */
    private int mWeekLineBackground,
            mYearViewBackground,
            mWeekBackground;

    /**
     * 星期栏字体大小
     */
    private int mWeekTextSize;

    /**
     * 标记的主题色和选中的主题色
     */
    private int mSchemeThemeColor, mSelectedThemeColor;


    /**
     * 自定义的日历路径
     */
    private String mMonthViewClass;

    /**
     * 自定义周视图路径
     */
    private String mWeekViewClass;

    /**
     * 自定义周栏路径
     */
    private String mWeekBarClass;


    /**
     * 年月视图是否打开
     */
    boolean isShowYearSelectedLayout;

    /**
     * 标记文本
     */
    private String mSchemeText;

    /**
     * 最小年份和最大年份
     */
    private int mMinYear, mMaxYear;

    /**
     * 最小年份和最大年份对应最小月份和最大月份
     * when you want set 2015-07 to 2017-08
     */
    private int mMinYearMonth, mMaxYearMonth;

    /**
     * 日期和农历文本大小
     */
    private int mDayTextSize, mLunarTextSize;

    /**
     * 日历卡的项高度
     */
    private int mCalendarItemHeight;

    /**
     * 星期栏的高度
     */
    private int mWeekBarHeight;

    /**
     * 今天的日子
     */
    private Calendar mCurrentDate;


    private boolean mMonthViewScrollable,
            mWeekViewScrollable,
            mYearViewScrollable;

    /**
     * 当前月份和周视图的item位置
     */
    @SuppressWarnings("all")
    int mCurrentMonthViewItem, mCurrentWeekViewItem;


    private int mSchemeType;

    /**
     * 标记的日期
     */
    List<Calendar> mSchemeDate;

    /**
     * 标记的日期,数量巨大，请使用这个
     */
    Map<String, Calendar> mSchemeDatesMap;


    /**
     * 日期被选中监听
     */
    CalendarView.OnDateSelectedListener mDateSelectedListener;
    CalendarView.OnDateRangeSelectedListener mDateRangeSelectedListener;
    CalendarView.OnDateMultipleSelectedListener mDateMultipleSelectedListener;

    /**
     * 外部日期长按事件
     */
    CalendarView.OnDateLongClickListener mDateLongClickListener;

    /**
     * 内部日期切换监听，用于内部更新计算
     */
    CalendarView.OnInnerDateSelectedListener mInnerListener;

    /**
     * 快速年份切换
     */
    CalendarView.OnYearChangeListener mYearChangeListener;


    /**
     * 月份切换事件
     */
    CalendarView.OnMonthChangeListener mMonthChangeListener;

    /**
     * 视图改变事件
     */
    CalendarView.OnViewChangeListener mViewChangeListener;

    /**
     * 保存选中的日期
     */
    Calendar mSelectedCalendar;
    List<Calendar> mSelectedCalendars;

    /**
     * 保存标记位置
     */
    Calendar mIndexCalendar;
    /**
     * 最多选择个数
     */
    private int mMultipleSelectMax = 5;

    CalendarViewDelegate(Context context, @Nullable AttributeSet attrs) {
        mSelectedCalendars = new ArrayList<>();
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CalendarView);

        LunarCalendar.init(context);

        mCalendarPadding = (int) array.getDimension(R.styleable.CalendarView_calendar_padding, 0);
        mSchemeTextColor = array.getColor(R.styleable.CalendarView_scheme_text_color, 0xFFFFFFFF);
        mSchemeLunarTextColor = array.getColor(R.styleable.CalendarView_scheme_lunar_text_color, 0xFFe1e1e1);
        mSchemeThemeColor = array.getColor(R.styleable.CalendarView_scheme_theme_color, 0x50CFCFCF);
        mMonthViewClass = array.getString(R.styleable.CalendarView_month_view);

        mWeekViewClass = array.getString(R.styleable.CalendarView_week_view);
        mWeekBarClass = array.getString(R.styleable.CalendarView_week_bar_view);
        mWeekTextSize = array.getDimensionPixelSize(R.styleable.CalendarView_week_text_size, CalendarUtil.dipToPx(context, 12));
        mWeekBarHeight = (int) array.getDimension(R.styleable.CalendarView_week_bar_height, CalendarUtil.dipToPx(context, 40));

        mSchemeText = array.getString(R.styleable.CalendarView_scheme_text);
        if (TextUtils.isEmpty(mSchemeText)) {
            mSchemeText = "记";
        }

        mMonthViewScrollable = array.getBoolean(R.styleable.CalendarView_month_view_scrollable, true);
        mWeekViewScrollable = array.getBoolean(R.styleable.CalendarView_week_view_scrollable, true);
        mYearViewScrollable = array.getBoolean(R.styleable.CalendarView_year_view_scrollable, true);


        mMonthViewShowMode = array.getInt(R.styleable.CalendarView_month_view_show_mode, MODE_ALL_MONTH);
        mWeekStart = array.getInt(R.styleable.CalendarView_week_start_with, WEEK_START_WITH_SUN);
        mSelectMode = array.getInt(R.styleable.CalendarView_select_mode, SELECT_MODE_DEFAULT);
        mMultipleSelectMax = array.getInt(R.styleable.CalendarView_select_multiple_max, mMultipleSelectMax);

        mWeekBackground = array.getColor(R.styleable.CalendarView_week_background, Color.WHITE);
        mWeekLineBackground = array.getColor(R.styleable.CalendarView_week_line_background, Color.TRANSPARENT);
        mYearViewBackground = array.getColor(R.styleable.CalendarView_year_view_background, Color.WHITE);
        mWeekTextColor = array.getColor(R.styleable.CalendarView_week_text_color, 0xFF333333);

        mCurDayTextColor = array.getColor(R.styleable.CalendarView_current_day_text_color, Color.RED);
        mCurDayLunarTextColor = array.getColor(R.styleable.CalendarView_current_day_lunar_text_color, Color.RED);

        mSelectedThemeColor = array.getColor(R.styleable.CalendarView_selected_theme_color, 0x50CFCFCF);
        mSelectedTextColor = array.getColor(R.styleable.CalendarView_selected_text_color, 0xFF111111);

        mSelectedLunarTextColor = array.getColor(R.styleable.CalendarView_selected_lunar_text_color, 0xFF111111);
        mCurrentMonthTextColor = array.getColor(R.styleable.CalendarView_current_month_text_color, 0xFF111111);
        mOtherMonthTextColor = array.getColor(R.styleable.CalendarView_other_month_text_color, 0xFFe1e1e1);

        mCurMonthLunarTextColor = array.getColor(R.styleable.CalendarView_current_month_lunar_text_color, 0xffe1e1e1);
        mOtherMonthLunarTextColor = array.getColor(R.styleable.CalendarView_other_month_lunar_text_color, 0xffe1e1e1);
        mMinYear = array.getInt(R.styleable.CalendarView_min_year, 1971);
        mMaxYear = array.getInt(R.styleable.CalendarView_max_year, 2055);
        mMinYearMonth = array.getInt(R.styleable.CalendarView_min_year_month, 1);
        mMaxYearMonth = array.getInt(R.styleable.CalendarView_max_year_month, 12);

        mDayTextSize = array.getDimensionPixelSize(R.styleable.CalendarView_day_text_size, CalendarUtil.dipToPx(context, 16));
        mLunarTextSize = array.getDimensionPixelSize(R.styleable.CalendarView_lunar_text_size, CalendarUtil.dipToPx(context, 10));
        mCalendarItemHeight = (int) array.getDimension(R.styleable.CalendarView_calendar_height, CalendarUtil.dipToPx(context, 56));

        //年视图相关
        mYearViewMonthTextSize = array.getDimensionPixelSize(R.styleable.CalendarView_year_view_month_text_size, CalendarUtil.dipToPx(context, 18));
        mYearViewDayTextSize = array.getDimensionPixelSize(R.styleable.CalendarView_year_view_day_text_size, CalendarUtil.dipToPx(context, 8));
        mYearViewMonthTextColor = array.getColor(R.styleable.CalendarView_year_view_month_text_color, 0xFF111111);
        mYearViewDayTextColor = array.getColor(R.styleable.CalendarView_year_view_day_text_color, 0xFF111111);
        mYearViewSchemeTextColor = array.getColor(R.styleable.CalendarView_year_view_scheme_color, mSchemeThemeColor);

        if (mMinYear <= MIN_YEAR) {
            mMinYear = 1971;
        }
        if (mMaxYear >= MAX_YEAR) {
            mMaxYear = 2055;
        }
        array.recycle();
        init();
    }

    private void init() {
        mCurrentDate = new Calendar();
        Date d = new Date();
        mCurrentDate.setYear(CalendarUtil.getDate("yyyy", d));
        mCurrentDate.setMonth(CalendarUtil.getDate("MM", d));
        mCurrentDate.setDay(CalendarUtil.getDate("dd", d));
        mCurrentDate.setCurrentDay(true);
        LunarCalendar.setupLunarCalendar(mCurrentDate);
        setRange(mMinYear, mMinYearMonth, mMaxYear, mMaxYearMonth);
    }


    void setRange(int minYear, int minYearMonth,
                  int maxYear, int maxYearMonth) {
        this.mMinYear = minYear;
        this.mMinYearMonth = minYearMonth;
        this.mMaxYear = maxYear;
        this.mMaxYearMonth = maxYearMonth;
        if (this.mMaxYear < mCurrentDate.getYear()) {
            this.mMaxYear = mCurrentDate.getYear();
        }
        int y = mCurrentDate.getYear() - this.mMinYear;
        mCurrentMonthViewItem = 12 * y + mCurrentDate.getMonth() - this.mMinYearMonth;
        mCurrentWeekViewItem = CalendarUtil.getWeekFromCalendarBetweenYearAndYear(mCurrentDate, mMinYear, mMinYearMonth, mWeekStart);
    }

    String getSchemeText() {
        return mSchemeText;
    }

    int getCurDayTextColor() {
        return mCurDayTextColor;
    }

    int getCurDayLunarTextColor() {
        return mCurDayLunarTextColor;
    }

    int getWeekTextColor() {
        return mWeekTextColor;
    }

    int getSchemeTextColor() {
        return mSchemeTextColor;
    }

    int getSchemeLunarTextColor() {
        return mSchemeLunarTextColor;
    }

    int getOtherMonthTextColor() {
        return mOtherMonthTextColor;
    }

    int getCurrentMonthTextColor() {
        return mCurrentMonthTextColor;
    }

    int getSelectedTextColor() {
        return mSelectedTextColor;
    }

    int getSelectedLunarTextColor() {
        return mSelectedLunarTextColor;
    }

    int getCurrentMonthLunarTextColor() {
        return mCurMonthLunarTextColor;
    }

    int getOtherMonthLunarTextColor() {
        return mOtherMonthLunarTextColor;
    }

    int getSchemeThemeColor() {
        return mSchemeThemeColor;
    }

    int getSelectedThemeColor() {
        return mSelectedThemeColor;
    }

    int getWeekBackground() {
        return mWeekBackground;
    }

    int getYearViewBackground() {
        return mYearViewBackground;
    }

    int getWeekLineBackground() {
        return mWeekLineBackground;
    }


    String getMonthViewClass() {
        return mMonthViewClass;
    }

    String getWeekViewClass() {
        return mWeekViewClass;
    }

    String getWeekBarClass() {
        return mWeekBarClass;
    }

    int getWeekBarHeight() {
        return mWeekBarHeight;
    }

    int getMinYear() {
        return mMinYear;
    }

    int getMaxYear() {
        return mMaxYear;
    }

    int getDayTextSize() {
        return mDayTextSize;
    }

    int getLunarTextSize() {
        return mLunarTextSize;
    }

    int getCalendarItemHeight() {
        return mCalendarItemHeight;
    }

    int getMinYearMonth() {
        return mMinYearMonth;
    }

    int getMaxYearMonth() {
        return mMaxYearMonth;
    }


    int getYearViewMonthTextSize() {
        return mYearViewMonthTextSize;
    }

    int getYearViewMonthTextColor() {
        return mYearViewMonthTextColor;
    }

    int getYearViewDayTextColor() {
        return mYearViewDayTextColor;
    }

    int getYearViewDayTextSize() {
        return mYearViewDayTextSize;
    }

    int getYearViewSchemeTextColor() {
        return mYearViewSchemeTextColor;
    }

    int getMonthViewShowMode() {
        return mMonthViewShowMode;
    }

    void setMonthViewShowMode(int monthViewShowMode) {
        this.mMonthViewShowMode = monthViewShowMode;
    }

    void setTextColor(int curDayTextColor, int curMonthTextColor, int otherMonthTextColor, int curMonthLunarTextColor, int otherMonthLunarTextColor) {
        mCurDayTextColor = curDayTextColor;
        mOtherMonthTextColor = otherMonthTextColor;
        mCurrentMonthTextColor = curMonthTextColor;
        mCurMonthLunarTextColor = curMonthLunarTextColor;
        mOtherMonthLunarTextColor = otherMonthLunarTextColor;
    }

    void setSchemeColor(int schemeColor, int schemeTextColor, int schemeLunarTextColor) {
        this.mSchemeThemeColor = schemeColor;
        this.mSchemeTextColor = schemeTextColor;
        this.mSchemeLunarTextColor = schemeLunarTextColor;
    }

    void setYearViewTextColor(int yearViewMonthTextColor, int yearViewDayTextColor, int yarViewSchemeTextColor) {
        this.mYearViewMonthTextColor = yearViewMonthTextColor;
        this.mYearViewDayTextColor = yearViewDayTextColor;
        this.mYearViewSchemeTextColor = yarViewSchemeTextColor;
    }

    void setSelectColor(int selectedColor, int selectedTextColor, int selectedLunarTextColor) {
        this.mSelectedThemeColor = selectedColor;
        this.mSelectedTextColor = selectedTextColor;
        this.mSelectedLunarTextColor = selectedLunarTextColor;
    }

    void setThemeColor(int selectedThemeColor, int schemeColor) {
        this.mSelectedThemeColor = selectedThemeColor;
        this.mSchemeThemeColor = schemeColor;
    }

    boolean isMonthViewScrollable() {
        return mMonthViewScrollable;
    }

    boolean isWeekViewScrollable() {
        return mWeekViewScrollable;
    }

    boolean isYearViewScrollable() {
        return mYearViewScrollable;
    }

    int getWeekStart() {
        return mWeekStart;
    }

    void setWeekStart(int mWeekStart) {
        this.mWeekStart = mWeekStart;
    }

    int getWeekTextSize() {
        return mWeekTextSize;
    }

    /**
     * 选择模式
     *
     * @return 选择模式
     */
    int getSelectMode() {
        return mSelectMode;
    }

    /**
     * 设置选择模式
     *
     * @param mSelectMode mSelectMode
     */
    void setSelectMode(int mSelectMode) {
        this.mSelectMode = mSelectMode;
    }

    Calendar getCurrentDay() {
        return mCurrentDate;
    }

    void updateCurrentDay() {
        Date d = new Date();
        mCurrentDate.setYear(CalendarUtil.getDate("yyyy", d));
        mCurrentDate.setMonth(CalendarUtil.getDate("MM", d));
        mCurrentDate.setDay(CalendarUtil.getDate("dd", d));
        LunarCalendar.setupLunarCalendar(mCurrentDate);
    }

    int getCalendarPadding() {
        return mCalendarPadding;
    }

    int getSchemeType() {
        return mSchemeType;
    }

    void setSchemeType(int schemeType) {
        this.mSchemeType = schemeType;
    }

    void setPreventLongPressedSelected(boolean preventLongPressedSelected) {
        this.preventLongPressedSelected = preventLongPressedSelected;
    }

    void setMonthViewClass(String monthViewClass) {
        this.mMonthViewClass = monthViewClass;
    }

    void setWeekBarClass(String weekBarClass) {
        this.mWeekBarClass = weekBarClass;
    }

    void setWeekViewClass(String weekViewClass) {
        this.mWeekViewClass = weekViewClass;
    }

    boolean isPreventLongPressedSelected() {
        return preventLongPressedSelected;
    }

    void clearSelectedScheme() {
        mSelectedCalendar.setScheme(null);
        mSelectedCalendar.setSchemeColor(0);
        mSelectedCalendar.setSchemes(null);
    }

    Calendar createCurrentDate() {
        Calendar calendar = new Calendar();
        calendar.setYear(mCurrentDate.getYear());
        calendar.setWeek(mCurrentDate.getWeek());
        calendar.setMonth(mCurrentDate.getMonth());
        calendar.setDay(mCurrentDate.getDay());
        LunarCalendar.setupLunarCalendar(calendar);
        return calendar;
    }

    /**
     * 这个数据是否是选中的
     *
     * @param calendar
     * @return
     */
    public boolean isSelect(Calendar calendar) {
        switch (mSelectMode) {
            case SELECT_MODE_DEFAULT:
                return mSelectedCalendar.equals(calendar);
            case SELECT_MODE_SINGLE:
                return mSelectedCalendars.contains(calendar);
            case SELECT_MODE_MULTIPLE:
                return mSelectedCalendars.contains(calendar);
            case SELECT_MODE_RANGE:
                if (mSelectedCalendars.size() >= 2) {
                    return mSelectedCalendars.get(0).compareTo(calendar) <= 0 && mSelectedCalendars.get(1).compareTo(calendar) >= 0;
                } else {
                    return mSelectedCalendars.contains(calendar);
                }
        }
        return false;
    }

    /**
     * 是否是默认选择模式
     *
     * @return
     */
    public boolean isSelectModeDefault() {
        return mSelectMode == SELECT_MODE_DEFAULT;
    }

    /**
     * 是否是单一选择模式
     *
     * @return
     */
    public boolean isSelectModeSingle() {
        return mSelectMode == SELECT_MODE_SINGLE;
    }

    /**
     * 是否是多选模式
     *
     * @return
     */
    public boolean isSelectModeMultiple() {
        return mSelectMode == SELECT_MODE_MULTIPLE;
    }

    /**
     * 是否是范围选择模式
     *
     * @return
     */
    public boolean isSelectModeRange() {
        return mSelectMode == SELECT_MODE_RANGE;
    }


    /**
     * 获取选中的数据，多个会选第一个
     *
     * @return
     */
    public Calendar getSelectOne() {
        switch (mSelectMode) {
            case SELECT_MODE_DEFAULT:
                return mSelectedCalendar;
            case SELECT_MODE_SINGLE:
                return mSelectedCalendars.size() > 0 ? mSelectedCalendars.get(0) : null;
            case SELECT_MODE_MULTIPLE:
                return mSelectedCalendars.size() > 0 ? mSelectedCalendars.get(0) : null;
            case SELECT_MODE_RANGE:
                return mSelectedCalendars.size() > 0 ? mSelectedCalendars.get(0) : null;
        }
        return null;
    }

    /**
     * 获取范围选择的开始
     *
     * @return
     */
    public Calendar getRangeSelectStart() {
        return mSelectedCalendars.size() > 0 ? mSelectedCalendars.get(0) : null;
    }

    /**
     * 获取范围选择的结束
     *
     * @return
     */
    public Calendar getRangeSelectEnd() {
        return mSelectedCalendars.size() > 1 ? mSelectedCalendars.get(1) : null;
    }

    void cleanSelectCalendrs() {
        mSelectedCalendars.clear();
    }

    /**
     * 记录已经选择的日期
     *
     * @param selectCalendar
     */
    public void setSelectCalendar(Calendar selectCalendar, boolean isSelect) {
        if (isSelectModeDefault()) {
            //默认模式,单选
            mSelectedCalendar = selectCalendar;
        } else if (isSelectModeSingle()) {
            if (isSelect) {
                mSelectedCalendars.clear();
                mSelectedCalendars.add(selectCalendar);
            } else {
                mSelectedCalendars.remove(selectCalendar);
            }
        } else if (isSelectModeMultiple()) {
            //多选
            if (isSelect) {
                if (mSelectedCalendars.size() >= mMultipleSelectMax) {
                    //选择到上限,移动数据
                    for (int i = 0; i < mSelectedCalendars.size() - 1; i++) {
                        mSelectedCalendars.set(i, mSelectedCalendars.get(i + 1));
                    }
                    mSelectedCalendars.set(mSelectedCalendars.size() - 1, selectCalendar);
                } else {
                    mSelectedCalendars.add(selectCalendar);
                }
            } else {
                //反选
                if (mSelectedCalendars.contains(selectCalendar)) {
                    mSelectedCalendars.remove(selectCalendar);
                }
            }
        } else if (isSelectModeRange()) {
            //范围选择
            if (isSelect) {
                mSelectedCalendars.add(selectCalendar);
                if (mSelectedCalendars.size() > 2) {
                    //大于2个，就从新选择
                    mSelectedCalendars.clear();
                    mSelectedCalendars.add(selectCalendar);
                } else if (mSelectedCalendars.size() == 2) {
                    //1>2，交换数据
                    if (mSelectedCalendars.get(0).compareTo(mSelectedCalendars.get(1)) >= 1) {
                        Calendar temp = mSelectedCalendars.get(0);
                        mSelectedCalendars.set(0, mSelectedCalendars.get(1));
                        mSelectedCalendars.set(1, temp);
                    }
                }
            } else {
                if (mSelectedCalendars.contains(selectCalendar)) {
                    mSelectedCalendars.remove(selectCalendar);
                }
            }
        }
    }

    /**
     * 分发选择事件
     *
     * @param isClick 是否是点击
     */
    public void dispatchSelectListener(boolean isClick) {
        switch (mSelectMode) {
            case SELECT_MODE_DEFAULT:
                if (mDateSelectedListener != null) {
                    mDateSelectedListener.onDateSelected(mSelectedCalendar, isClick);
                }
                break;
            case SELECT_MODE_SINGLE:
                if (mDateSelectedListener != null) {
                    mDateSelectedListener.onDateSelected(getSelectOne(), isClick);
                }
                break;
            case SELECT_MODE_MULTIPLE:
                if (mDateMultipleSelectedListener != null) {
                    mDateMultipleSelectedListener.onDateMultipleSelected(mSelectedCalendars, isClick);
                }
                break;
            case SELECT_MODE_RANGE:
                if (mDateRangeSelectedListener != null) {
                    mDateRangeSelectedListener.onDateRangeSelected(getRangeSelectStart(), getRangeSelectEnd(), isClick);
                }
                break;
        }
    }


}
