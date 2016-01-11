package com.tubb.calendarselector.library;

import java.util.Calendar;

/**
 * Created by tubingbing on 16/1/20.
 */
public class DateUtils {

    public static int getCurrentDay(){
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static int getCurrentMonth(){
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH) + 1;
    }

    public static int getCurrentYear(){
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    /**
     * the first day of month
     * @param year
     * @param month
     * @return week position
     */
    public static int getDayOfWeekInMonth(int year, int month){
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month-1);
        calendar.set(Calendar.DATE, 1);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public static int getDayCountOfMonth(int year, int month){
        int[] arr = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        int days = 0;
        if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
            arr[1] = 29; // 闰年2月29天
        }
        try {
            days = arr[month - 1];
        } catch (Exception e) {
            e.getStackTrace();
        }
        return days;
    }

    public static SSMonth prevMonth(int year, int month){
        if(month == 1){
            year -= 1;
            month = 12;
        }else{
            month -= 1;
        }
        return new SSMonth(year, month);
    }

    public static SSMonth nextMonth(int year, int month){
        if(month == 12){
            year += 1;
            month = 1;
        }else{
            month += 1;
        }
        return new SSMonth(year, month);
    }

    public static boolean isToday(int year, int month, int day) {
        return year == getCurrentYear() && month == getCurrentMonth() && day == getCurrentDay();
    }
}
