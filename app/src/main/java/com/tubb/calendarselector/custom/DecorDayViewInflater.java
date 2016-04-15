package com.tubb.calendarselector.custom;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.tubb.calendarselector.R;
import com.tubb.calendarselector.library.FullDay;

/**
 * Created by tubingbing on 16/4/14.
 */
public class DecorDayViewInflater extends DayViewInflater{

    public DecorDayViewInflater(Context context) {
        super(context);
    }

    @Override
    public DayViewHolder inflateDayView(ViewGroup container) {
        View dayView = mLayoutInflater.inflate(R.layout.layout_dayview_decor_custom, container, false);
        return new CustomDayViewHolder(dayView);
    }

    @Override
    public Decor inflateHorizontalDecor(ViewGroup container, int row, int totalRow) {
        return new Decor(mLayoutInflater.inflate(R.layout.view_horizontal_decor, container, false), true);
    }

    @Override
    public Decor inflateVerticalDecor(ViewGroup container, int col, int totalCol) {
        return new Decor(mLayoutInflater.inflate(R.layout.view_vertical_decor, container, false), true);
    }

    public static class CustomDayViewHolder extends DayViewHolder{

        protected TextView tvDay;
        private int mPrevMonthDayTextColor;
        private int mNextMonthDayTextColor;

        public CustomDayViewHolder(View dayView) {
            super(dayView);
            tvDay = (TextView) dayView.findViewById(com.tubb.calendarselector.library.R.id.tvDay);
            mPrevMonthDayTextColor = ContextCompat.getColor(mContext, com.tubb.calendarselector.library.R.color.c_999999);
            mNextMonthDayTextColor = ContextCompat.getColor(mContext, com.tubb.calendarselector.library.R.color.c_dddddd);
        }

        @Override
        public void setCurrentMonthDayText(FullDay day, boolean isSelected) {
            boolean oldSelected = tvDay.isSelected();
            tvDay.setText(String.valueOf(day.getDay()));
            tvDay.setSelected(isSelected);
            if(!oldSelected && isSelected){
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.play(ObjectAnimator.ofFloat(tvDay, "rotationX", 0.0f, 360f))
                .with(ObjectAnimator.ofFloat(tvDay, "rotationY", 0.0f, 360f));
                animatorSet.setDuration(500)
                        .start();
            }
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
