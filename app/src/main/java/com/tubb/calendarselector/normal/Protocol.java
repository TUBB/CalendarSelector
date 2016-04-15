package com.tubb.calendarselector.normal;

import com.tubb.calendarselector.library.SCMonth;

/**
 * Created by tubingbing on 16/3/9.
 */
public interface Protocol {
    void clickNextMonthDay(SCMonth currentMonth);
    void clickPrevMonthDay(SCMonth currentMonth);
}
