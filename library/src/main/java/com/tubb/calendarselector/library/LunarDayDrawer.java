package com.tubb.calendarselector.library;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by hui.xiong on 2016/2/29.
 */
public class LunarDayDrawer extends SSDayDrawer{
    private static final String TAG = "mv";
    private Context mContext;
    private Paint mNormalDayPaint;
    private Paint mPreMonthDayPaint;
    private Paint mNextMonthDayPaint;
    private Paint mTodayPaint;
    private Paint mSelectedDayPaint;
    private Paint mSelectedDayCirclePaint;

    public LunarDayDrawer(Context context){
        mContext = context;
    }

    @Override
    protected void init(SSMonthView ssMonthView) {
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
    protected void draw(SSDay ssDay, Canvas canvas, int row, int col, int dayViewWidth, int dayViewHeight) {

        int[] lunar=LunarCalendar.solarToLunar(ssDay.getSsMonth().getYear(),ssDay.getSsMonth().getMonth(),ssDay.getDay());
        String day= LunarDay.getDayStr(lunar[1],lunar[2]);
        if (!ssDay.getSsMonth().getSelectedDays().contains(ssDay)){
           switch (ssDay.getDayType()) {
               case SSDay.CURRENT_MONTH_DAY:
                   canvas.drawText(String.valueOf(ssDay.getDay()), getX(ssDay.getDay(), col, dayViewWidth, mNormalDayPaint),
                           getY(ssDay.getDay(), row, dayViewHeight, mNormalDayPaint), mNormalDayPaint);
                   canvas.drawText(String.valueOf(day),getXLunar(day,col,dayViewWidth,mNormalDayPaint),
                           getYLunar(day,row,dayViewHeight,mNormalDayPaint),mNormalDayPaint);
                   break;
               case SSDay.PRE_MONTH_DAY:
                   canvas.drawText(String.valueOf(ssDay.getDay()), getX(ssDay.getDay(), col, dayViewWidth, mPreMonthDayPaint),
                           getY(ssDay.getDay(), row, dayViewHeight, mPreMonthDayPaint), mPreMonthDayPaint);
                   canvas.drawText(String.valueOf(day),getXLunar(day,col,dayViewWidth,mPreMonthDayPaint),
                           getYLunar(day,row,dayViewHeight,mPreMonthDayPaint),mPreMonthDayPaint);
                   break;
               case SSDay.NEXT_MONTH_DAY:
                   canvas.drawText(String.valueOf(ssDay.getDay()), getX(ssDay.getDay(), col, dayViewWidth, mNextMonthDayPaint),
                           getY(ssDay.getDay(), row, dayViewHeight, mNextMonthDayPaint), mNextMonthDayPaint);
                   canvas.drawText(String.valueOf(day),getXLunar(day,col,dayViewWidth,mNextMonthDayPaint),
                           getYLunar(day,row,dayViewHeight,mNextMonthDayPaint),mNextMonthDayPaint);
                   break;
               case SSDay.TODAY:
                   canvas.drawText(String.valueOf(ssDay.getDay()), getX(ssDay.getDay(), col, dayViewWidth, mTodayPaint),
                           getY(ssDay.getDay(), row, dayViewHeight, mTodayPaint), mTodayPaint);
                   canvas.drawText(String.valueOf(day),getXLunar(day,col,dayViewWidth,mTodayPaint),
                           getYLunar(day,row,dayViewHeight,mTodayPaint),mTodayPaint);
                   break;
           }
        }else {
            canvas.drawCircle(getCX(col, dayViewWidth), getCY(row, dayViewHeight), getCircleRadius(mSelectedDayPaint), mSelectedDayCirclePaint);
            canvas.drawText(String.valueOf(ssDay.getDay()), getX(ssDay.getDay(), col, dayViewWidth, mSelectedDayPaint),
                    getY(ssDay.getDay(), row, dayViewHeight, mSelectedDayPaint), mSelectedDayPaint);
            canvas.drawText(String.valueOf(day),getXLunar(day,col,dayViewWidth,mSelectedDayPaint),
                    getYLunar(day,row,dayViewHeight,mSelectedDayPaint),mSelectedDayPaint);
        }
    }
}
