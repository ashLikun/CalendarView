package com.ashlikun.calendarview;

import java.io.Serializable;

/**
 * 作者　　: 李坤
 * 创建时间: 2018/8/12 0012 13:08
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：月数据mode
 */

final class Month implements Serializable {
    private int diff;//日期偏移
    private int count;
    private int month;
    private int year;

    int getDiff() {
        return diff;
    }

    void setDiff(int diff) {
        this.diff = diff;
    }

    int getCount() {
        return count;
    }

    void setCount(int count) {
        this.count = count;
    }

    int getMonth() {
        return month;
    }

    void setMonth(int month) {
        this.month = month;
    }

    int getYear() {
        return year;
    }

    void setYear(int year) {
        this.year = year;
    }
}
