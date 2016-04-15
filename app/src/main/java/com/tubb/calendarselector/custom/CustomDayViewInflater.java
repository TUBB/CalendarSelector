package com.tubb.calendarselector.custom;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tubb.calendarselector.R;
import com.tubb.calendarselector.library.FullDay;

/**
 * Created by tubingbing on 16/4/14.
 */
public class CustomDayViewInflater extends DayViewInflater{

    public CustomDayViewInflater(Context context) {
        super(context);
    }

    @Override
    public DayViewHolder inflateDayView(ViewGroup container) {
        View dayView = mLayoutInflater.inflate(R.layout.layout_dayview_custom, container, false);
        return new CustomDayViewHolder(dayView);
    }

    public static class CustomDayViewHolder extends DayViewHolder{

        protected TextView tvDay;
        private int mPrevMonthDayTextColor;
        private int mNextMonthDayTextColor;

        public CustomDayViewHolder(View dayView) {
            super(dayView);
            tvDay = (TextView) dayView.findViewById(com.tubb.calendarselector.library.R.id.tvDay);
            mPrevMonthDayTextColor = ContextCompat.getColor(mContext, com.tubb.calendarselector.library.R.color.c_999999);
            mNextMonthDayTextColor = ContextCompat.getColor(mContext, com.tubb.calendarselector.library.R.color.c_999999);
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
}
