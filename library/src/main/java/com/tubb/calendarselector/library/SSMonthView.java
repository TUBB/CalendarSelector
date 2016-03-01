package com.tubb.calendarselector.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Parcel;
import android.os.Parcelable;
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
import java.util.Set;

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

    private List<List<SSDay>> mMonthDays;
    private SSDayDrawer dayDrawer;

    private int mWidth;
    private int mHeight;
    private int mDayWidth;
    private int mDayHeight;
    private SSMonth ssMonth;

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
        if(mMonthDays == null)
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

        SSMonth prevMonth = DateUtils.prevMonth(ssMonth.getYear(), ssMonth.getMonth());
        int dayCountOfPrevMonth = DateUtils.getDayCountOfMonth(prevMonth.getYear(), prevMonth.getMonth());
        SSMonth nextMonth = DateUtils.nextMonth(ssMonth.getYear(), ssMonth.getMonth());

        int dayOfWeekInMonth = DateUtils.mapDayOfWeekInMonth(DateUtils.getDayOfWeekInMonth(ssMonth.getYear(), ssMonth.getMonth()), firstDayOfWeek);
//        Log.d(TAG, ssMonth.toString()+" dayOfWeekInMonth:"+dayOfWeekInMonth);
        int dayCountOfMonth = DateUtils.getDayCountOfMonth(ssMonth.getYear(), ssMonth.getMonth());

        mMonthDays = new ArrayList<>(ROW_COUNT);

        int realRow = 0;
        int day = 1;
        for (int row = 0; row < ROW_COUNT; row++){
            boolean isAllRowEmpty = true;
            ArrayList<SSDay> days = new ArrayList<>(COL_COUNT);
            for (int col = 1; col <= COL_COUNT; col++){
                int monthPosition = col + row * COL_COUNT;
                if(monthPosition >= dayOfWeekInMonth
                        && monthPosition < dayOfWeekInMonth + dayCountOfMonth){ // current month
                    SSDay currentMonthDay = new SSDay(SSDay.CURRENT_MONTH_DAY, ssMonth, day);
                    if(DateUtils.isToday(ssMonth.getYear(), ssMonth.getMonth(), day)){
                        currentMonthDay.setDayType(SSDay.TODAY);
                    }
                    days.add(currentMonthDay);
                    day++;
                    isAllRowEmpty = false;
                }else if(monthPosition < dayOfWeekInMonth){ // prev month
                    int prevDay = dayCountOfPrevMonth - (dayOfWeekInMonth - 1 - monthPosition);
                    SSDay prevMonthDay = new SSDay(SSDay.PRE_MONTH_DAY, prevMonth, prevDay);
                    days.add(prevMonthDay);
                }else if(monthPosition >= dayOfWeekInMonth + dayCountOfMonth){ // next month
                    SSDay nextMonthDay = new SSDay(SSDay.NEXT_MONTH_DAY, nextMonth,
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
                List<SSDay> ssDays = new ArrayList<>(COL_COUNT);
                for (int monthPosition = COL_COUNT - 1; monthPosition >= 0; monthPosition--){
                    int prevDay = dayCountOfPrevMonth - monthPosition;
                    SSDay prevMonthDay = new SSDay(SSDay.PRE_MONTH_DAY, prevMonth, prevDay);
                    ssDays.add(prevMonthDay);
                }
                mMonthDays.add(0, ssDays);
            }
        }
//        Log.d(TAG, ssMonth.getYear()+"-"+ssMonth.getMonth()+" realRowCount:"+realRowCount);
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
        if(mWidth == 0 || mHeight == 0)
            throw new RuntimeException("the month view width or height is not correct");
        if(this.ssMonth == null)
            throw new RuntimeException("the ssMonth property must be set");
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
                if(mMonthDayClickListener != null) mMonthDayClickListener.onMonthDayClick(ssDay);
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
                ssMonth.addSelectedDay(new SSDay(SSDay.CURRENT_MONTH_DAY, ssMonth, Integer.parseInt(day)));
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
        void onMonthDayClick(SSDay ssDay);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Log.d(TAG, "onSaveInstanceState subview...");
        Parcelable superParcelable = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superParcelable);
        savedState.monthDays = mMonthDays;
        savedState.realRowCount = realRowCount;
        savedState.selectedDays = ssMonth.getSelectedDays();
        return savedState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Log.d(TAG, "onRestoreInstanceState subview...");
        //begin boilerplate code so parent classes can restore state
        if(!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState ss = (SavedState)state;
        super.onRestoreInstanceState(ss.getSuperState());
        //end

        this.mMonthDays = ss.monthDays;
        this.realRowCount = ss.realRowCount;
        this.ssMonth.setSelectedDays(ss.selectedDays);

    }

    static class SavedState extends BaseSavedState {

        List<List<SSDay>> monthDays;
        List<SSDay> selectedDays;
        int realRowCount;

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeList(this.monthDays);
            dest.writeList(selectedDays);
            dest.writeInt(this.realRowCount);
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        protected SavedState(Parcel in) {
            super(in);
            this.monthDays = new ArrayList<>(ROW_COUNT);
            in.readList(this.monthDays, List.class.getClassLoader());
            this.selectedDays = new ArrayList<>(5);
            in.readList(this.selectedDays, List.class.getClassLoader());
            this.realRowCount = in.readInt();
        }

        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel source) {
                return new SavedState(source);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

}
