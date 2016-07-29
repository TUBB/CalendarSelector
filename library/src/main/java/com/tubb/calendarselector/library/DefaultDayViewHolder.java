package com.tubb.calendarselector.library;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.tubb.calendarselector.custom.DayViewHolder;

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
        int targetSDKVersion = 0;
        try {
            PackageInfo packageInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            targetSDKVersion = packageInfo.applicationInfo.targetSdkVersion;
        } catch (PackageManager.NameNotFoundException e) {}

        if (targetSDKVersion >= 23) {
            mPrevMonthDayTextColor = ContextCompat.getColor(mContext, R.color.c_999999);
        } else {
            mPrevMonthDayTextColor = mContext.getResources().getColor(R.color.c_999999);
        }

        if (targetSDKVersion >= 23) {
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
