package com.ashlikun.calendarview.simple.single;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import com.ashlikun.calendarview.Calendar;
import com.ashlikun.calendarview.WeekView;

import java.util.List;

/**
 * 多彩周视图
 */
public class SingleWeekView extends WeekView {

    private int mRadius;
    private Paint mRingPaint = new Paint();
    private int mRingRadius;
    Paint ranglePaint;

    public SingleWeekView(Context context) {
        super(context);
        //兼容硬件加速无效的代码
        setLayerType(View.LAYER_TYPE_SOFTWARE, mSelectedPaint);
        //4.0以上硬件加速会导致无效
        mSelectedPaint.setMaskFilter(new BlurMaskFilter(30, BlurMaskFilter.Blur.SOLID));

        setLayerType(View.LAYER_TYPE_SOFTWARE, mSchemePaint);
        mSchemePaint.setMaskFilter(new BlurMaskFilter(30, BlurMaskFilter.Blur.SOLID));

        mRingPaint.setAntiAlias(true);
        mRingPaint.setColor(mSchemePaint.getColor());
        mRingPaint.setStyle(Paint.Style.STROKE);
        mRingPaint.setStrokeWidth(dipToPx(context, 1));
        setLayerType(View.LAYER_TYPE_SOFTWARE, mRingPaint);
        mRingPaint.setMaskFilter(new BlurMaskFilter(30, BlurMaskFilter.Blur.SOLID));
        ranglePaint = new Paint();
        ranglePaint.setStyle(Paint.Style.FILL);
        ranglePaint.setAntiAlias(true);
        ranglePaint.setColor(0xffff0000);
        ranglePaint.setTextSize(dipToPx(getContext(), 11));
    }

    @Override
    protected void onPreviewHook() {
        mRadius = Math.min(mItemWidth, mItemHeight) / 6 * 2;
        mRingRadius = Math.min(mItemWidth, mItemHeight) / 5 * 2;
        mSelectTextPaint.setTextSize(dipToPx(getContext(), 17));
    }

    /**
     * 如果需要点击Scheme没有效果，则return true
     *
     * @param canvas    canvas
     * @param calendar  日历日历calendar
     * @param x         日历Card x起点坐标
     * @param hasScheme hasScheme 非标记的日期
     * @return false 则不绘制onDrawScheme，因为这里背景色是互斥的
     */
    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, boolean hasScheme) {
        int cx = x + mItemWidth / 2;
        int cy = mItemHeight / 2;
        canvas.drawCircle(cx, cy, mRingRadius, mSelectedPaint);
        if (isSelectModeRange()) {
            List<Calendar> calendars = getSelectedCalendars();
            int index = calendars.indexOf(calendar);
            if (index == 0) {
                float textWidth = ranglePaint.measureText("开始", 0, 2);
                canvas.drawText("开始", x + mItemWidth - textWidth, getBaselineY(), ranglePaint);
            } else if (index == 1) {
                float textWidth = ranglePaint.measureText("结束", 0, 2);
                canvas.drawText("结束", x + mItemWidth - textWidth, getBaselineY(), ranglePaint);
            }
            return false;
        }
        return true;
    }


    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x) {
        int cx = x + mItemWidth / 2;
        int cy = mItemHeight / 2;
        canvas.drawCircle(cx, cy, mRadius, mSchemePaint);
    }

    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, boolean hasScheme, boolean isSelected) {
        float baselineY = getBaselineY();
        int cx = x + mItemWidth / 2;
        String text = getDrawText(calendar, hasScheme, isSelected);
        if (isSelected) {
            canvas.drawText(text,
                    cx,
                    baselineY,
                    mSelectTextPaint);
        } else if (hasScheme) {
            canvas.drawText(text,
                    cx,
                    baselineY,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() ? mSchemeTextPaint : mOtherMonthTextPaint);

        } else {
            canvas.drawText(text,
                    cx,
                    baselineY,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() ? mCurMonthTextPaint : mOtherMonthTextPaint);
        }
    }

    @Override
    protected String getDrawText(Calendar calendar, boolean hasScheme, boolean isSelected) {
        if (isSelected) {
            if (isSelectModeRange()) {
            }
        }
        return calendar.isCurrentDay() ? "今" : String.valueOf(calendar.getDay());
    }

    /**
     * dp转px
     *
     * @param context context
     * @param dpValue dp
     * @return px
     */
    @SuppressWarnings("all")
    private static int dipToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
