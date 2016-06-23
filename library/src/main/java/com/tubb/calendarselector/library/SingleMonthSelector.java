package com.tubb.calendarselector.library;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by tubingbing on 16/3/2.
 */
public class SingleMonthSelector implements Parcelable {

    public static final int INTERVAL = 0;
    public static final int SEGMENT = 1;

    protected int mode = -1;
    private SCMonth mMonth;
    protected SelectedRecord startSelectedRecord = new SelectedRecord();
    protected SelectedRecord endSelectedRecord = new SelectedRecord();
    protected List<FullDay> sDays = new LinkedList<>();
    protected IntervalSelectListener intervalSelectListener;
    protected SegmentSelectListener segmentSelectListener;

    public SingleMonthSelector(SCMonth scMonth, @Mode int mode){
        mMonth = scMonth;
        this.mode = mode;
    }

    public void addSelectedSegment(FullDay startDay, FullDay endDay){
        if (mode == INTERVAL) throw new IllegalArgumentException("Just used with SEGMENT mode!!!");
        if (startDay == null || endDay == null) throw new IllegalArgumentException("startDay or endDay can't be null");
        if (startDay.day >= endDay.day) throw new IllegalArgumentException("startDay >= endDay not support");
        SCMonth comparedMonthStart = new SCMonth(startDay.getYear(), startDay.getMonth());
        if (!mMonth.equals(comparedMonthStart)) throw new IllegalArgumentException("startDay not belong to scMonth");
        SCMonth comparedMonthEnd = new SCMonth(endDay.getYear(), endDay.getMonth());
        if (!mMonth.equals(comparedMonthEnd)) throw new IllegalArgumentException("endDay not belong to scMonth");
        startSelectedRecord.day = startDay;
        endSelectedRecord.day = endDay;
        mMonth.addSelectedDay(startDay);
        mMonth.addSelectedDay(endDay);

        if(startSelectedRecord.day.getDay() < endSelectedRecord.day.getDay()){
            for (int day = startSelectedRecord.day.getDay(); day <= endSelectedRecord.day.getDay(); day++){
                mMonth.addSelectedDay(new FullDay(mMonth.getYear(), mMonth.getMonth(), day));
            }
        }else if(startSelectedRecord.day.getDay() > endSelectedRecord.day.getDay()){
            for (int day = endSelectedRecord.day.getDay(); day <= startSelectedRecord.day.getDay(); day++){
                mMonth.addSelectedDay(new FullDay(mMonth.getYear(), mMonth.getMonth(), day));
            }
        }
    }

    public void addSelectedInterval(FullDay day){
        if (mode == SEGMENT) throw new IllegalArgumentException("Just used with INTERVAL mode!!!");
        if (day == null) throw new IllegalArgumentException("day can't be null!!!");
        addSelectedDayToMonth(day);
    }

    public void addSelectedInterval(List<FullDay> selectedDays){
        if (mode == SEGMENT) throw new IllegalArgumentException("Just used with INTERVAL mode!!!");
        if (selectedDays == null) throw new IllegalArgumentException("selectedDays can't be null!!!");
        for (FullDay day : selectedDays) {
            addSelectedDayToMonth(day);
        }
    }

    protected void addSelectedDayToMonth(FullDay day) {
        SCMonth comparedMonth = new SCMonth(day.getYear(), day.getMonth());
        if (mMonth.equals(comparedMonth)){
            mMonth.addSelectedDay(day);
            sDays.add(day);
        }else {
            throw new IllegalArgumentException("The day not belong to any month!!!");
        }
    }


    public void bind(final MonthView monthView){
        if(monthView == null)
            throw new IllegalArgumentException("Invalid params of bind(final ViewGroup container, final SSMonthView monthView, final int position) method");
        if(this.mode == INTERVAL && this.intervalSelectListener == null)
            throw new IllegalArgumentException("Please set IntervalSelectListener for Mode.INTERVAL mode");
        if(this.mode == SEGMENT && this.segmentSelectListener == null)
            throw new IllegalArgumentException("Please set SegmentSelectListener for Mode.SEGMENT mode");
        monthView.setMonthDayClickListener(new MonthView.OnMonthDayClickListener() {
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
                        segmentSelect(monthView, day);
                        break;
                }
            }
        });
    }

    private void segmentSelect(MonthView monthView, FullDay ssDay) {
        if(segmentSelectListener.onInterceptSelect(ssDay)) return;

        if(startSelectedRecord.day == null && endSelectedRecord.day == null){ // init status
            startSelectedRecord.day = ssDay;
            monthView.addSelectedDay(ssDay);
        }else if(endSelectedRecord.day == null){ // start day is ok, but end day not

            if(startSelectedRecord.day.getDay() != ssDay.getDay()){
                if(startSelectedRecord.day.getDay() < ssDay.getDay()){
                    if(segmentSelectListener.onInterceptSelect(startSelectedRecord.day, ssDay)) return;
                    for (int day = startSelectedRecord.day.getDay(); day <= ssDay.getDay(); day++){
                        monthView.addSelectedDay(new FullDay(monthView.getYear(), monthView.getMonth(), day));
                    }
                    endSelectedRecord.day = ssDay;
                }else if(startSelectedRecord.day.getDay() > ssDay.getDay()){
                    if(segmentSelectListener.onInterceptSelect(ssDay, startSelectedRecord.day)) return;
                    for (int day = ssDay.getDay(); day <= startSelectedRecord.day.getDay(); day++){
                        monthView.addSelectedDay(new FullDay(monthView.getYear(), monthView.getMonth(), day));
                    }
                    endSelectedRecord.day = startSelectedRecord.day;
                    startSelectedRecord.day = ssDay;
                }
                segmentSelectListener.onSegmentSelect(startSelectedRecord.day, endSelectedRecord.day);
            }else{
                // selected the same day when the end day is not selected
                segmentSelectListener.selectedSameDay(ssDay);
                monthView.clearSelectedDays();
                startSelectedRecord.reset();
                endSelectedRecord.reset();
            }

        }else { // start day and end day is ok
            monthView.clearSelectedDays();
            monthView.addSelectedDay(ssDay);
            startSelectedRecord.day = ssDay;
            endSelectedRecord.reset();
        }
    }

    protected void intervalSelect(MonthView monthView, FullDay day) {
        if(monthView.getSelectedDays().contains(day)) {
            monthView.removeSelectedDay(day);
            sDays.remove(day);
            if(intervalSelectListener.onInterceptSelect(sDays, day)) return;
        } else {
            if(intervalSelectListener.onInterceptSelect(sDays, day)) return;
            monthView.addSelectedDay(day);
            sDays.add(day);
        }
        intervalSelectListener.onIntervalSelect(sDays);
    }

    public void setIntervalSelectListener(IntervalSelectListener intervalSelectListener) {
        this.intervalSelectListener = intervalSelectListener;
    }

    public void setSegmentSelectListener(SegmentSelectListener segmentSelectListener) {
        this.segmentSelectListener = segmentSelectListener;
    }

    public static class SelectedRecord implements Parcelable {
        public int position = -1;
        public FullDay day;

        public boolean isRecord(){
            return position >= 0 && day != null;
        }

        public void reset(){
            position = -1;
            day = null;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.position);
            dest.writeParcelable(this.day, 0);
        }

        public SelectedRecord() {
        }

        @Override
        public String toString() {
            return "SelectedRecord{" +
                    "position=" + position +
                    ", day=" + day +
                    '}';
        }

        protected SelectedRecord(Parcel in) {
            this.position = in.readInt();
            this.day = in.readParcelable(FullDay.class.getClassLoader());
        }

        public static final Creator<SelectedRecord> CREATOR = new Creator<SelectedRecord>() {
            public SelectedRecord createFromParcel(Parcel source) {
                return new SelectedRecord(source);
            }

            public SelectedRecord[] newArray(int size) {
                return new SelectedRecord[size];
            }
        };
    }

    @IntDef({INTERVAL, SEGMENT})
    public @interface Mode{}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mode);
        dest.writeParcelable(this.startSelectedRecord, flags);
        dest.writeParcelable(this.endSelectedRecord, flags);
        dest.writeTypedList(sDays);
    }

    protected SingleMonthSelector(Parcel in) {
        this.mode = in.readInt();
        this.startSelectedRecord = in.readParcelable(SelectedRecord.class.getClassLoader());
        this.endSelectedRecord = in.readParcelable(SelectedRecord.class.getClassLoader());
        this.sDays = in.createTypedArrayList(FullDay.CREATOR);
    }

    public static final Parcelable.Creator<SingleMonthSelector> CREATOR = new Parcelable.Creator<SingleMonthSelector>() {
        @Override
        public SingleMonthSelector createFromParcel(Parcel source) {
            return new SingleMonthSelector(source);
        }

        @Override
        public SingleMonthSelector[] newArray(int size) {
            return new SingleMonthSelector[size];
        }
    };
}
