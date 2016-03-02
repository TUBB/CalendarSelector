package com.tubb.calendarselector.library;

/**
 * Created by tubingbing on 16/2/24.
 */
public abstract class SegmentSelectListener {
    public abstract void onSegmentSelect(FullDay startDay, FullDay endDay);
    public boolean onInterceptSelect(FullDay startDay, FullDay endDay){
        return false;
    }
    public boolean onInterceptSelect(FullDay selectingDay){
        return false;
    }
}
