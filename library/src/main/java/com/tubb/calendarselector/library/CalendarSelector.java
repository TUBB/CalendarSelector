package com.tubb.calendarselector.library;

import android.os.Parcel;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

/**
 * Created by tubingbing on 16/2/4.
 */
public class CalendarSelector extends SingleMonthSelector {

    private static final String TAG = "mv";
    protected List<SCMonth> dataList;

    public CalendarSelector(List<SCMonth> dataList, Mode mode){
        super(mode);
        this.dataList = dataList;
    }

    public void bind(final ViewGroup container, final MonthView monthView, final int position){
        if(container == null || monthView == null || position < 0)
            throw new IllegalArgumentException("Invalid params of bind(final ViewGroup container, final SSMonthView monthView, final int position) method");
        if(this.mode == Mode.INTERVAL && this.intervalSelectListener == null)
            throw new IllegalArgumentException("Please set IntervalSelectListener for Mode.INTERVAL mode");
        if(this.mode == Mode.SEGMENT && this.segmentSelectListener == null)
            throw new IllegalArgumentException("Please set SegmentSelectListener for Mode.SEGMENT mode");
        if(container instanceof ListView) throw new IllegalArgumentException("Not support ListView yet");
        monthView.setMonthDayClickListener(new MonthView.OnMonthDayClickListener() {
            @Override
            public void onMonthDayClick(FullDay day) {
                if(!SCDateUtils.isMonthDay(monthView.getSCMonth().getYear(), monthView.getSCMonth().getMonth(),
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
        });
    }

    private void segmentSelect(ViewGroup container, MonthView monthView, FullDay ssDay, int position) {

//        Log.d(TAG, startSelectedRecord != null?startSelectedRecord.toString():"startSelectedRecord is null");
//        Log.d(TAG, endSelectedRecord != null?endSelectedRecord.toString():"endSelectedRecord is null");

        if(segmentSelectListener.onInterceptSelect(ssDay)) return;

        if(!startSelectedRecord.isRecord() && !endSelectedRecord.isRecord()){ // init status
            startSelectedRecord.position = position;
            startSelectedRecord.day = ssDay;
            monthView.getSCMonth().addSelectedDay(ssDay);
            monthView.invalidate();
        }else if(startSelectedRecord.isRecord() && !endSelectedRecord.isRecord()){ // start day is ok, but end day not
            if(startSelectedRecord.position < position){ // click later month
                if(segmentSelectListener.onInterceptSelect(startSelectedRecord.day, ssDay)) return;
                endSelectedRecord.position = position;
                endSelectedRecord.day = ssDay;
                segmentMonthSelected(container);
            }else if(startSelectedRecord.position > position){ // click before month
                if(segmentSelectListener.onInterceptSelect(ssDay, startSelectedRecord.day)) return;
                endSelectedRecord.position = startSelectedRecord.position;
                endSelectedRecord.day = startSelectedRecord.day;
                startSelectedRecord.position = position;
                startSelectedRecord.day = ssDay;
                segmentMonthSelected(container);
            }else{ // click the same month
                SCMonth SCMonth = monthView.getSCMonth();
                if(startSelectedRecord.day.getDay() != ssDay.getDay()){
                    if(startSelectedRecord.day.getDay() < ssDay.getDay()){
                        if(segmentSelectListener.onInterceptSelect(startSelectedRecord.day, ssDay)) return;
                        for (int day = startSelectedRecord.day.getDay(); day <= ssDay.getDay(); day++){
                            SCMonth.addSelectedDay(new FullDay(SCMonth.getYear(), SCMonth.getMonth(), day));
                        }
                        endSelectedRecord.position = position;
                        endSelectedRecord.day = ssDay;
                    }else if(startSelectedRecord.day.getDay() > ssDay.getDay()){
                        if(segmentSelectListener.onInterceptSelect(ssDay, startSelectedRecord.day)) return;
                        for (int day = ssDay.getDay(); day <= startSelectedRecord.day.getDay(); day++){
                            SCMonth.addSelectedDay(new FullDay(SCMonth.getYear(), SCMonth.getMonth(), day));
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
                    SCMonth.getSelectedDays().clear();
                    monthView.invalidate();
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
            View childView = container.getChildAt(position);
            if(childView == null){
                if(container instanceof RecyclerView){
                    RecyclerView rv = (RecyclerView)container;
                    rv.getAdapter().notifyItemChanged(position);
                }else{
                    Log.e(TAG, "the container view is not expected ViewGroup");
                }
            }else{
                if(childView instanceof ViewGroup){
                    ViewGroup vg = (ViewGroup) childView;
                    for (int i = 0; i < vg.getChildCount(); i++) {
                        View view = vg.getChildAt(i);
                        if(view instanceof MonthView) view.invalidate();
                    }
                }else{
                    if(childView instanceof MonthView) childView.invalidate();
                }
            }
        }
    }

    private void segmentMonthSelected(ViewGroup container) {

        SCMonth startMonth = dataList.get(startSelectedRecord.position);
        int startSelectedMonthDayCount = SCDateUtils.getDayCountOfMonth(startMonth.getYear(), startMonth.getMonth());
        for (int day = startSelectedRecord.day.getDay(); day <= startSelectedMonthDayCount; day++){
            startMonth.addSelectedDay(new FullDay(startMonth.getYear(), startMonth.getMonth(), day));
        }
        invalidate(container, startSelectedRecord.position);

        int startSelectedPosition = startSelectedRecord.position;
        int endSelectedPosition = endSelectedRecord.position;

        while (endSelectedPosition - startSelectedPosition > 1){
            startSelectedPosition++;
            SCMonth segmentMonth = dataList.get(startSelectedPosition);
            int segmentSelectedMonthDayCount = SCDateUtils.getDayCountOfMonth(segmentMonth.getYear(), segmentMonth.getMonth());
            for (int day = 1; day <= segmentSelectedMonthDayCount; day++) {
                segmentMonth.addSelectedDay(new FullDay(segmentMonth.getYear(), segmentMonth.getMonth(), day));
            }
            invalidate(container, startSelectedPosition);
        }

        SCMonth endMonth = dataList.get(endSelectedRecord.position);
        for (int day = 1; day <= endSelectedRecord.day.getDay(); day++){
            endMonth.addSelectedDay(new FullDay(endMonth.getYear(), endMonth.getMonth(), day));
        }
        invalidate(container, endSelectedRecord.position);

        segmentSelectListener.onSegmentSelect(startSelectedRecord.day, endSelectedRecord.day);
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
