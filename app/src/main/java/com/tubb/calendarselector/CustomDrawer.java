package com.tubb.calendarselector;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.tubb.calendarselector.library.DefaultSSDayDrawer;
import com.tubb.calendarselector.library.SSMonthView;

/**
 * Created by tubingbing on 16/3/2.
 */
public class CustomDrawer extends DefaultSSDayDrawer{

    private Paint mSelectedDayRectPaint;
    private float strokeWidth = 6.0f;

    public CustomDrawer(Context context) {
        super(context);
    }

    @Override
    public void init(SSMonthView ssMonthView) {
        super.init(ssMonthView);
        mSelectedDayRectPaint = new Paint();
        mSelectedDayRectPaint.setStyle(Paint.Style.STROKE);
        mSelectedDayRectPaint.setColor(ssMonthView.getSelectedDayCircleColor());
        mSelectedDayRectPaint.setStrokeWidth(strokeWidth);
    }

    @Override
    protected void drawSelectedDay(final Canvas canvas, final String day, final int row, final int col, final int dayViewWidth, final int dayViewHeight) {
        canvas.drawRect(getLeft(col, dayViewWidth)+strokeWidth, getTop(row, dayViewHeight)+strokeWidth, getRight(col, dayViewWidth)-strokeWidth, getBottom(row, dayViewHeight)-strokeWidth, mSelectedDayRectPaint);
        mSelectedDayPaint.setColor(ssMonthView.getSelectedDayCircleColor());
        canvas.drawText(day, getX(day, col, dayViewWidth, mSelectedDayPaint),
                getY(day, row, dayViewHeight, mSelectedDayPaint), mSelectedDayPaint);
    }
}
