package com.tubb.calendarselector.library;

/**
 * Created by tubingbing on 16/2/24.
 */
public abstract class SegmentSelectListener {
    public abstract void onSegmentSelect(SSDay startDay, SSDay endDay);
    public boolean onInterceptSelect(SSDay startDay, SSDay endDay){
        return false;
    }
    public boolean onInterceptSelect(SSDay selectingDay){
        return false;
    }
}
