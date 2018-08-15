package com.ashlikun.calendarview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;

/**
 * 作者　　: 李坤
 * 创建时间: 2018/8/12 0012 13:04
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：基本的日历View，派生出{@link MonthView} 和 {@link WeekView}
 */
public abstract class BaseView extends View implements View.OnClickListener, View.OnLongClickListener {

    CalendarViewDelegate mDelegate;

    /**
     * 当前月份日期的笔
     */
    protected Paint mCurMonthTextPaint = new Paint();

    /**
     * 其它月份日期颜色
     */
    protected Paint mOtherMonthTextPaint = new Paint();

    /**
     * 当前月份农历文本颜色
     */
    protected Paint mCurMonthLunarTextPaint = new Paint();


    /**
     * 当前月份农历文本颜色
     */
    protected Paint mSelectedLunarTextPaint = new Paint();

    /**
     * 其它月份农历文本颜色
     */
    protected Paint mOtherMonthLunarTextPaint = new Paint();

    /**
     * 其它月份农历文本颜色
     */
    protected Paint mSchemeLunarTextPaint = new Paint();

    /**
     * 标记的日期背景颜色画笔
     */
    protected Paint mSchemePaint = new Paint();

    /**
     * 被选择的日期背景色
     */
    protected Paint mSelectedPaint = new Paint();

    /**
     * 标记的文本画笔
     */
    protected Paint mSchemeTextPaint = new Paint();

    /**
     * 选中的文本画笔
     */
    protected Paint mSelectTextPaint = new Paint();

    /**
     * 当前日期文本颜色画笔
     */
    protected Paint mCurDayTextPaint = new Paint();

    /**
     * 当前日期文本颜色画笔
     */
    protected Paint mCurDayLunarTextPaint = new Paint();

    /**
     * 日历布局，需要在日历下方放自己的布局
     */
    CalendarLayout mParentLayout;

    /**
     * 日历项
     */
    List<Calendar> mItems;

    /**
     * 每一项的高度
     */
    protected int mItemHeight;

    /**
     * 每一项的宽度
     */
    protected int mItemWidth;

    /**
     * Text的基线
     */
    protected float mTextBaseLine;

    /**
     * 点击的x、y坐标
     */
    float mX, mY;

    /**
     * 是否点击
     */
    boolean isClick = true;

    /**
     * 字体大小
     */
    static final int TEXT_SIZE = 14;

    /**
     * 当前点击项
     */
    int mCurrentItem = -1;

    public BaseView(Context context) {
        this(context, null);
    }

    public BaseView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint(context);
    }

    /**
     * 初始化配置
     *
     * @param context context
     */
    private void initPaint(Context context) {
        mCurMonthTextPaint.setAntiAlias(true);
        mCurMonthTextPaint.setTextAlign(Paint.Align.CENTER);
        mCurMonthTextPaint.setColor(0xFF111111);
        mCurMonthTextPaint.setFakeBoldText(true);
        mCurMonthTextPaint.setTextSize(CalendarUtil.dipToPx(context, TEXT_SIZE));

        mOtherMonthTextPaint.setAntiAlias(true);
        mOtherMonthTextPaint.setTextAlign(Paint.Align.CENTER);
        mOtherMonthTextPaint.setColor(0xFFe1e1e1);
        mOtherMonthTextPaint.setFakeBoldText(true);
        mOtherMonthTextPaint.setTextSize(CalendarUtil.dipToPx(context, TEXT_SIZE));

        mCurMonthLunarTextPaint.setAntiAlias(true);
        mCurMonthLunarTextPaint.setTextAlign(Paint.Align.CENTER);

        mSelectedLunarTextPaint.setAntiAlias(true);
        mSelectedLunarTextPaint.setTextAlign(Paint.Align.CENTER);

        mOtherMonthLunarTextPaint.setAntiAlias(true);
        mOtherMonthLunarTextPaint.setTextAlign(Paint.Align.CENTER);


        mSchemeLunarTextPaint.setAntiAlias(true);
        mSchemeLunarTextPaint.setTextAlign(Paint.Align.CENTER);

        mSchemeTextPaint.setAntiAlias(true);
        mSchemeTextPaint.setStyle(Paint.Style.FILL);
        mSchemeTextPaint.setTextAlign(Paint.Align.CENTER);
        mSchemeTextPaint.setColor(0xffed5353);
        mSchemeTextPaint.setFakeBoldText(true);
        mSchemeTextPaint.setTextSize(CalendarUtil.dipToPx(context, TEXT_SIZE));

        mSelectTextPaint.setAntiAlias(true);
        mSelectTextPaint.setStyle(Paint.Style.FILL);
        mSelectTextPaint.setTextAlign(Paint.Align.CENTER);
        mSelectTextPaint.setColor(0xffed5353);
        mSelectTextPaint.setFakeBoldText(true);
        mSelectTextPaint.setTextSize(CalendarUtil.dipToPx(context, TEXT_SIZE));

        mSchemePaint.setAntiAlias(true);
        mSchemePaint.setStyle(Paint.Style.FILL);
        mSchemePaint.setStrokeWidth(2);
        mSchemePaint.setColor(0xffefefef);

        mCurDayTextPaint.setAntiAlias(true);
        mCurDayTextPaint.setTextAlign(Paint.Align.CENTER);
        mCurDayTextPaint.setColor(Color.RED);
        mCurDayTextPaint.setFakeBoldText(true);
        mCurDayTextPaint.setTextSize(CalendarUtil.dipToPx(context, TEXT_SIZE));

        mCurDayLunarTextPaint.setAntiAlias(true);
        mCurDayLunarTextPaint.setTextAlign(Paint.Align.CENTER);
        mCurDayLunarTextPaint.setColor(Color.RED);
        mCurDayLunarTextPaint.setFakeBoldText(true);
        mCurDayLunarTextPaint.setTextSize(CalendarUtil.dipToPx(context, TEXT_SIZE));

        mSelectedPaint.setAntiAlias(true);
        mSelectedPaint.setStyle(Paint.Style.FILL);
        mSelectedPaint.setStrokeWidth(2);

        setOnClickListener(this);
        setOnLongClickListener(this);
    }

    /**
     * 初始化所有UI配置
     *
     * @param delegate delegate
     */
    void setup(CalendarViewDelegate delegate) {
        this.mDelegate = delegate;

        this.mCurDayTextPaint.setColor(delegate.getCurDayTextColor());
        this.mCurDayLunarTextPaint.setColor(delegate.getCurDayLunarTextColor());
        this.mCurMonthTextPaint.setColor(delegate.getCurrentMonthTextColor());
        this.mOtherMonthTextPaint.setColor(delegate.getOtherMonthTextColor());
        this.mCurMonthLunarTextPaint.setColor(delegate.getCurrentMonthLunarTextColor());
        this.mSelectedLunarTextPaint.setColor(delegate.getSelectedLunarTextColor());
        this.mSelectTextPaint.setColor(delegate.getSelectedTextColor());
        this.mOtherMonthLunarTextPaint.setColor(delegate.getOtherMonthLunarTextColor());
        this.mSchemeLunarTextPaint.setColor(delegate.getSchemeLunarTextColor());

        this.mSchemePaint.setColor(delegate.getSchemeThemeColor());
        this.mSchemeTextPaint.setColor(delegate.getSchemeTextColor());


        this.mCurMonthTextPaint.setTextSize(delegate.getDayTextSize());
        this.mOtherMonthTextPaint.setTextSize(delegate.getDayTextSize());
        this.mCurDayTextPaint.setTextSize(delegate.getDayTextSize());
        this.mSchemeTextPaint.setTextSize(delegate.getDayTextSize());
        this.mSelectTextPaint.setTextSize(delegate.getDayTextSize());

        this.mCurMonthLunarTextPaint.setTextSize(delegate.getLunarTextSize());
        this.mSelectedLunarTextPaint.setTextSize(delegate.getLunarTextSize());
        this.mCurDayLunarTextPaint.setTextSize(delegate.getLunarTextSize());
        this.mOtherMonthLunarTextPaint.setTextSize(delegate.getLunarTextSize());
        this.mSchemeLunarTextPaint.setTextSize(delegate.getLunarTextSize());

        this.mSelectedPaint.setStyle(Paint.Style.FILL);
        this.mSelectedPaint.setColor(delegate.getSelectedThemeColor());
        setItemHeight(delegate.getCalendarItemHeight());
    }


    /**
     * 移除事件
     */
    final void removeSchemes() {
        for (Calendar a : mItems) {
            a.setScheme("");
            a.setSchemeColor(0);
            a.setSchemes(null);
        }
    }


    /**
     * 添加事件标记，来自List
     */
    final void addSchemesFromList() {
        if (mDelegate.mSchemeDate == null || mDelegate.mSchemeDate.size() == 0) {
            return;
        }
        for (Calendar a : mItems) {//添加操作
            if (mDelegate.mSchemeDate.contains(a)) {
                Calendar d = mDelegate.mSchemeDate.get(mDelegate.mSchemeDate.indexOf(a));
                a.setScheme(TextUtils.isEmpty(d.getScheme()) ? mDelegate.getSchemeText() : d.getScheme());
                a.setSchemeColor(d.getSchemeColor());
                a.setSchemes(d.getSchemes());
            } else {
                a.setScheme("");
                a.setSchemeColor(0);
                a.setSchemes(null);
            }
        }
    }

    /**
     * 添加事件标记，来自Map
     */
    final void addSchemesFromMap() {
        if (mDelegate.mSchemeDatesMap == null || mDelegate.mSchemeDatesMap.size() == 0) {
            return;
        }
        for (Calendar a : mItems) {
            if (mDelegate.mSchemeDatesMap.containsKey(a.toString())) {
                Calendar d = mDelegate.mSchemeDatesMap.get(a.toString());
                a.setScheme(TextUtils.isEmpty(d.getScheme()) ? mDelegate.getSchemeText() : d.getScheme());
                a.setSchemeColor(d.getSchemeColor());
                a.setSchemes(d.getSchemes());
            } else {
                a.setScheme("");
                a.setSchemeColor(0);
                a.setSchemes(null);
            }
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getPointerCount() > 1) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mX = event.getX();
                mY = event.getY();
                isClick = true;
                break;
            case MotionEvent.ACTION_MOVE:
                float mDY;
                if (isClick) {
                    mDY = event.getY() - mY;
                    isClick = Math.abs(mDY) <= 50;
                }
                break;
            case MotionEvent.ACTION_UP:
                mX = event.getX();
                mY = event.getY();
                break;
        }
        return super.onTouchEvent(event);
    }


    /**
     * 开始绘制前的钩子，这里做一些初始化的操作，每次绘制只调用一次，性能高效
     * 没有需要可忽略不实现
     * 例如：
     * 1、需要绘制圆形标记事件背景，可以在这里计算半径
     * 2、绘制矩形选中效果，也可以在这里计算矩形宽和高
     */
    protected void onPreviewHook() {
    }


    /**
     * 设置高度
     *
     * @param itemHeight itemHeight
     */
    private void setItemHeight(int itemHeight) {
        this.mItemHeight = itemHeight;
        Paint.FontMetrics metrics = mCurMonthTextPaint.getFontMetrics();
        mTextBaseLine = mItemHeight / 2 - metrics.descent + (metrics.bottom - metrics.top) / 2;
    }


    /**
     * 是否是选中的
     *
     * @param calendar calendar
     * @return true or false
     */
    protected boolean isSelected(Calendar calendar) {
        return mDelegate.isSelect(calendar);
    }

    /**
     * 获取文本的基线
     * MonthView用的，因为多行
     *
     * @param y y的坐标
     * @return
     */
    protected float getBaselineY(int y) {
        return mTextBaseLine + y - CalendarUtil.dipToPx(getContext(), 1);
    }

    /**
     * 获取文本的基线
     * 给WeekView用的，因为就是一行
     *
     * @return
     */
    protected float getBaselineY() {
        return mTextBaseLine - CalendarUtil.dipToPx(getContext(), 1);
    }

    /**
     * 更新事件
     */
    final void update() {
        if (mDelegate.getSchemeType() == CalendarViewDelegate.SCHEME_TYPE_LIST) {
            if (mDelegate.mSchemeDate == null || mDelegate.mSchemeDate.size() == 0) {//清空操作
                removeSchemes();
                invalidate();
                return;
            }
            addSchemesFromList();
        } else {
            if (mDelegate.mSchemeDatesMap == null || mDelegate.mSchemeDatesMap.size() == 0) {//清空操作
                removeSchemes();
                invalidate();
                return;
            }
            addSchemesFromMap();
        }
        invalidate();
    }

    /**
     * 是否是默认选择模式
     *
     * @return
     */
    public boolean isSelectModeDefault() {
        return mDelegate.isSelectModeDefault();
    }

    /**
     * 是否是单一选择模式
     *
     * @return
     */
    public boolean isSelectModeSingle() {
        return mDelegate.isSelectModeSingle();
    }

    /**
     * 是否是多选模式
     *
     * @return
     */
    public boolean isSelectModeMultiple() {
        return mDelegate.isSelectModeMultiple();
    }

    /**
     * 是否是范围选择模式
     *
     * @return
     */
    public boolean isSelectModeRange() {
        return mDelegate.isSelectModeRange();
    }

    /**
     * 获取单选或者默认模式的,选中数据
     *
     * @return
     */
    public Calendar getSelectedCalendar() {
        return mDelegate.mSelectedCalendar;
    }

    /**
     * 获取多选的数据
     *
     * @return
     */
    public List<Calendar> getSelectedCalendars() {
        return mDelegate.mSelectedCalendars;
    }

    /**
     * 获取选中的数据，多个会选第一个
     *
     * @return
     */
    public Calendar getSelectOne() {
        return mDelegate.getSelectOne();
    }

    /**
     * 获取默认的绘制文本，可以重写，按照自定的
     *
     * @param calendar
     * @param hasScheme
     * @param isSelected
     */
    protected String getDrawText(Calendar calendar, boolean hasScheme, boolean isSelected) {
        return String.valueOf(calendar.getDay());
    }

    abstract void updateCurrentDate();

    protected abstract void onDestroy();

}
