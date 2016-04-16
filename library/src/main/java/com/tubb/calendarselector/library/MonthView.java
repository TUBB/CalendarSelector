package com.tubb.calendarselector.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.tubb.calendarselector.custom.DayViewHolder;
import com.tubb.calendarselector.custom.DayViewInflater;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tubingbing on 16/1/18.
 */
public class MonthView extends FrameLayout{

    private static final String TAG = "mv";

    public static final int ROW_COUNT = 6;
    public static final int COL_COUNT = 7;

    private boolean neededRelayout = false;
    private boolean drawMonthDay = true;
    private int realRowCount = ROW_COUNT;
    protected Context mContext;
    protected DisplayMetrics mDisplayMetrics;
    // the first day of week (support sunday,monday,saturday)
    private int firstDayOfWeek;
    private OnMonthDayClickListener mMonthDayClickListener;

    private FullDay[][] monthDays = new FullDay[ROW_COUNT][COL_COUNT];
    private DayViewHolder[][] dayViewHolders = new DayViewHolder[ROW_COUNT][COL_COUNT];
    private DayViewInflater dayInflater;
    private SparseArray<DayViewInflater.Decor> horizontalDecors = new SparseArray<>(ROW_COUNT+1);
    private SparseArray<DayViewInflater.Decor> verticalDecors = new SparseArray<>(COL_COUNT+1);

    private int mDefaultWidth;
    private int mDefaultHeight;
    private int dayWidth;
    private int dayHeight;
    private SCMonth scMonth;
    private SCMonth prevMonth;
    private SCMonth nextMonth;

    // the first day of month is which week's day
    private int firstdayOfWeekPosInMonth;

    public MonthView(Context context) {
        super(context);
        throw new RuntimeException("You could't use this constructor ( MonthView(Context context) )");
    }

    public MonthView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MonthView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        mContext = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MonthView, 0, defStyleAttr);
        drawMonthDay = a.getBoolean(R.styleable.MonthView_sc_draw_monthday_only, drawMonthDay);
        firstDayOfWeek = a.getInt(R.styleable.MonthView_sc_firstday_week, SCMonth.SUNDAY_OF_WEEK);
        mDisplayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(mDisplayMetrics);
        mDefaultWidth = mDisplayMetrics.widthPixels;
        mDefaultHeight = mDefaultWidth / 7 * 6;
        dayInflater = new DefaultDayViewInflater(getContext());
        if(isInEditMode()){
            String testMonth = a.getString(R.styleable.MonthView_sc_month);
            String selectedDays = a.getString(R.styleable.MonthView_sc_selected_days);
            initEditorMode(testMonth, selectedDays);
        }
        a.recycle();
    }

    public void setSCMonth(SCMonth scMonth, DayViewInflater dayInflater){
        if(scMonth.getYear() <=0 || scMonth.getMonth() <=0 || scMonth.getMonth() > 12)
            throw new IllegalArgumentException("Invalidate year or month");
        if(dayInflater != null){
            this.dayInflater = dayInflater;
        }
        this.scMonth = scMonth;
        calculateDays();
        if(getChildCount() > 0){
            refresh();
        }else{
            if(!isInEditMode()){
                // wait for measure finish
                post(new Runnable() {
                    @Override
                    public void run() {
                        createDayViews();
                    }
                });
            }else{
                createDayViews();
            }
        }
        // when use in the recyclerview, each item's height may be different, we should requestLayout again
        if(neededRelayout) {
            requestLayout();
            neededRelayout = false;
        }
    }

    private void createDayViews() {

        for (int row = 0; row < ROW_COUNT; row++){
            for (int col = 0; col < COL_COUNT; col++){
                DayViewHolder dayViewHolder = dayInflater.inflateDayView(this);
                View dayView = dayViewHolder.getDayView();
                dayView.setLayoutParams(new ViewGroup.LayoutParams(
                        dayWidth,
                        dayHeight));
                addView(dayView);
                dayViewHolders[row][col] = dayViewHolder;
                drawDays(row, col, dayView);
                dayView.setClickable(true);
                final int clickRow = row;
                final int clickCol = col;
                dayView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        measureClickCell(clickRow, clickCol);
                    }
                });
            }
        }

        for (int row = 0, hCount = ROW_COUNT + 1; row < hCount; row++){
            DayViewInflater.Decor horizontalDecor = dayInflater.inflateHorizontalDecor(this, row, hCount);
            if(horizontalDecor != null && horizontalDecor.getDecorView() != null){
                horizontalDecors.put(row, horizontalDecor);
                addView(horizontalDecor.getDecorView());
            }
        }

        for (int col = 0, vCount = COL_COUNT + 1; col < vCount; col++){
            DayViewInflater.Decor verticalDecor = dayInflater.inflateVerticalDecor(this, col, vCount);
            if(verticalDecor != null && verticalDecor.getDecorView() != null){
                verticalDecors.put(col, verticalDecor);
                addView(verticalDecor.getDecorView());
            }
        }
    }

    private void drawDays(final int row, final int col, View dayView) {
        FullDay fullDay = monthDays[row][col];
        DayViewHolder dayViewHolder = dayViewHolders[row][col];
        boolean isPrevMonthDay = SCDateUtils.isPrevMonthDay(
                scMonth.getYear(), scMonth.getMonth(),
                fullDay.getYear(), fullDay.getMonth());
        boolean isNextMonthDay = SCDateUtils.isNextMonthDay(
                scMonth.getYear(), scMonth.getMonth(),
                fullDay.getYear(), fullDay.getMonth());
        boolean isSelected = getSelectedDays().contains(fullDay);
        if(drawMonthDay){
            if(isPrevMonthDay || isNextMonthDay){
                dayView.setVisibility(View.INVISIBLE);
            }else{
                dayView.setVisibility(View.VISIBLE);
                dayViewHolder.setCurrentMonthDayText(fullDay, isSelected);
            }
        }else{
            dayView.setVisibility(View.VISIBLE);
            if(isPrevMonthDay){
                dayViewHolder.setPrevMonthDayText(fullDay);
            }else if(isNextMonthDay){
                dayViewHolder.setNextMonthDayText(fullDay);
            }else{
                dayViewHolder.setCurrentMonthDayText(fullDay, isSelected);
            }
        }
    }

    private void calculateDays() {

        prevMonth = SCDateUtils.prevMonth(scMonth.getYear(), scMonth.getMonth());
        int dayCountOfPrevMonth = SCDateUtils.getDayCountOfMonth(prevMonth.getYear(), prevMonth.getMonth());
        nextMonth = SCDateUtils.nextMonth(scMonth.getYear(), scMonth.getMonth());
        firstdayOfWeekPosInMonth = SCDateUtils.mapDayOfWeekInMonth(SCDateUtils.getDayOfWeekInMonth(scMonth.getYear(), scMonth.getMonth()), firstDayOfWeek);
//        Log.e(TAG, "firstdayOfWeekPosInMonth:"+firstdayOfWeekPosInMonth);
        int dayCountOfMonth = SCDateUtils.getDayCountOfMonth(scMonth.getYear(), scMonth.getMonth());

        int realRow = 0;
        int day = 1;
        for (int row = 0; row < ROW_COUNT; row++){
            boolean isAllRowEmpty = true;
            for (int col = 1; col <= COL_COUNT; col++){
                int monthPosition = col + row * COL_COUNT;
                if(monthPosition >= firstdayOfWeekPosInMonth
                        && monthPosition < firstdayOfWeekPosInMonth + dayCountOfMonth){ // current month
                    FullDay currentMonthDay = new FullDay(scMonth.getYear(), scMonth.getMonth(), day);
                    monthDays[row][col-1] = currentMonthDay;
                    day++;
                    isAllRowEmpty = false;
                }else if(monthPosition < firstdayOfWeekPosInMonth){ // prev month
                    int prevDay = dayCountOfPrevMonth - (firstdayOfWeekPosInMonth - 1 - monthPosition);
                    FullDay prevMonthDay = new FullDay(prevMonth.getYear(), prevMonth.getMonth(), prevDay);
                    monthDays[row][col-1] = prevMonthDay;
                }else if(monthPosition >= firstdayOfWeekPosInMonth + dayCountOfMonth){ // next month
                    FullDay nextMonthDay = new FullDay(nextMonth.getYear(), nextMonth.getMonth(),
                            monthPosition - (firstdayOfWeekPosInMonth + dayCountOfMonth) + 1);
                    monthDays[row][col-1] = nextMonthDay;
                }
            }
            if(!isAllRowEmpty) realRow++;
        }

        if(drawMonthDay) {
            if(realRowCount != realRow) neededRelayout = true;
            realRowCount = realRow;
        } else{
            // adjust display
            if(firstdayOfWeekPosInMonth == 1
                    && (realRow == (ROW_COUNT - 1) || realRow == (ROW_COUNT - 2))
                    && isFirstRowFullCurrentMonthDays()){
                FullDay[][] tempMonthdays = new FullDay[ROW_COUNT][COL_COUNT];
                FullDay[] ssDays = new FullDay[COL_COUNT];
                tempMonthdays[0] = ssDays;
                for (int monthPosition = COL_COUNT - 1; monthPosition >= 0; monthPosition--){
                    int prevDay = dayCountOfPrevMonth - monthPosition;
                    FullDay prevMonthDay = new FullDay(prevMonth.getYear(), prevMonth.getMonth(), prevDay);
                    ssDays[COL_COUNT - 1 - monthPosition] = prevMonthDay;
                }
                System.arraycopy(monthDays, 0, tempMonthdays, 1, 5);
                monthDays = tempMonthdays;
            }
        }
    }

    private boolean isFirstRowFullCurrentMonthDays() {
        FullDay[] ssDays = monthDays[0];
        for (FullDay day : ssDays){
            if(SCDateUtils.isPrevMonthDay(scMonth.getYear(), scMonth.getMonth(), day.getYear(), day.getMonth())) return false;
        }
        return true;
    }

    List<FullDay> getSelectedDays(){
        return getSCMonth().getSelectedDays();
    }

    public void addSelectedDay(FullDay day){
        getSelectedDays().add(day);
        selectedDaysChanged();
    }

    public void addSelectedDays(List<FullDay> days){
        getSelectedDays().addAll(days);
        selectedDaysChanged();
    }

    public void removeSelectedDay(FullDay day){
        getSelectedDays().remove(day);
        selectedDaysChanged();
    }

    public void clearSelectedDays() {
        getSelectedDays().clear();
        selectedDaysChanged();
    }

    private void selectedDaysChanged(){
        int decorSize = horizontalDecors.size() + verticalDecors.size();
        for (int index = 0, count = getChildCount() - decorSize; index < count; index++){
            View childView = getChildAt(index);
            int row = index / COL_COUNT;
            int col = index - row * COL_COUNT;
            drawDays(row, col, childView);
        }
    }

    /**
     * set the month
     * @param scMonth month obj
     */
    public void setSCMonth(SCMonth scMonth){
        this.setSCMonth(scMonth, null);
    }

    private SCMonth getSCMonth() {
        return scMonth;
    }

    public void setMonthDayClickListener(OnMonthDayClickListener monthDayClickListener) {
        mMonthDayClickListener = monthDayClickListener;
    }

    public int getYear(){
        return getSCMonth().getYear();
    }

    public int getMonth(){
        return getSCMonth().getMonth();
    }

    private void measureClickCell(int row, int col) {
        if (row >= realRowCount || col >= COL_COUNT){
            Log.d(TAG, "Out of bound");
        }else{
            FullDay[] ssDays = monthDays[row];
            if(ssDays != null && ssDays.length > 0){
                FullDay ssDay = ssDays[col];
                if(mMonthDayClickListener != null) {
                    if (SCDateUtils.isMonthDay(scMonth.getYear(), scMonth.getMonth(), ssDay.getYear(), ssDay.getMonth()))
                        mMonthDayClickListener.onMonthDayClick(new FullDay(scMonth.getYear(), scMonth.getMonth(), ssDay.getDay()));
                    else if(SCDateUtils.isPrevMonthDay(scMonth.getYear(), scMonth.getMonth(), ssDay.getYear(), ssDay.getMonth()))
                        mMonthDayClickListener.onMonthDayClick(new FullDay(prevMonth.getYear(), prevMonth.getMonth(), ssDay.getDay()));
                    else if(SCDateUtils.isNextMonthDay(scMonth.getYear(), scMonth.getMonth(), ssDay.getYear(), ssDay.getMonth()))
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

        int width = getMeasurement(widthMeasureSpec, mDefaultWidth);
        int height = getMeasurement(heightMeasureSpec, mDefaultHeight);

        dayWidth = width / COL_COUNT;
        dayHeight = height / realRowCount;
//        Log.d(TAG, "mWidth:"+width+" mHeight:"+height+" dayWidth:"+dayWidth+" dayHeight:"+dayHeight);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        for (int index = 0, count = getChildCount();
             index < count; index++){
            View childView = getChildAt(index);
            int row = index / COL_COUNT;
            int col = index - row * COL_COUNT;
            int l = col * dayWidth;
            int t = row * dayHeight;
            int r = l + dayWidth;
            int b = t + dayHeight;
            childView.layout(l, t,
                    r, b);
        }

        for (int row = 0, hCount = ROW_COUNT + 1; row < hCount; row++){
            DayViewInflater.Decor hDecor = horizontalDecors.get(row);
            if(hDecor != null && hDecor.isShowDecor()){
                View decorView = hDecor.getDecorView();
                if(row == hCount-1){
                    decorView.layout(0, row * dayHeight - decorView.getMeasuredHeight(), getWidth(), row * dayHeight);
                }else{
                    decorView.layout(0, row * dayHeight, getWidth(), row * dayHeight+decorView.getMeasuredHeight());
                }
            }
        }

        for (int col = 0, vCount = COL_COUNT + 1; col < vCount; col++){
            DayViewInflater.Decor vDecor = verticalDecors.get(col);
            if(vDecor != null && vDecor.isShowDecor()){
                View decorView = vDecor.getDecorView();
                if(col == vCount - 1){
                    decorView.layout(col * dayWidth - decorView.getMeasuredWidth(), 0, col * dayWidth, getHeight());
                }else{
                    decorView.layout(col * dayWidth, 0, col * dayWidth + decorView.getMeasuredWidth(), getHeight());
                }
            }
        }

    }

    private int getMeasurement(int measureSpec, int contentSize) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
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
        SCMonth scMonth;
        if(!TextUtils.isEmpty(testMonth)){
            String[] ym = testMonth.split("-");
            int year = Integer.parseInt(ym[0]);
            int month = Integer.parseInt(ym[1]);
            scMonth = new SCMonth(year, month);
        }else{
            scMonth = new SCMonth(SCDateUtils.getCurrentYear(), SCDateUtils.getCurrentMonth());
        }
        if(!TextUtils.isEmpty(selectedDays)){
            String[] days = selectedDays.split(",");
            for (String day:days){
                scMonth.addSelectedDay(new FullDay(scMonth.getYear(), scMonth.getMonth(), Integer.parseInt(day)));
            }
        }
        setSCMonth(scMonth);
    }

    public void refresh() {
        post(new Runnable() {
            @Override
            public void run() {
                selectedDaysChanged();
            }
        });
    }

    /**
     * @return the first day of month is which week's day
     */
    public int getFirstdayOfWeekPosInMonth() {
        return firstdayOfWeekPosInMonth;
    }

    /**
     * @return the first day of week (support sunday,monday,saturday)
     */
    @SCMonth.WeekType
    public int getFirstDayOfWeek() {
        return firstDayOfWeek;
    }

    public void setFirstDayOfWeek(@SCMonth.WeekType int firstDayOfWeek) {
        this.firstDayOfWeek = firstDayOfWeek;
    }

    public interface OnMonthDayClickListener{
        void onMonthDayClick(FullDay day);
    }

}
