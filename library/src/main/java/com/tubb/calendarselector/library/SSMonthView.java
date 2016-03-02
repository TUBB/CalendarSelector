package com.tubb.calendarselector.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;

import java.util.ArrayList;
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
    private boolean neededLayout = false;
    protected Context mContext;
    protected DisplayMetrics mDisplayMetrics;
    private float mDownX;
    private float mDownY;
    private int mTouchSlop;
    private int todayColor;
    private int prevMonthDayColor;
    private int nextMonthDayColor;
    private int normalDayColor;
    private int selectedDayColor;
    private int selectedDayCircleColor;
    private int daySize;
    private int firstDayOfWeek;
    private OnMonthDayClickListener mMonthDayClickListener;

    private List<List<FullDay>> mMonthDays;
    private SSDayDrawer dayDrawer;

    private int mWidth;
    private int mHeight;
    private int mDayWidth;
    private int mDayHeight;
    private SSMonth ssMonth;
    private SSMonth prevMonth;
    private SSMonth nextMonth;

    public SSMonthView(Context context) {
        super(context);
        throw new RuntimeException("You could't use this constructor ( MonthView(Context context) )");
    }

    public SSMonthView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SSMonthView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setSaveEnabled(true);
        mContext = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SSMonthView, 0, defStyleAttr);
        mDrawMonthDay = a.getBoolean(R.styleable.SSMonthView_draw_monthday, false);
        todayColor = a.getColor(R.styleable.SSMonthView_today_color, ContextCompat.getColor(mContext, R.color.c_ff6666));
        prevMonthDayColor = a.getColor(R.styleable.SSMonthView_prevmonthday_color, ContextCompat.getColor(mContext, R.color.c_999999));
        nextMonthDayColor = a.getColor(R.styleable.SSMonthView_nextmonthday_color, ContextCompat.getColor(mContext, R.color.c_999999));
        normalDayColor = a.getColor(R.styleable.SSMonthView_normalday_color, ContextCompat.getColor(mContext, R.color.c_000000));
        selectedDayColor = a.getColor(R.styleable.SSMonthView_selectedday_color, ContextCompat.getColor(mContext, R.color.c_ffffff));
        selectedDayCircleColor = a.getColor(R.styleable.SSMonthView_selectedday_circle_color, ContextCompat.getColor(mContext, R.color.c_33ccff));
        daySize = a.getDimensionPixelSize(R.styleable.SSMonthView_day_size, getResources().getDimensionPixelSize(R.dimen.t_16));
        firstDayOfWeek = a.getInt(R.styleable.SSMonthView_firstDayOfWeek, SSMonth.SUNDAY_OF_WEEK);
        if(isInEditMode()){
            String testMonth = a.getString(R.styleable.SSMonthView_month);
            String selectedDays = a.getString(R.styleable.SSMonthView_selected_days);
            initEditorMode(testMonth, selectedDays);
        }
        a.recycle();
        mDisplayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(mDisplayMetrics);
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        dayDrawer = new DefaultSSDayDrawer(mContext);
        dayDrawer.init(this);
    }

    public void setSsMonth(SSMonth ssMonth, SSDayDrawer dayDrawer){
        if(ssMonth.getYear() <=0 || ssMonth.getMonth() <=0 || ssMonth.getMonth() > 12)
            throw new IllegalArgumentException("Invalidate year or month");
        if(dayDrawer != null){
            this.dayDrawer = dayDrawer;
            this.dayDrawer.init(this);
        }
        this.ssMonth = ssMonth;
        calculateDays();
        if(neededLayout) requestLayout();
        else invalidate();
        neededLayout = false;
    }

    /**
     * set the month
     * @param ssMonth month obj
     */
    public void setSsMonth(SSMonth ssMonth){
        this.setSsMonth(ssMonth, null);
    }

    public SSMonth getSsMonth() {
        return ssMonth;
    }

    public void setMonthDayClickListener(OnMonthDayClickListener monthDayClickListener) {
        mMonthDayClickListener = monthDayClickListener;
    }

    private void calculateDays() {

        prevMonth = DateUtils.prevMonth(ssMonth.getYear(), ssMonth.getMonth());
        int dayCountOfPrevMonth = DateUtils.getDayCountOfMonth(prevMonth.getYear(), prevMonth.getMonth());
        nextMonth = DateUtils.nextMonth(ssMonth.getYear(), ssMonth.getMonth());

        int dayOfWeekInMonth = DateUtils.mapDayOfWeekInMonth(DateUtils.getDayOfWeekInMonth(ssMonth.getYear(), ssMonth.getMonth()), firstDayOfWeek);
//        Log.d(TAG, ssMonth.toString()+" dayOfWeekInMonth:"+dayOfWeekInMonth);
        int dayCountOfMonth = DateUtils.getDayCountOfMonth(ssMonth.getYear(), ssMonth.getMonth());

        mMonthDays = new ArrayList<>(ROW_COUNT);

        int realRow = 0;
        int day = 1;
        for (int row = 0; row < ROW_COUNT; row++){
            boolean isAllRowEmpty = true;
            ArrayList<FullDay> days = new ArrayList<>(COL_COUNT);
            for (int col = 1; col <= COL_COUNT; col++){
                int monthPosition = col + row * COL_COUNT;
                if(monthPosition >= dayOfWeekInMonth
                        && monthPosition < dayOfWeekInMonth + dayCountOfMonth){ // current month
                    FullDay currentMonthDay = new FullDay(ssMonth.getYear(), ssMonth.getMonth(), day);
                    days.add(currentMonthDay);
                    day++;
                    isAllRowEmpty = false;
                }else if(monthPosition < dayOfWeekInMonth){ // prev month
                    int prevDay = dayCountOfPrevMonth - (dayOfWeekInMonth - 1 - monthPosition);
                    FullDay prevMonthDay = new FullDay(prevMonth.getYear(), prevMonth.getMonth(), prevDay);
                    days.add(prevMonthDay);
                }else if(monthPosition >= dayOfWeekInMonth + dayCountOfMonth){ // next month
                    FullDay nextMonthDay = new FullDay(nextMonth.getYear(), nextMonth.getMonth(),
                            monthPosition - (dayOfWeekInMonth + dayCountOfMonth) + 1);
                    days.add(nextMonthDay);
                }
            }
            mMonthDays.add(days);
            if(!isAllRowEmpty) realRow++;
        }

        if(mDrawMonthDay) {
            if(realRowCount != realRow) neededLayout = true;
            realRowCount = realRow;
        } else{
            // adjust display
            if(dayOfWeekInMonth == 1
                    && (realRow == (ROW_COUNT - 1) || realRow == (ROW_COUNT - 2))
                    && isFirstRowFullCurrentMonthDays()){

                mMonthDays.remove(mMonthDays.size() - 1);
                List<FullDay> ssDays = new ArrayList<>(COL_COUNT);
                for (int monthPosition = COL_COUNT - 1; monthPosition >= 0; monthPosition--){
                    int prevDay = dayCountOfPrevMonth - monthPosition;
                    FullDay prevMonthDay = new FullDay(prevMonth.getYear(), prevMonth.getMonth(), prevDay);
                    ssDays.add(prevMonthDay);
                }
                mMonthDays.add(0, ssDays);
            }
        }
//        Log.d(TAG, ssMonth.getYear()+"-"+ssMonth.getMonth()+" realRowCount:"+realRowCount);
    }

    private boolean isFirstRowFullCurrentMonthDays() {
        List<FullDay> ssDays = mMonthDays.get(0);
        for (FullDay day : ssDays){
            if(DateUtils.isPrevMonthDay(ssMonth.getYear(), ssMonth.getMonth(), day.getYear(), day.getMonth())) return false;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(mWidth == 0 || mHeight == 0)
            throw new RuntimeException("the month view width or height is not correct");
        if(this.ssMonth == null)
            throw new RuntimeException("the ssMonth property must be set");
        for (int row = 0; row < mMonthDays.size(); row++){
            List<FullDay> ssDays = mMonthDays.get(row);
            for (int col = 0; col < ssDays.size(); col++){
                FullDay ssDay = ssDays.get(col);
                if(mDrawMonthDay){
                    if(DateUtils.isMonthDay(ssMonth.getYear(), ssMonth.getMonth(), ssDay.getYear(), ssDay.getMonth()))
                        dayDrawer.draw(ssMonth, ssDay, canvas, row, col, mDayWidth, mDayHeight);
                }else{
                    dayDrawer.draw(ssMonth, ssDay, canvas, row, col, mDayWidth, mDayHeight);
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
            List<FullDay> ssDays = mMonthDays.get(row);
            if(ssDays != null && ssDays.size() > 0){
                FullDay ssDay = ssDays.get(col);
                if(mMonthDayClickListener != null) {
                    if (DateUtils.isMonthDay(ssMonth.getYear(), ssMonth.getMonth(), ssDay.getYear(), ssDay.getMonth()))
                        mMonthDayClickListener.onMonthDayClick(new FullDay(ssMonth.getYear(), ssMonth.getMonth(), ssDay.getDay()));
                    else if(DateUtils.isPrevMonthDay(ssMonth.getYear(), ssMonth.getMonth(), ssDay.getYear(), ssDay.getMonth()))
                        mMonthDayClickListener.onMonthDayClick(new FullDay(prevMonth.getYear(), prevMonth.getMonth(), ssDay.getDay()));
                    else if(DateUtils.isNextMonthDay(ssMonth.getYear(), ssMonth.getMonth(), ssDay.getYear(), ssDay.getMonth()))
                        mMonthDayClickListener.onMonthDayClick(new FullDay(nextMonth.getYear(), nextMonth.getMonth(), ssDay.getDay()));
                }
            }else{
                Log.d(TAG, "Not found the row's days");
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = getMeasurement(widthMeasureSpec, getMeasuredWidth());
        mHeight = getMeasurement(heightMeasureSpec, getMeasuredHeight());

        mDayWidth = mWidth / COL_COUNT;
        mDayHeight = mHeight / realRowCount;
//        Log.d(TAG, ssMonth.getYear()+"-"+ssMonth.getMonth()+" onMeasure");
//        Log.d(TAG, "mWidth:"+mWidth+" mHeight:"+mHeight+" mDayWidth:"+mDayWidth+" mDayHeight:"+mDayHeight);
        setMeasuredDimension(mWidth, mHeight);
    }

    private int getMeasurement(int measureSpec, int contentSize) {
        int specMode = View.MeasureSpec.getMode(measureSpec);
        int specSize = View.MeasureSpec.getSize(measureSpec);
        int resultSize = 0;
        switch (specMode){
            case MeasureSpec.EXACTLY:
                resultSize = specSize;
                break;
            case MeasureSpec.AT_MOST:
                resultSize = Math.min(specSize, contentSize);
                break;
            case MeasureSpec.UNSPECIFIED:
                resultSize = contentSize;
                break;
        }
        return resultSize;
    }

    private void initEditorMode(String testMonth, String selectedDays){
        SSMonth ssMonth;
        if(!TextUtils.isEmpty(testMonth)){
            String[] ym = testMonth.split("-");
            int year = Integer.parseInt(ym[0]);
            int month = Integer.parseInt(ym[1]);
            ssMonth = new SSMonth(year, month);
        }else{
            ssMonth = new SSMonth(DateUtils.getCurrentYear(), DateUtils.getCurrentMonth());
        }
        if(!TextUtils.isEmpty(selectedDays)){
            String[] days = selectedDays.split(",");
            for (String day:days){
                ssMonth.addSelectedDay(new FullDay(ssMonth.getYear(), ssMonth.getMonth(), Integer.parseInt(day)));
            }
        }
        setSsMonth(ssMonth);
    }

    public int getNormalDayColor() {
        return normalDayColor;
    }

    public int getPrevMonthDayColor() {
        return prevMonthDayColor;
    }

    public int getNextMonthDayColor() {
        return nextMonthDayColor;
    }

    public int getTodayColor() {
        return todayColor;
    }

    public int getDaySize() {
        return daySize;
    }

    public int getSelectedDayColor() {
        return selectedDayColor;
    }

    public int getSelectedDayCircleColor() {
        return selectedDayCircleColor;
    }

    public interface OnMonthDayClickListener{
        void onMonthDayClick(FullDay day);
    }

}
