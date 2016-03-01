package com.tubb.calendarselector.library;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by tubingbing on 16/1/28.
 */
public class DefaultSSDayDrawer extends SSDayDrawer{

    private static final String TAG = "mv";
    private Context mContext;
    private Paint mNormalDayPaint;
    private Paint mPreMonthDayPaint;
    private Paint mNextMonthDayPaint;
    private Paint mTodayPaint;
    private Paint mSelectedDayPaint;
    private Paint mSelectedDayCirclePaint;

    public DefaultSSDayDrawer(Context context){
        mContext = context;
    }

    @Override
    public void init(SSMonthView ssMonthView) {
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
    public void draw(SSDay ssDay, Canvas canvas, int row, int col, int dayViewWidth, int dayViewHeight) {
        if(!ssDay.getSsMonth().getSelectedDays().contains(ssDay)){
            switch (ssDay.getDayType()){
                case SSDay.CURRENT_MONTH_DAY:
                    canvas.drawText(String.valueOf(ssDay.getDay()), getX(ssDay.getDay(), col, dayViewWidth, mNormalDayPaint),
                            getY(ssDay.getDay(), row, dayViewHeight, mNormalDayPaint), mNormalDayPaint);
                    break;
                case SSDay.PRE_MONTH_DAY:
                    canvas.drawText(String.valueOf(ssDay.getDay()), getX(ssDay.getDay(), col, dayViewWidth, mPreMonthDayPaint),
                            getY(ssDay.getDay(), row, dayViewHeight, mPreMonthDayPaint), mPreMonthDayPaint);
                    break;
                case SSDay.NEXT_MONTH_DAY:
                    canvas.drawText(String.valueOf(ssDay.getDay()), getX(ssDay.getDay(), col, dayViewWidth, mNextMonthDayPaint),
                            getY(ssDay.getDay(), row, dayViewHeight, mNextMonthDayPaint), mNextMonthDayPaint);
                    break;
                case SSDay.TODAY:
                    canvas.drawText(String.valueOf(ssDay.getDay()), getX(ssDay.getDay(), col, dayViewWidth, mTodayPaint),
                            getY(ssDay.getDay(), row, dayViewHeight, mTodayPaint), mTodayPaint);
                    break;
            }
        }else{
            canvas.drawCircle(getCX(col, dayViewWidth), getCY(row, dayViewHeight), getCircleRadius(mSelectedDayPaint), mSelectedDayCirclePaint);
            canvas.drawText(String.valueOf(ssDay.getDay()), getX(ssDay.getDay(), col, dayViewWidth, mSelectedDayPaint),
                    getY(ssDay.getDay(), row, dayViewHeight, mSelectedDayPaint), mSelectedDayPaint);
        }
    }


}
