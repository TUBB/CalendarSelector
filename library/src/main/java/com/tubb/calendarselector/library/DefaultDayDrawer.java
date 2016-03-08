package com.tubb.calendarselector.library;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.List;

/**
 * Created by tubingbing on 16/1/28.
 */
public class DefaultDayDrawer extends DayDrawer {

    protected static final String TAG = "mv";
    protected Context mContext;
    protected MonthView monthView;
    protected Paint mNormalDayPaint;
    protected Paint mPreMonthDayPaint;
    protected Paint mNextMonthDayPaint;
    protected Paint mTodayPaint;
    protected Paint mSelectedDayPaint;
    protected Paint mSelectedDayCirclePaint;

    public DefaultDayDrawer(Context context){
        mContext = context;
    }

    @Override
    public void init(MonthView monthView) {
        this.monthView = monthView;
        mNormalDayPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mNormalDayPaint.setColor(monthView.getNormalDayColor());
        mNormalDayPaint.setTextSize(monthView.getDaySize());

        mPreMonthDayPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPreMonthDayPaint.setColor(monthView.getPrevMonthDayColor());
        mPreMonthDayPaint.setTextSize(monthView.getDaySize());

        mNextMonthDayPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mNextMonthDayPaint.setColor(monthView.getNextMonthDayColor());
        mNextMonthDayPaint.setTextSize(monthView.getDaySize());

        mTodayPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTodayPaint.setColor(monthView.getTodayColor());
        mTodayPaint.setTextSize(monthView.getDaySize());

        mSelectedDayPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSelectedDayPaint.setColor(monthView.getSelectedDayColor());
        mSelectedDayPaint.setTextSize(monthView.getDaySize());

        mSelectedDayCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSelectedDayCirclePaint.setColor(monthView.getSelectedDayCircleColor());
        mSelectedDayCirclePaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void draw(Canvas canvas){

        List<List<FullDay>> monthDays = monthView.getMonthDays();
        SCMonth SCMonth = monthView.getSCMonth();
        int dayViewWidth = monthView.getDayWidth();
        int dayViewHeight = monthView.getDayHeight();

        for (int row = 0; row < monthDays.size(); row++){
            List<FullDay> ssDays = monthDays.get(row);
            for (int col = 0; col < ssDays.size(); col++){
                FullDay ssDay = ssDays.get(col);
                if(monthView.isDrawMonthDay()
                        && !SCDateUtils.isMonthDay(SCMonth.getYear(), SCMonth.getMonth(), ssDay.getYear(), ssDay.getMonth())){
                    continue;
                }
                if(SCMonth.getSelectedDays().contains(ssDay))
                    drawSelectedDay(canvas, String.valueOf(ssDay.getDay()), row, col, dayViewWidth, dayViewHeight);
                else if(SCDateUtils.isToday(ssDay.getYear(), ssDay.getMonth(), ssDay.getDay()))
                    drawToday(canvas, String.valueOf(ssDay.getDay()), row, col, dayViewWidth, dayViewHeight);
                else if(SCDateUtils.isMonthDay(SCMonth.getYear(), SCMonth.getMonth(), ssDay.getYear(), ssDay.getMonth()))
                    drawMonthDay(canvas, String.valueOf(ssDay.getDay()), row, col, dayViewWidth, dayViewHeight);
                else if(SCDateUtils.isPrevMonthDay(SCMonth.getYear(), SCMonth.getMonth(), ssDay.getYear(), ssDay.getMonth()))
                    drawPrevMonthDay(canvas, String.valueOf(ssDay.getDay()), row, col, dayViewWidth, dayViewHeight);
                else if(SCDateUtils.isNextMonthDay(SCMonth.getYear(), SCMonth.getMonth(), ssDay.getYear(), ssDay.getMonth()))
                    drawNextMonthDay(canvas, String.valueOf(ssDay.getDay()), row, col, dayViewWidth, dayViewHeight);
            }
        }
    }

    protected void drawMonthDay(Canvas canvas, String day, int row, int col, int dayViewWidth, int dayViewHeight) {
        canvas.drawText(day, getX(day, col, dayViewWidth, mNormalDayPaint),
                getY(day, row, dayViewHeight, mNormalDayPaint), mNormalDayPaint);
    }

    protected void drawPrevMonthDay(Canvas canvas, String day, int row, int col, int dayViewWidth, int dayViewHeight) {
        canvas.drawText(day, getX(day, col, dayViewWidth, mPreMonthDayPaint),
                getY(day, row, dayViewHeight, mPreMonthDayPaint), mPreMonthDayPaint);
    }

    protected void drawNextMonthDay(Canvas canvas, String day, int row, int col, int dayViewWidth, int dayViewHeight) {
        canvas.drawText(day, getX(day, col, dayViewWidth, mNextMonthDayPaint),
                getY(day, row, dayViewHeight, mNextMonthDayPaint), mNextMonthDayPaint);
    }

    protected void drawToday(Canvas canvas, String day, int row, int col, int dayViewWidth, int dayViewHeight) {
        canvas.drawText(day, getX(day, col, dayViewWidth, mTodayPaint),
                getY(day, row, dayViewHeight, mTodayPaint), mTodayPaint);
    }

    protected void drawSelectedDay(Canvas canvas, String day, int row, int col, int dayViewWidth, int dayViewHeight) {
        canvas.drawCircle(getCenterX(col, dayViewWidth), getCenterY(row, dayViewHeight), getCircleRadius(mSelectedDayPaint), mSelectedDayCirclePaint);
        canvas.drawText(day, getX(day, col, dayViewWidth, mSelectedDayPaint),
                getY(day, row, dayViewHeight, mSelectedDayPaint), mSelectedDayPaint);
    }

    protected float getCircleRadius(Paint paint){
        return paint.getTextSize();
    }
}
