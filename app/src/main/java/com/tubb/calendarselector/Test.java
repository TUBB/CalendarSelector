package com.tubb.calendarselector;

import com.tubb.calendarselector.library.DateUtils;
import com.tubb.calendarselector.library.SSMonth;

/**
 * Created by tubingbing on 16/1/22.
 */
public class Test {
    public static void main(String[] args){
        int day = DateUtils.getDayOfWeekInMonth(2016, 2);
        System.out.print(DateUtils.mapDayOfWeekInMonth(day, SSMonth.SATURDAY_OF_WEEK));
    }
}
