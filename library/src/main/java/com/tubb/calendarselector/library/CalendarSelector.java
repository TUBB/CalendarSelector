package com.tubb.calendarselector.library;

import android.os.Parcel;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by tubingbing on 16/2/4.
 */
public class CalendarSelector extends SingleMonthSelector {

    private static final String TAG = "mv";
    private static final int LISTENER_HOLDER_TAG_KEY = "LISTENER_HOLDER_TAG_KEY".hashCode();
    protected List<SCMonth> dataList;

    public CalendarSelector(List<SCMonth> dataList, @Mode int mode){
        super(null, mode);
        this.dataList = dataList;
    }

    @Override
    public void addSelectedSegment(FullDay startDay, FullDay endDay){
        if (mode == INTERVAL) throw new IllegalArgumentException("Just used with SEGMENT mode!!!");
        if (startDay == null || endDay == null) throw new IllegalArgumentException("startDay or endDay can't be null");
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        startCalendar.set(Calendar.YEAR, startDay.getYear());
        startCalendar.set(Calendar.MONTH, startDay.getMonth() - 1);
        startCalendar.set(Calendar.DAY_OF_MONTH, startDay.getDay());
        endCalendar.set(Calendar.YEAR, endDay.getYear());
        endCalendar.set(Calendar.MONTH, endDay.getMonth() - 1);
        endCalendar.set(Calendar.DAY_OF_MONTH, endDay.getDay());
        if (startCalendar.getTime().getTime() > endCalendar.getTime().getTime())
            throw new IllegalArgumentException("startDay > endDay not support");
        int startDayPosition = dataList.indexOf(new SCMonth(startDay.getYear(), startDay.getMonth()));
        int endDayPosition = dataList.indexOf(new SCMonth(endDay.getYear(), endDay.getMonth()));
        startSelectedRecord = new SelectedRecord();
        startSelectedRecord.position = startDayPosition;
        startSelectedRecord.day = startDay;

        endSelectedRecord = new SelectedRecord();
        endSelectedRecord.position = endDayPosition;
        endSelectedRecord.day = endDay;

        segmentMonthSelected(null, false);
    }

    @Override
    public void addSelectedInterval(FullDay day){
        if (mode == SEGMENT) throw new IllegalArgumentException("Just used with INTERVAL mode!!!");
        if (day == null) throw new IllegalArgumentException("day can't be null!!!");
        addSelectedDayToMonth(day);
    }

    @Override
    public void addSelectedInterval(List<FullDay> selectedDays){
        if (mode == SEGMENT) throw new IllegalArgumentException("Just used with INTERVAL mode!!!");
        if (selectedDays == null) throw new IllegalArgumentException("selectedDays can't be null!!!");
        for (FullDay day : selectedDays) {
            addSelectedDayToMonth(day);
        }
    }

    @Override
    protected void addSelectedDayToMonth(FullDay day) {
        SCMonth comparedMonth = new SCMonth(day.getYear(), day.getMonth());
        if (dataList.contains(comparedMonth)){
            SCMonth sourceMonth = dataList.get(dataList.indexOf(comparedMonth));
            sDays.add(day);
            sourceMonth.addSelectedDay(day);
        }else {
            throw new IllegalArgumentException("The day not belong to any month!!!");
        }
    }

    public void bind(final ViewGroup container, final MonthView monthView, final int position){
        if(container == null || monthView == null || position < 0)
            throw new IllegalArgumentException("Invalid params of bind(final ViewGroup container, final SSMonthView monthView, final int position) method");
        if(this.mode == INTERVAL && this.intervalSelectListener == null)
            throw new IllegalArgumentException("Please set IntervalSelectListener for Mode.INTERVAL mode");
        if(this.mode == SEGMENT && this.segmentSelectListener == null)
            throw new IllegalArgumentException("Please set SegmentSelectListener for Mode.SEGMENT mode");
        if(container instanceof ListView) throw new IllegalArgumentException("Not support ListView yet");
        ListenerHolder listenerHolder = (ListenerHolder) monthView.getTag(LISTENER_HOLDER_TAG_KEY);
        if(listenerHolder == null){
            listenerHolder = new ListenerHolder();
            monthView.setTag(LISTENER_HOLDER_TAG_KEY, listenerHolder);
        }
        listenerHolder.container = container;
        listenerHolder.monthView = monthView;
        listenerHolder.position = position;
        monthView.setMonthDayClickListener(listenerHolder);
    }

    class ListenerHolder implements MonthView.OnMonthDayClickListener{

        ViewGroup container;
        MonthView monthView;
        int position;

        @Override
        public void onMonthDayClick(FullDay day) {
            if(!SCDateUtils.isMonthDay(monthView.getYear(), monthView.getMonth(),
                    day.getYear(), day.getMonth()))
                return;
            switch (mode){
                case INTERVAL:
                    intervalSelect(monthView, day);
                    break;
                case SEGMENT:
                    segmentSelect(container, monthView, day, position);
                    break;
            }
        }
    }

    private void segmentSelect(ViewGroup container, MonthView monthView, FullDay ssDay, int position) {

        if(segmentSelectListener.onInterceptSelect(ssDay)) return;

        if(!startSelectedRecord.isRecord() && !endSelectedRecord.isRecord()){ // init status
            startSelectedRecord.position = position;
            startSelectedRecord.day = ssDay;
            monthView.addSelectedDay(ssDay);
        }else if(startSelectedRecord.isRecord() && !endSelectedRecord.isRecord()){ // start day is ok, but end day not
            if(startSelectedRecord.position < position){ // click later month
                if(segmentSelectListener.onInterceptSelect(startSelectedRecord.day, ssDay)) return;
                endSelectedRecord.position = position;
                endSelectedRecord.day = ssDay;
                segmentMonthSelected(container, true);
            }else if(startSelectedRecord.position > position){ // click before month
                if(segmentSelectListener.onInterceptSelect(ssDay, startSelectedRecord.day)) return;
                endSelectedRecord.position = startSelectedRecord.position;
                endSelectedRecord.day = startSelectedRecord.day;
                startSelectedRecord.position = position;
                startSelectedRecord.day = ssDay;
                segmentMonthSelected(container, true);
            }else{ // click the same month
                if(startSelectedRecord.day.getDay() != ssDay.getDay()){
                    if(startSelectedRecord.day.getDay() < ssDay.getDay()){
                        if(segmentSelectListener.onInterceptSelect(startSelectedRecord.day, ssDay)) return;
                        for (int day = startSelectedRecord.day.getDay(); day <= ssDay.getDay(); day++){
                            monthView.addSelectedDay(new FullDay(monthView.getYear(), monthView.getMonth(), day));
                        }
                        endSelectedRecord.position = position;
                        endSelectedRecord.day = ssDay;
                    }else if(startSelectedRecord.day.getDay() > ssDay.getDay()){
                        if(segmentSelectListener.onInterceptSelect(ssDay, startSelectedRecord.day)) return;
                        for (int day = ssDay.getDay(); day <= startSelectedRecord.day.getDay(); day++){
                            monthView.addSelectedDay(new FullDay(monthView.getYear(), monthView.getMonth(), day));
                        }
                        endSelectedRecord.position = position;
                        endSelectedRecord.day = startSelectedRecord.day;
                        startSelectedRecord.day = ssDay;
                    }
                    monthView.invalidate();
                    segmentSelectListener.onSegmentSelect(startSelectedRecord.day, endSelectedRecord.day);
                }else{
                    // selected the same day when the end day is not selected
                    segmentSelectListener.selectedSameDay(ssDay);
                    monthView.clearSelectedDays();
                    startSelectedRecord.reset();
                    endSelectedRecord.reset();
                }
            }

        }else if(startSelectedRecord.isRecord() && endSelectedRecord.isRecord()){ // start day and end day is ok
            dataList.get(startSelectedRecord.position).getSelectedDays().clear();
            invalidate(container, startSelectedRecord.position);

            dataList.get(endSelectedRecord.position).getSelectedDays().clear();
            invalidate(container, endSelectedRecord.position);

            int startSelectedPosition = startSelectedRecord.position;
            int endSelectedPosition = endSelectedRecord.position;

            if(endSelectedPosition - startSelectedPosition > 1){
                do {
                    startSelectedPosition++;
                    dataList.get(startSelectedPosition).getSelectedDays().clear();
                    invalidate(container, startSelectedPosition);
                }while (startSelectedPosition < endSelectedPosition);
            }

            startSelectedRecord.position = position;
            startSelectedRecord.day = ssDay;
            dataList.get(startSelectedRecord.position).addSelectedDay(startSelectedRecord.day);
            invalidate(container, position);

            endSelectedRecord.reset();
        }
    }

    private void invalidate(ViewGroup container, int position){
        if(position >= 0) {
            if (container instanceof RecyclerView){
                RecyclerView rv = (RecyclerView)container;
                rv.getAdapter().notifyItemChanged(position);
            }else if (container instanceof ListView){
                Log.e(TAG, "The ListView not support yet!!!");
            }else {
                View childView = container.getChildAt(position);
                List<View> unvisited = new ArrayList<>();
                unvisited.add(childView);
                while (!unvisited.isEmpty()) {
                    View child = unvisited.remove(0);
                    if (!(child instanceof ViewGroup)) {
                        continue;
                    }
                    ViewGroup group = (ViewGroup) child;
                    if(group instanceof MonthView){
                        MonthView monthView = (MonthView) group;
                        monthView.refresh();
                        break;
                    }
                    final int childCount = group.getChildCount();
                    for (int i=0; i<childCount; i++) unvisited.add(group.getChildAt(i));
                }
            }
        }else {
            throw new IllegalArgumentException("Invalid position!!!");
        }
    }

    private void segmentMonthSelected(ViewGroup container, boolean shouldRefreshView) {

        if (container == null
                && !shouldRefreshView
                && startSelectedRecord.position == endSelectedRecord.position) {
            SCMonth targetMonth = dataList.get(startSelectedRecord.position);
            if(startSelectedRecord.day.getDay() != endSelectedRecord.day.getDay()){
                if(startSelectedRecord.day.getDay() < endSelectedRecord.day.getDay()){
                    for (int day = startSelectedRecord.day.getDay(); day <= endSelectedRecord.day.getDay(); day++){
                        targetMonth.addSelectedDay(new FullDay(targetMonth.getYear(), targetMonth.getMonth(), day));
                    }
                }else if(startSelectedRecord.day.getDay() > endSelectedRecord.day.getDay()){
                    for (int day = endSelectedRecord.day.getDay(); day <= startSelectedRecord.day.getDay(); day++){
                        targetMonth.addSelectedDay(new FullDay(targetMonth.getYear(), targetMonth.getMonth(), day));
                    }
                }
            }else{
                targetMonth.addSelectedDay(startSelectedRecord.day);
            }
            return;
        }

        SCMonth startMonth = dataList.get(startSelectedRecord.position);
        int startSelectedMonthDayCount = SCDateUtils.getDayCountOfMonth(startMonth.getYear(), startMonth.getMonth());
        for (int day = startSelectedRecord.day.getDay(); day <= startSelectedMonthDayCount; day++){
            startMonth.addSelectedDay(new FullDay(startMonth.getYear(), startMonth.getMonth(), day));
        }
        if (shouldRefreshView) invalidate(container, startSelectedRecord.position);

        int startSelectedPosition = startSelectedRecord.position;
        int endSelectedPosition = endSelectedRecord.position;

        while (endSelectedPosition - startSelectedPosition > 1){
            startSelectedPosition++;
            SCMonth segmentMonth = dataList.get(startSelectedPosition);
            int segmentSelectedMonthDayCount = SCDateUtils.getDayCountOfMonth(segmentMonth.getYear(), segmentMonth.getMonth());
            for (int day = 1; day <= segmentSelectedMonthDayCount; day++) {
                segmentMonth.addSelectedDay(new FullDay(segmentMonth.getYear(), segmentMonth.getMonth(), day));
            }
            if (shouldRefreshView) invalidate(container, startSelectedPosition);
        }

        SCMonth endMonth = dataList.get(endSelectedRecord.position);
        for (int day = 1; day <= endSelectedRecord.day.getDay(); day++){
            endMonth.addSelectedDay(new FullDay(endMonth.getYear(), endMonth.getMonth(), day));
        }
        if (shouldRefreshView) invalidate(container, endSelectedRecord.position);

        if (shouldRefreshView) segmentSelectListener.onSegmentSelect(startSelectedRecord.day, endSelectedRecord.day);
    }

    /**
     * get the first selected day
     * @return the first selected day, may be null
     */
    public FullDay getStartDay(){
        return startSelectedRecord.day;
    }

    /**
     * get the last selected day
     * @return the last selected day, may be null
     */
    public FullDay getEndDay(){
        return endSelectedRecord.day;
    }

    public List<SCMonth> getDataList() {
        return dataList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeTypedList(dataList);
    }

    protected CalendarSelector(Parcel in) {
        super(in);
        this.dataList = in.createTypedArrayList(SCMonth.CREATOR);
    }

    public static final Creator<CalendarSelector> CREATOR = new Creator<CalendarSelector>() {
        public CalendarSelector createFromParcel(Parcel source) {
            return new CalendarSelector(source);
        }

        public CalendarSelector[] newArray(int size) {
            return new CalendarSelector[size];
        }
    };
}
