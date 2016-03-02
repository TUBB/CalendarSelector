package com.tubb.calendarselector.library;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by tubingbing on 16/1/28.
 */
public class DefaultSSDayDrawer extends SSDayDrawer{

    protected static final String TAG = "mv";
    protected Context mContext;
    protected SSMonthView ssMonthView;
    protected Paint mNormalDayPaint;
    protected Paint mPreMonthDayPaint;
    protected Paint mNextMonthDayPaint;
    protected Paint mTodayPaint;
    protected Paint mSelectedDayPaint;
    protected Paint mSelectedDayCirclePaint;

    public DefaultSSDayDrawer(Context context){
        mContext = context;
    }

    @Override
    public void init(SSMonthView ssMonthView) {
        this.ssMonthView = ssMonthView;
        mNormalDayPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mNormalDayPaint.setColor(ssMonthView.getNormalDayColor());
        mNormalDayPaint.setTextSize(ssMonthView.getDaySize());

        mPreMonthDayPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPreMonthDayPaint.setColor(ssMonthView.getPrevMonthDayColor());
        mPreMonthDayPaint.setTextSize(ssMonthView.getDaySize());

        mNextMonthDayPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mNextMonthDayPaint.setColor(ssMonthView.getNextMonthDayColor());
        mNextMonthDayPaint.setTextSize(ssMonthView.getDaySize());

        mTodayPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTodayPaint.setColor(ssMonthView.getTodayColor());
        mTodayPaint.setTextSize(ssMonthView.getDaySize());

        mSelectedDayPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSelectedDayPaint.setColor(ssMonthView.getSelectedDayColor());
        mSelectedDayPaint.setTextSize(ssMonthView.getDaySize());

        mSelectedDayCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSelectedDayCirclePaint.setColor(ssMonthView.getSelectedDayCircleColor());
        mSelectedDayCirclePaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void drawMonthDay(Canvas canvas, String day, int row, int col, int dayViewWidth, int dayViewHeight) {
        canvas.drawText(day, getX(day, col, dayViewWidth, mNormalDayPaint),
                getY(day, row, dayViewHeight, mNormalDayPaint), mNormalDayPaint);
    }

    @Override
    protected void drawPrevMonthDay(Canvas canvas, String day, int row, int col, int dayViewWidth, int dayViewHeight) {
        canvas.drawText(day, getX(day, col, dayViewWidth, mPreMonthDayPaint),
                getY(day, row, dayViewHeight, mPreMonthDayPaint), mPreMonthDayPaint);
    }

    @Override
    protected void drawNextMonthDay(Canvas canvas, String day, int row, int col, int dayViewWidth, int dayViewHeight) {
        canvas.drawText(day, getX(day, col, dayViewWidth, mNextMonthDayPaint),
                getY(day, row, dayViewHeight, mNextMonthDayPaint), mNextMonthDayPaint);
    }

    @Override
    protected void drawToday(Canvas canvas, String day, int row, int col, int dayViewWidth, int dayViewHeight) {
        canvas.drawText(day, getX(day, col, dayViewWidth, mTodayPaint),
                getY(day, row, dayViewHeight, mTodayPaint), mTodayPaint);
    }

    @Override
    protected void drawSelectedDay(Canvas canvas, String day, int row, int col, int dayViewWidth, int dayViewHeight) {
        canvas.drawCircle(getCX(col, dayViewWidth), getCY(row, dayViewHeight), getCircleRadius(mSelectedDayPaint), mSelectedDayCirclePaint);
        canvas.drawText(day, getX(day, col, dayViewWidth, mSelectedDayPaint),
                getY(day, row, dayViewHeight, mSelectedDayPaint), mSelectedDayPaint);
    }

    protected float getCircleRadius(Paint paint){
        return paint.getTextSize();
    }
}
