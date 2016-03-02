package com.tubb.calendarselector.library;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by tubingbing on 16/1/28.
 */
public abstract class SSDayDrawer {

    protected abstract void init(SSMonthView ssMonthView);
    protected abstract void drawMonthDay(Canvas canvas, String day, int row, int col, int dayViewWidth, int dayViewHeight);
    protected abstract void drawPrevMonthDay(Canvas canvas, String day, int row, int col, int dayViewWidth, int dayViewHeight);
    protected abstract void drawNextMonthDay(Canvas canvas, String day, int row, int col, int dayViewWidth, int dayViewHeight);
    protected abstract void drawToday(Canvas canvas, String day, int row, int col, int dayViewWidth, int dayViewHeight);
    protected abstract void drawSelectedDay(Canvas canvas, String day, int row, int col, int dayViewWidth, int dayViewHeight);
    protected void draw(SSMonth ssMonth, FullDay ssDay, Canvas canvas, int row, int col, int dayViewWidth, int dayViewHeight){
        if(ssMonth.getSelectedDays().contains(ssDay))
            drawSelectedDay(canvas, String.valueOf(ssDay.getDay()), row, col, dayViewWidth, dayViewHeight);
        else if(DateUtils.isToday(ssDay.getYear(), ssDay.getMonth(), ssDay.getDay()))
            drawToday(canvas, String.valueOf(ssDay.getDay()), row, col, dayViewWidth, dayViewHeight);
        else if(DateUtils.isMonthDay(ssMonth.getYear(), ssMonth.getMonth(), ssDay.getYear(), ssDay.getMonth()))
            drawMonthDay(canvas, String.valueOf(ssDay.getDay()), row, col, dayViewWidth, dayViewHeight);
        else if(DateUtils.isPrevMonthDay(ssMonth.getYear(), ssMonth.getMonth(), ssDay.getYear(), ssDay.getMonth()))
            drawPrevMonthDay(canvas, String.valueOf(ssDay.getDay()), row, col, dayViewWidth, dayViewHeight);
        else if(DateUtils.isNextMonthDay(ssMonth.getYear(), ssMonth.getMonth(), ssDay.getYear(), ssDay.getMonth()))
            drawNextMonthDay(canvas, String.valueOf(ssDay.getDay()), row, col, dayViewWidth, dayViewHeight);
    }

    protected float getX(String day, int col, int dayViewWidth, Paint paint){
        return getCX(col, dayViewWidth) - paint.measureText(day) / 2;
    }

    protected float getY(String day, int row, int dayViewHeight, Paint paint){
        return getCY(row, dayViewHeight) + paint.measureText(day, 0, 1) / 2;
    }

    protected float getCX(int col, int dayViewWidth){
        return (col + 0.5f) * dayViewWidth;
    }

    protected float getCY(int row, int dayViewHeight){
        return (row + 1) * dayViewHeight - dayViewHeight / 2;
    }

    protected float getLeft(int col, int dayViewWidth){
        return col * dayViewWidth;
    }

    protected float getRight(int col, int dayViewWidth){
        return col * dayViewWidth + dayViewWidth;
    }

    protected float getTop(int row, int dayViewHeight){
        return row * dayViewHeight;
    }

    protected float getBottom(int row, int dayViewHeight){
        return row * dayViewHeight + dayViewHeight;
    }
}
