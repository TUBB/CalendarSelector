package com.tubb.calendarselector;

import com.tubb.calendarselector.library.DateUtils;

/**
 * Created by tubingbing on 16/1/22.
 */
public class Test {
    public static void main(String[] args){
        int day = DateUtils.getDayOfWeekInMonth(2016, 3);
        System.out.print(day);
    }
}
