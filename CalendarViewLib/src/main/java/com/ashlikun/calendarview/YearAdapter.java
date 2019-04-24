package com.ashlikun.calendarview;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

/**
 * 作者　　: 李坤
 * 创建时间: 2018/8/12 0012 13:13
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：年适配器
 */

final class YearAdapter extends BaseRecyclerAdapter<Month> {
    private CalendarViewDelegate mDelegate;
    private int mItemHeight;
    private int mTextHeight;

    YearAdapter(Context context) {
        super(context);
        mTextHeight = CalendarUtil.dipToPx(context, 52);
    }

    void setup(CalendarViewDelegate delegate) {
        this.mDelegate = delegate;
    }

    void setItemHeight(int itemHeight) {
        this.mItemHeight = itemHeight;
    }

    @Override
    RecyclerView.ViewHolder onCreateDefaultViewHolder(ViewGroup parent, int type) {
        return new YearViewHolder(mInflater.inflate(R.layout.cv_item_list_year, parent, false), mDelegate);
    }

    @Override
    void onBindViewHolder(RecyclerView.ViewHolder holder, Month item, int position) {
        YearViewHolder h = (YearViewHolder) holder;
        YearView view = h.mYearView;
        view.setSchemeColor(mDelegate.getYearViewSchemeTextColor());
        view.setTextStyle(mDelegate.getYearViewDayTextSize(),
                mDelegate.getYearViewDayTextColor());
        view.init(item.getDiff(), item.getCount(), item.getYear(), item.getMonth());
        view.measureHeight(mItemHeight - mTextHeight);
        h.mTextMonth.setText(mContext.getResources().getStringArray(R.array.month_string_array)[item.getMonth() - 1]);
        h.mTextMonth.setTextSize(TypedValue.COMPLEX_UNIT_PX, mDelegate.getYearViewMonthTextSize());
        h.mTextMonth.setTextColor(mDelegate.getYearViewMonthTextColor());
    }

    private static class YearViewHolder extends RecyclerView.ViewHolder {
        YearView mYearView;
        TextView mTextMonth;

        YearViewHolder(View itemView, CalendarViewDelegate delegate) {
            super(itemView);
            mYearView = (YearView) itemView.findViewById(R.id.selectView);
            mYearView.setup(delegate);
            mTextMonth = (TextView) itemView.findViewById(R.id.tv_month);
        }
    }
}
