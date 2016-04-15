package com.tubb.calendarselector.custom;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.tubb.calendarselector.library.FullDay;

/**
 * Created by tubingbing on 16/4/13.
 */
public abstract class DayViewHolder {

    protected Context mContext;
    protected View dayView;

    public DayViewHolder(View dayView){
        this.dayView = dayView;
        mContext = dayView.getContext();
    }

    public View getDayView() {
        return dayView;
    }

    public abstract void setCurrentMonthDayText(FullDay day, boolean isSelected);
    public abstract void setPrevMonthDayText(FullDay day);
    public abstract void setNextMonthDayText(FullDay day);
}
