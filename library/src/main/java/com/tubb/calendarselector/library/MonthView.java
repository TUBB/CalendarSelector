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
public class MonthView extends View{

    private static final String TAG = "mv";

    public static final int ROW_COUNT = 6;
    public static final int COL_COUNT = 7;

    private boolean drawMonthDay = false;
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

    private List<List<FullDay>> monthDays;
    private DayDrawer dayDrawer;

    private int mWidth;
    private int mHeight;
    private int dayWidth;
    private int dayHeight;
    private SCMonth SCMonth;
    private SCMonth prevMonth;
    private SCMonth nextMonth;

    public MonthView(Context context) {
        super(context);
        throw new RuntimeException("You could't use this constructor ( MonthView(Context context) )");
    }

    public MonthView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MonthView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setSaveEnabled(true);
        mContext = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MonthView, 0, defStyleAttr);
        drawMonthDay = a.getBoolean(R.styleable.MonthView_sc_draw_monthday, false);
        todayColor = a.getColor(R.styleable.MonthView_sc_today_color, ContextCompat.getColor(mContext, R.color.c_ff6666));
        prevMonthDayColor = a.getColor(R.styleable.MonthView_sc_prevmonthday_color, ContextCompat.getColor(mContext, R.color.c_999999));
        nextMonthDayColor = a.getColor(R.styleable.MonthView_sc_nextmonthday_color, ContextCompat.getColor(mContext, R.color.c_999999));
        normalDayColor = a.getColor(R.styleable.MonthView_sc_normalday_color, ContextCompat.getColor(mContext, R.color.c_000000));
        selectedDayColor = a.getColor(R.styleable.MonthView_sc_selectedday_color, ContextCompat.getColor(mContext, R.color.c_ffffff));
        selectedDayCircleColor = a.getColor(R.styleable.MonthView_sc_selectedday_bgcolor, ContextCompat.getColor(mContext, R.color.c_33ccff));
        daySize = a.getDimensionPixelSize(R.styleable.MonthView_sc_day_textsize, getResources().getDimensionPixelSize(R.dimen.t_16));
        firstDayOfWeek = a.getInt(R.styleable.MonthView_sc_firstday_week, SCMonth.SUNDAY_OF_WEEK);
        if(isInEditMode()){
            String testMonth = a.getString(R.styleable.MonthView_sc_month);
            String selectedDays = a.getString(R.styleable.MonthView_sc_selected_days);
            initEditorMode(testMonth, selectedDays);
        }
        a.recycle();
        mDisplayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(mDisplayMetrics);
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        dayDrawer = new DefaultDayDrawer(mContext);
        dayDrawer.init(this);
    }

    public void setSsMonth(SCMonth SCMonth, DayDrawer dayDrawer){
        if(SCMonth.getYear() <=0 || SCMonth.getMonth() <=0 || SCMonth.getMonth() > 12)
            throw new IllegalArgumentException("Invalidate year or month");
        if(dayDrawer != null){
            this.dayDrawer = dayDrawer;
            this.dayDrawer.init(this);
        }
        this.SCMonth = SCMonth;
        calculateDays();
        if(neededLayout) requestLayout();
        else invalidate();
        neededLayout = false;
    }

    /**
     * set the month
     * @param SCMonth month obj
     */
    public void setSCMonth(SCMonth SCMonth){
        this.setSsMonth(SCMonth, null);
    }

    public SCMonth getSCMonth() {
        return SCMonth;
    }

    public void setMonthDayClickListener(OnMonthDayClickListener monthDayClickListener) {
        mMonthDayClickListener = monthDayClickListener;
    }

    private void calculateDays() {

        prevMonth = SCDateUtils.prevMonth(SCMonth.getYear(), SCMonth.getMonth());
        int dayCountOfPrevMonth = SCDateUtils.getDayCountOfMonth(prevMonth.getYear(), prevMonth.getMonth());
        nextMonth = SCDateUtils.nextMonth(SCMonth.getYear(), SCMonth.getMonth());

        int dayOfWeekInMonth = SCDateUtils.mapDayOfWeekInMonth(SCDateUtils.getDayOfWeekInMonth(SCMonth.getYear(), SCMonth.getMonth()), firstDayOfWeek);
//        Log.d(TAG, SCMonth.toString()+" dayOfWeekInMonth:"+dayOfWeekInMonth);
        int dayCountOfMonth = SCDateUtils.getDayCountOfMonth(SCMonth.getYear(), SCMonth.getMonth());

        monthDays = new ArrayList<>(ROW_COUNT);

        int realRow = 0;
        int day = 1;
        for (int row = 0; row < ROW_COUNT; row++){
            boolean isAllRowEmpty = true;
            ArrayList<FullDay> days = new ArrayList<>(COL_COUNT);
            for (int col = 1; col <= COL_COUNT; col++){
                int monthPosition = col + row * COL_COUNT;
                if(monthPosition >= dayOfWeekInMonth
                        && monthPosition < dayOfWeekInMonth + dayCountOfMonth){ // current month
                    FullDay currentMonthDay = new FullDay(SCMonth.getYear(), SCMonth.getMonth(), day);
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
            monthDays.add(days);
            if(!isAllRowEmpty) realRow++;
        }

        if(drawMonthDay) {
            if(realRowCount != realRow) neededLayout = true;
            realRowCount = realRow;
        } else{
            // adjust display
            if(dayOfWeekInMonth == 1
                    && (realRow == (ROW_COUNT - 1) || realRow == (ROW_COUNT - 2))
                    && isFirstRowFullCurrentMonthDays()){

                monthDays.remove(monthDays.size() - 1);
                List<FullDay> ssDays = new ArrayList<>(COL_COUNT);
                for (int monthPosition = COL_COUNT - 1; monthPosition >= 0; monthPosition--){
                    int prevDay = dayCountOfPrevMonth - monthPosition;
                    FullDay prevMonthDay = new FullDay(prevMonth.getYear(), prevMonth.getMonth(), prevDay);
                    ssDays.add(prevMonthDay);
                }
                monthDays.add(0, ssDays);
            }
        }
//        Log.d(TAG, SCMonth.getYear()+"-"+SCMonth.getMonth()+" realRowCount:"+realRowCount);
    }

    private boolean isFirstRowFullCurrentMonthDays() {
        List<FullDay> ssDays = monthDays.get(0);
        for (FullDay day : ssDays){
            if(SCDateUtils.isPrevMonthDay(SCMonth.getYear(), SCMonth.getMonth(), day.getYear(), day.getMonth())) return false;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(mWidth == 0 || mHeight == 0)
            throw new RuntimeException("the month view width or height is not correct");
        if(this.SCMonth == null)
            throw new RuntimeException("the SCMonth property must be set");
        dayDrawer.draw(canvas);
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
                    int col = (int) (mDownX / dayWidth);
                    int row = (int) (mDownY / dayHeight);
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
            List<FullDay> ssDays = monthDays.get(row);
            if(ssDays != null && ssDays.size() > 0){
                FullDay ssDay = ssDays.get(col);
                if(mMonthDayClickListener != null) {
                    if (SCDateUtils.isMonthDay(SCMonth.getYear(), SCMonth.getMonth(), ssDay.getYear(), ssDay.getMonth()))
                        mMonthDayClickListener.onMonthDayClick(new FullDay(SCMonth.getYear(), SCMonth.getMonth(), ssDay.getDay()));
                    else if(SCDateUtils.isPrevMonthDay(SCMonth.getYear(), SCMonth.getMonth(), ssDay.getYear(), ssDay.getMonth()))
                        mMonthDayClickListener.onMonthDayClick(new FullDay(prevMonth.getYear(), prevMonth.getMonth(), ssDay.getDay()));
                    else if(SCDateUtils.isNextMonthDay(SCMonth.getYear(), SCMonth.getMonth(), ssDay.getYear(), ssDay.getMonth()))
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

        dayWidth = mWidth / COL_COUNT;
        dayHeight = mHeight / realRowCount;
//        Log.d(TAG, SCMonth.getYear()+"-"+SCMonth.getMonth()+" onMeasure");
//        Log.d(TAG, "mWidth:"+mWidth+" mHeight:"+mHeight+" dayWidth:"+dayWidth+" dayHeight:"+dayHeight);
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
        SCMonth SCMonth;
        if(!TextUtils.isEmpty(testMonth)){
            String[] ym = testMonth.split("-");
            int year = Integer.parseInt(ym[0]);
            int month = Integer.parseInt(ym[1]);
            SCMonth = new SCMonth(year, month);
        }else{
            SCMonth = new SCMonth(SCDateUtils.getCurrentYear(), SCDateUtils.getCurrentMonth());
        }
        if(!TextUtils.isEmpty(selectedDays)){
            String[] days = selectedDays.split(",");
            for (String day:days){
                SCMonth.addSelectedDay(new FullDay(SCMonth.getYear(), SCMonth.getMonth(), Integer.parseInt(day)));
            }
        }
        setSCMonth(SCMonth);
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

    public boolean isDrawMonthDay() {
        return drawMonthDay;
    }

    public List<List<FullDay>> getMonthDays() {
        return monthDays;
    }

    public int getDayWidth() {
        return dayWidth;
    }

    public int getDayHeight() {
        return dayHeight;
    }

    public void addSelectedDay(FullDay selectedDay){
        getSCMonth().addSelectedDay(selectedDay);
    }

    public interface OnMonthDayClickListener{
        void onMonthDayClick(FullDay day);
    }

}
