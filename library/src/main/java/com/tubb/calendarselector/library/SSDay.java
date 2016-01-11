package com.tubb.calendarselector.library;

/**
 * Created by tubingbing on 16/1/19.
 */
public class SSDay {

    public static final int PRE_MONTH_DAY = 0;
    public static final int TODAY = 1;
    public static final int CURRENT_MONTH_DAY = 2;
    public static final int NEXT_MONTH_DAY = 3;

    private int dayType;
    private SSMonth ssMonth;
    private int day;

    public SSDay(int dayType, SSMonth ssMonth, int day) {
        this.dayType = dayType;
        this.ssMonth = ssMonth;
        this.day = day;
    }

    public int getDayType() {
        return dayType;
    }

    public void setDayType(int dayType) {
        this.dayType = dayType;
    }

    public SSMonth getSsMonth() {
        return ssMonth;
    }

    public void setSsMonth(SSMonth ssMonth) {
        this.ssMonth = ssMonth;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    @Override
    public String toString() {
        return ssMonth.getYear()+"-"+ssMonth.getMonth()+"-"+day;
    }
}
