package com.tubb.calendarselector.custom;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.widget.TextView;

import com.tubb.calendarselector.R;
import com.tubb.calendarselector.library.FullDay;

/**
 * Created by tubingbing on 16/4/14.
 */
public class AnimDayViewInflater extends DayViewInflater{

    public AnimDayViewInflater(Context context) {
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
            mNextMonthDayTextColor = ContextCompat.getColor(mContext, com.tubb.calendarselector.library.R.color.c_dddddd);
        }

        @Override
        public void setCurrentMonthDayText(FullDay day, boolean isSelected) {
            boolean oldSelected = tvDay.isSelected();
            tvDay.setText(String.valueOf(day.getDay()));
            tvDay.setSelected(isSelected);
            // selected animation
            if(!oldSelected && isSelected){
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.setInterpolator(AnimationUtils.loadInterpolator(mContext, android.R.anim.bounce_interpolator));
                animatorSet.play(ObjectAnimator.ofFloat(tvDay, "scaleX", 0.5f, 1.0f))
                        .with(ObjectAnimator.ofFloat(tvDay, "scaleY", 0.5f, 1.0f));
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
