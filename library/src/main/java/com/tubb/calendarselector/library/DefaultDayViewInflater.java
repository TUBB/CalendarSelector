package com.tubb.calendarselector.library;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;

import com.tubb.calendarselector.custom.DayViewHolder;
import com.tubb.calendarselector.custom.DayViewInflater;

/**
 * Created by tubingbing on 16/4/13.
 */
public final class DefaultDayViewInflater extends DayViewInflater {

    private Paint mDecorPaint;
    private int mDecorWidth; // px

    public DefaultDayViewInflater(Context context) {
        super(context);
        mDecorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDecorWidth = dip2px(mContext, 0.5f);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mDecorPaint.setColor(ContextCompat.getColor(mContext, R.color.c_dddddd));
        } else {
            mDecorPaint.setColor(context.getResources().getColor(R.color.c_dddddd));
        }
        mDecorPaint.setStrokeWidth(mDecorWidth);
    }

    @Override
    public DayViewHolder inflateDayView(ViewGroup container) {
        View dayView = mLayoutInflater.inflate(R.layout.layout_dayview_default, container, false);
        return new DefaultDayViewHolder(dayView);
    }

}
