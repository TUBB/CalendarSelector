package com.tubb.calendarselector.library;

import java.util.List;

/**
 * Created by tubingbing on 16/2/24.
 */
public abstract class IntervalSelectListener {
    public abstract void onIntervalSelect(List<SSDay> selectedDays);
    public boolean onInterceptSelect(List<SSDay> selectedDays, SSDay selectingDay){
        return false;
    }
}
