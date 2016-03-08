package com.tubb.calendarselector.library;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by tubingbing on 16/1/20.
 */
public class SCDateUtils {

    private static final String[] SUNDAY_WEEKS = new String[]{"日", "一", "二", "三", "四", "五", "六"};
    private static final String[] MONDAY_WEEKS = new String[]{"一", "二", "三", "四", "五", "六", "日"};
    private static final String[] SATURDAY_WEEKS = new String[]{"六", "日", "一", "二", "三", "四", "五"};

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

    public static int mapDayOfWeekInMonth(int sundayPosition, int mappingWeek){
        if(mappingWeek <= 0 || mappingWeek == SCMonth.SUNDAY_OF_WEEK) return sundayPosition;
        else{
            String sundayPositionDesc = SUNDAY_WEEKS[sundayPosition - 1];
            if(mappingWeek == SCMonth.MONDAY_OF_WEEK){ // monday is the start day of a week
                for (int i = 0; i < MONDAY_WEEKS.length; i++) {
                    if(sundayPositionDesc.equals(MONDAY_WEEKS[i])) return i+1;
                }
            }else if(mappingWeek == SCMonth.SATURDAY_OF_WEEK){ // saturday is the start day of a week
                for (int i = 0; i < SATURDAY_WEEKS.length; i++) {
                    if(sundayPositionDesc.equals(SATURDAY_WEEKS[i])) return i+1;
                }
            }
        }
        return sundayPosition;
    }

    /**
     * the first day of month
     * @param year
     * @param month
     * @return week position
     */
    public static int getDayOfWeekInMonth(int year, int month){
        Calendar calendar = Calendar.getInstance();
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

    public static SCMonth prevMonth(int year, int month){
        if(month == 1){
            year -= 1;
            month = 12;
        }else{
            month -= 1;
        }
        return new SCMonth(year, month);
    }

    public static SCMonth nextMonth(int year, int month){
        if(month == 12){
            year += 1;
            month = 1;
        }else{
            month += 1;
        }
        return new SCMonth(year, month);
    }

    public static boolean isPrevMonthDay(int year, int month, int otherYear, int otherMonth){
        if((year - otherYear) == 1 && otherMonth == 12) return true;
        if(otherYear != year) return false;
        if(otherMonth + 1 != month) return false;
        return true;
    }

    public static boolean isMonthDay(int year, int month, int otherYear, int otherMonth){
        if(otherYear != year) return false;
        if(otherMonth != month) return false;
        return true;
    }

    public static boolean isNextMonthDay(int year, int month, int otherYear, int otherMonth){
        if((otherYear - year) == 1 && otherMonth == 1) return true;
        if(otherYear != year) return false;
        if(otherMonth - 1 != month) return false;
        return true;
    }

    public static boolean isToday(int year, int month, int day) {
        return year == getCurrentYear() && month == getCurrentMonth() && day == getCurrentDay();
    }

    public static int countDays(int startYear, int startMonth, int startDay, int endYear, int endMonth, int endDay) {
        Calendar startC = Calendar.getInstance();
        startC.set(Calendar.YEAR, startYear);
        startC.set(Calendar.MONTH, startMonth-1);
        startC.set(Calendar.DAY_OF_MONTH, startDay);
        Calendar endC = Calendar.getInstance();
        endC.set(Calendar.YEAR, endYear);
        endC.set(Calendar.MONTH, endMonth-1);
        endC.set(Calendar.DAY_OF_MONTH, endDay);
        return (int) ((endC.getTimeInMillis() - startC.getTimeInMillis()) / 86400000 + 1);
    }

    public static List<SCMonth> generateMonths(int startYear, int endYear){
        return generateMonths(startYear, 1, endYear, 12);
    }

    public static List<SCMonth> generateMonths(int startYear, int startMonth, int endYear, int endMonth){

        if(startYear <= 0 || endYear <= 0 || startMonth <= 0 || endMonth <= 0 || startMonth > 12 || endMonth > 12)
            throw new IllegalArgumentException("Invalid startYear、startMonth、endYear or endMonth");

        if(startYear > endYear) throw new IllegalArgumentException("startYear must less than endYear");

        if(startYear == endYear && startMonth > endMonth)
            throw new IllegalArgumentException("startMonth must less than endMonth when startYear equal to endYear");

        List<SCMonth> data = new ArrayList<>();
        if(startYear == endYear){
            for (int i = startMonth; i <= endMonth; i++) {
                data.add(new SCMonth(startYear, i));
            }
        }else{
            for (int i = startMonth; i <= 12; i++) {
                data.add(new SCMonth(startYear, i));
            }
            while (endYear - startYear > 1){
                startYear++;
                for (int i = 1; i <= 12; i++) {
                    data.add(new SCMonth(startYear, i));
                }
            }
            for (int i = 1; i <= endMonth; i++) {
                data.add(new SCMonth(endYear, i));
            }
        }
        return data;
    }
}
