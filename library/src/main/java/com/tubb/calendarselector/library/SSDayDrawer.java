package com.tubb.calendarselector.library;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by tubingbing on 16/1/28.
 */
public abstract class SSDayDrawer {

    protected abstract void init(SSMonthView ssMonthView);
    protected abstract void draw(SSDay ssDay, Canvas canvas, int row, int col, int dayViewWidth, int dayViewHeight);

    protected float getX(int day, int col, int dayViewWidth, Paint paint){
        return getCX(col, dayViewWidth) - paint.measureText(String.valueOf(day)) / 2;
    }

    protected float getY(int day, int row, int dayViewHeight, Paint paint){
        return getCY(row, dayViewHeight) + paint.measureText(String.valueOf(day), 0, 1) / 2;
    }

    protected float getXLunar(String day, int col, int dayViewWidth, Paint paint){
        return getCX(col, dayViewWidth) - paint.measureText(day) / 2;
    }
    protected float getYLunar(String day,int row, int dayViewHeight, Paint paint){
        return getCY(row, dayViewHeight) + paint.measureText(day, 0, 1) /2 *3 ;
    }
    protected float getCX(int col, int dayViewWidth){
        return (col + 0.5f) * dayViewWidth;
    }

    protected float getCY(int row, int dayViewHeight){
        return (row + 1) * dayViewHeight - dayViewHeight / 2;
    }
    protected float getCircleRadius(Paint paint){
        return paint.getTextSize();
    }
}
