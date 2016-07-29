package com.tubb.calendarselector.library;

import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.tubb.calendarselector.custom.DayViewHolder;
import com.tubb.calendarselector.library.FullDay;
import com.tubb.calendarselector.library.R;

/**
 * Created by tubingbing on 16/4/13.
 */
public final class DefaultDayViewHolder extends DayViewHolder {

    protected TextView tvDay;
    private int mPrevMonthDayTextColor;
    private int mNextMonthDayTextColor;

    public DefaultDayViewHolder(View dayView) {
        super(dayView);
        tvDay = (TextView) dayView.findViewById(R.id.tvDay);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mPrevMonthDayTextColor = ContextCompat.getColor(mContext, R.color.c_999999);
        } else {
            mPrevMonthDayTextColor = mContext.getResources().getColor(R.color.c_999999);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mNextMonthDayTextColor = ContextCompat.getColor(mContext, R.color.c_999999);
        } else {
            mNextMonthDayTextColor = mContext.getResources().getColor(R.color.c_999999);
        }
    }

    @Override
    public void setCurrentMonthDayText(FullDay day, boolean isSelected) {
        tvDay.setText(String.valueOf(day.getDay()));
        tvDay.setSelected(isSelected);
    }

    @Override
    public void setPrevMonthDayText(FullDay day) {
        tvDay.setTextColor(mPrevMonthDayTextColor);
        tvDay.setText(String.valueOf(day.getDay()));
    }

    @Override
    public void setNextMonthDayText(FullDay day) {
        tvDay.setTextColor(mNextMonthDayTextColor);
        tvDay.setText(String.valueOf(day.getDay()));
    }

}
