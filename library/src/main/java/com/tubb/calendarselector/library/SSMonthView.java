package com.tubb.calendarselector.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by tubingbing on 16/1/18.
 */
public class SSMonthView extends View{

    private static final String TAG = "mv";

    public static final int ROW_COUNT = 6;
    public static final int COL_COUNT = 7;

    private boolean mDrawMonthDay = false;
    private int realRowCount = ROW_COUNT;
    protected Context mContext;
    protected DisplayMetrics mDisplayMetrics;
    private int widthAttr;
    private int heightAttr;
    private float mDownX;
    private float mDownY;
    private int mTouchSlop;

    private List<List<SSDay>> mMonthDays;
    private SSDayDrawer dayDrawer;

    private int mWidth;
    private int mHeight;
    private int mDayWidth;
    private int mDayHeight;
    private SSMonth ssMonth;

    public SSMonthView(Context context) {
        super(context);
        throw new RuntimeException("Can't use this constructor ( MonthView(Context context) )");
    }

    public SSMonthView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SSMonthView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SSMonthView, 0, defStyleAttr);
        mDrawMonthDay = a.getBoolean(R.styleable.SSMonthView_draw_monthday, false);
        a.recycle();
        mContext = context;
        if(isInEditMode())
            initEditorMode();
        dayDrawer = CalendarSelector.getInstance().getConfiguration().getSsDayDrawer();
        mDisplayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(mDisplayMetrics);
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    public void setSsMonth(int year, int month){
        if(year <=0 || month <=0 || month > 12)
            throw new IllegalArgumentException("Invalidate year or month");
        this.ssMonth = new SSMonth(year, month);
        calculateDays();
        invalidate();
    }

    private void calculateDays() {

        SSMonth prevMonth = DateUtils.prevMonth(ssMonth.getYear(), ssMonth.getMonth());
        int dayCountOfPrevMonth = DateUtils.getDayCountOfMonth(prevMonth.getYear(), prevMonth.getMonth());
        SSMonth nextMonth = DateUtils.nextMonth(ssMonth.getYear(), ssMonth.getMonth());

        int dayOfWeekInMonth = DateUtils.getDayOfWeekInMonth(ssMonth.getYear(), ssMonth.getMonth());
        int dayCountOfMonth = DateUtils.getDayCountOfMonth(ssMonth.getYear(), ssMonth.getMonth());

        mMonthDays = new ArrayList<>(ROW_COUNT);

        int realRow = 0;
        int day = 1;
        for (int row = 0; row < ROW_COUNT; row++){
            boolean isAllRowEmpty = true;
            ArrayList<SSDay> days = new ArrayList<>(COL_COUNT);
            for (int col = 1; col <= COL_COUNT; col++){
                int monthPosition = col + row * COL_COUNT;
                if(monthPosition >= dayOfWeekInMonth && monthPosition < dayOfWeekInMonth + dayCountOfMonth){ // 本月
                    SSDay currentMonthDay = new SSDay(SSDay.CURRENT_MONTH_DAY, ssMonth, day);
                    if(DateUtils.isToday(ssMonth.getYear(), ssMonth.getMonth(), day)){
                        currentMonthDay.setDayType(SSDay.TODAY);
                    }
                    days.add(currentMonthDay);
                    day++;
                    isAllRowEmpty = false;
                }else if(monthPosition < dayOfWeekInMonth){ // 上月
                    int prevDay = dayCountOfPrevMonth - (dayOfWeekInMonth - 1 - monthPosition);
                    SSDay prevMonthDay = new SSDay(SSDay.PRE_MONTH_DAY, prevMonth, prevDay);
                    days.add(prevMonthDay);
                }else if(monthPosition >= dayOfWeekInMonth + dayCountOfMonth){ // 下月
                    SSDay nextMonthDay = new SSDay(SSDay.NEXT_MONTH_DAY, nextMonth, monthPosition - (dayOfWeekInMonth + dayCountOfMonth) + 1);
                    days.add(nextMonthDay);
                }
            }
            mMonthDays.add(days);
            if(!isAllRowEmpty) realRow++;
        }

        if(mDrawMonthDay) realRowCount = realRow;
        else{
            // adjust display
            if(dayOfWeekInMonth == 1
                    && (realRow == (ROW_COUNT - 1) || realRow == (ROW_COUNT - 2))
                    && isFirstRowFullCurrentMonthDays()){

                mMonthDays.remove(mMonthDays.size() - 1);
                List<SSDay> ssDays = new ArrayList<>(COL_COUNT);
                for (int monthPosition = COL_COUNT - 1; monthPosition >= 0; monthPosition--){
                    int prevDay = dayCountOfPrevMonth - monthPosition;
                    SSDay prevMonthDay = new SSDay(SSDay.PRE_MONTH_DAY, prevMonth, prevDay);
                    ssDays.add(prevMonthDay);
                }
                mMonthDays.add(0, ssDays);
            }
        }
        Log.d(TAG, "realRowCount:"+realRowCount);

    }

    private boolean isFirstRowFullCurrentMonthDays() {
        List<SSDay> ssDays = mMonthDays.get(0);
        for (SSDay ssDay : ssDays){
            if(ssDay.getDayType() == SSDay.PRE_MONTH_DAY) return false;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(mWidth == 0 || mHeight == 0 || this.ssMonth == null) return;
        for (int row = 0; row < mMonthDays.size(); row++){
            List<SSDay> ssDays = mMonthDays.get(row);
            for (int col = 0; col < ssDays.size(); col++){
                SSDay ssDay = ssDays.get(col);
                int dayType = ssDay.getDayType();
                if(mDrawMonthDay){
                    if(dayType == SSDay.CURRENT_MONTH_DAY || dayType == SSDay.TODAY)
                        dayDrawer.draw(ssDay, canvas, row, col, mDayWidth, mDayHeight);
                }else{
                    dayDrawer.draw(ssDay, canvas, row, col, mDayWidth, mDayHeight);
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                mDownY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                float disX = event.getX() - mDownX;
                float disY = event.getY() - mDownY;
                if (Math.abs(disX) < mTouchSlop && Math.abs(disY) < mTouchSlop) {
                    int col = (int) (mDownX / mDayWidth);
                    int row = (int) (mDownY / mDayHeight);
                    measureClickCell(row, col);
                }
                break;
        }
        return true;
    }

    private void measureClickCell(int row, int col) {
        if (row >= realRowCount || col >= COL_COUNT){
            Log.d(TAG, "Out of bound");
        }else{
            List<SSDay> ssDays = mMonthDays.get(row);
            if(ssDays != null && ssDays.size() > 0){
                SSDay ssDay = ssDays.get(col);
                dayDrawer.onDayClick(ssDay, this);
            }else{
                Log.d(TAG, "Not found the row's days");
            }
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(widthAttr == ViewGroup.LayoutParams.WRAP_CONTENT){
            mWidth = mDisplayMetrics.widthPixels;
        }else{
            mWidth = getMeasuredWidth();
        }

        if(heightAttr == ViewGroup.LayoutParams.WRAP_CONTENT){
            mHeight = mWidth / COL_COUNT * realRowCount;
        }else{
            mHeight = getMeasuredHeight();
        }

        mDayWidth = mWidth / COL_COUNT;
        mDayHeight = mHeight / realRowCount;

        Log.d(TAG, "mWidth:"+mWidth+" mHeight:"+mHeight+" mDayWidth:"+mDayWidth+" mDayHeight:"+mDayHeight);
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        widthAttr = getLayoutParams().width;
        heightAttr = getLayoutParams().height;
    }

    private void initEditorMode(){
        if(getTag() != null && !TextUtils.isEmpty(String.valueOf(getTag()))){
            String tag = String.valueOf(getTag());
            String[] ym = tag.split("-");
            int year = Integer.parseInt(ym[0]);
            int month = Integer.parseInt(ym[1]);
            CalendarSelector.getInstance().init(new CalendarSelectorConfiguration.Builder(mContext.getApplicationContext()).ssDayDrawer(new DefaultSSDayDrawer(mContext.getApplicationContext())).build());
            setSsMonth(year, month);
        }else{
            CalendarSelector.getInstance().init(new CalendarSelectorConfiguration.Builder(mContext.getApplicationContext()).ssDayDrawer(new DefaultSSDayDrawer(mContext.getApplicationContext())).build());
            setSsMonth(DateUtils.getCurrentYear(), DateUtils.getCurrentMonth());
        }
    }

}
