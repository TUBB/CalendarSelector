package com.tubb.calendarselector.library;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.Log;

/**
 * Created by tubingbing on 16/1/28.
 */
public class DefaultSSDayDrawer implements SSDayDrawer{

    private static final String TAG = "mv";
    private Context mContext;
    private Paint mNormalDayPaint;
    private Paint mPreMonthDayPaint;
    private Paint mNextMonthDayPaint;
    private Paint mTodayPaint;

    public DefaultSSDayDrawer(Context context){
        mContext = context;
        mNormalDayPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mNormalDayPaint.setColor(ContextCompat.getColor(context, R.color.c_000000));
        mNormalDayPaint.setTextSize(context.getResources().getDimensionPixelSize(R.dimen.t_16));

        mPreMonthDayPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPreMonthDayPaint.setColor(ContextCompat.getColor(context, R.color.c_999999));
        mPreMonthDayPaint.setTextSize(context.getResources().getDimensionPixelSize(R.dimen.t_16));

        mNextMonthDayPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mNextMonthDayPaint.setColor(ContextCompat.getColor(context, R.color.c_999999));
        mNextMonthDayPaint.setTextSize(context.getResources().getDimensionPixelSize(R.dimen.t_16));

        mTodayPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTodayPaint.setColor(ContextCompat.getColor(context, R.color.c_ff6666));
        mTodayPaint.setTextSize(context.getResources().getDimensionPixelSize(R.dimen.t_16));

    }

    @Override
    public void draw(SSDay ssDay, Canvas canvas, int row, int col, int dayViewWidth, int dayViewHeight) {
        Log.d(TAG, "day:"+ssDay.toString()+" row:"+row+" col:"+col+" dayViewWidth:"+dayViewWidth+" dayViewHeight:"+dayViewHeight);
        switch (ssDay.getDayType()){
            case SSDay.CURRENT_MONTH_DAY:
                canvas.drawText(String.valueOf(ssDay.getDay()), getX(ssDay.getDay(), col, dayViewWidth, mNormalDayPaint),
                        getY(row, dayViewHeight, mNormalDayPaint), mNormalDayPaint);
                break;
            case SSDay.PRE_MONTH_DAY:
                canvas.drawText(String.valueOf(ssDay.getDay()), getX(ssDay.getDay(), col, dayViewWidth, mPreMonthDayPaint),
                        getY(row, dayViewHeight, mPreMonthDayPaint), mPreMonthDayPaint);
                break;
            case SSDay.NEXT_MONTH_DAY:
                canvas.drawText(String.valueOf(ssDay.getDay()), getX(ssDay.getDay(), col, dayViewWidth, mNextMonthDayPaint),
                        getY(row, dayViewHeight, mNextMonthDayPaint), mNextMonthDayPaint);
                break;
            case SSDay.TODAY:
                canvas.drawText(String.valueOf(ssDay.getDay()), getX(ssDay.getDay(), col, dayViewWidth, mTodayPaint),
                        getY(row, dayViewHeight, mTodayPaint), mTodayPaint);
                break;
        }

    }

    @Override
    public void onDayClick(SSDay ssDay, SSMonthView ssMonthView) {

    }

    private float getX(int day, int col, int dayViewWidth, Paint paint){
        return (col + 0.5f) * dayViewWidth - paint.measureText(String.valueOf(day)) / 2;
    }

    private float getY(int row, int dayViewHeight, Paint paint){
        int c = (row + 1) * dayViewHeight - dayViewHeight / 2;
        float t = paint.getTextSize() / 2;
        return c + t;
    }

}
