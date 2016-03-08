package com.tubb.calendarselector.library;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by tubingbing on 16/1/28.
 */
public abstract class DayDrawer {

    protected abstract void init(MonthView monthView);

    protected abstract void draw(Canvas canvas);

    protected float getX(String day, int col, int dayViewWidth, Paint paint){
        return getCenterX(col, dayViewWidth) - paint.measureText(day) / 2;
    }

    protected float getY(String day, int row, int dayViewHeight, Paint paint){
        return getCenterY(row, dayViewHeight) + paint.measureText(day, 0, 1) / 2;
    }

    protected float getCenterX(int col, int dayViewWidth){
        return (col + 0.5f) * dayViewWidth;
    }

    protected float getCenterY(int row, int dayViewHeight){
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
