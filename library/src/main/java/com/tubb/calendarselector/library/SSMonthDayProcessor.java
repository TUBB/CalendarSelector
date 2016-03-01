package com.tubb.calendarselector.library;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by tubingbing on 16/2/4.
 */
public class SSMonthDayProcessor {

    private static final String TAG = "mv";
    private Mode mode;
    private RecyclerView recyclerView;
    private SelectedRecord startSelectedRecord = new SelectedRecord();
    private SelectedRecord endSelectedRecord = new SelectedRecord();
    private List<SSDay> sDays = new LinkedList<>();
    private List<SSMonth> dataList;
    private IntervalSelectListener intervalSelectListener;
    private SegmentSelectListener segmentSelectListener;

    public SSMonthDayProcessor(RecyclerView recyclerView, List<SSMonth> dataList, IntervalSelectListener intervalSelectListener){
        this.recyclerView = recyclerView;
        this.mode = Mode.INTERVAL;
        this.dataList = dataList;
        this.intervalSelectListener = intervalSelectListener;
    }

    public SSMonthDayProcessor(RecyclerView recyclerView, List<SSMonth> dataList, SegmentSelectListener segmentSelectListener){
        this.recyclerView = recyclerView;
        this.mode = Mode.SEGMENT;
        this.dataList = dataList;
        this.segmentSelectListener = segmentSelectListener;
    }

    public void bind(final SSMonthView ssMonthView, final RecyclerView.ViewHolder viewHolder){
        ssMonthView.setMonthDayClickListener(new SSMonthView.OnMonthDayClickListener() {
            @Override
            public void onMonthDayClick(SSDay ssDay) {
                if(!(ssDay.getDayType() == SSDay.CURRENT_MONTH_DAY || ssDay.getDayType() == SSDay.TODAY)) return;
                switch (mode){
                    case INTERVAL:
                        intervalSelect(ssMonthView, ssDay);
                        break;
                    case SEGMENT:
                        segmentSelect(ssMonthView, ssDay, viewHolder);
                        break;
                }
            }
        });
    }

    private void segmentSelect(SSMonthView ssMonthView, SSDay ssDay, RecyclerView.ViewHolder viewHolder) {
        if(segmentSelectListener.onInterceptSelect(ssDay)) return;

        if(!startSelectedRecord.isRecord() && !endSelectedRecord.isRecord()){ // init status
            startSelectedRecord.position = viewHolder.getAdapterPosition();
            startSelectedRecord.day = ssDay;
            ssMonthView.getSsMonth().addSelectedDay(ssDay);
            invalidate(viewHolder);
        }else if(startSelectedRecord.isRecord() && !endSelectedRecord.isRecord()){ // start day is ok, but end day not
            if(startSelectedRecord.position < viewHolder.getAdapterPosition()){ // click later month
                if(segmentSelectListener.onInterceptSelect(startSelectedRecord.day, ssDay)) return;
                endSelectedRecord.position = viewHolder.getAdapterPosition();
                endSelectedRecord.day = ssDay;
                segmentMonthSelected();
            }else if(startSelectedRecord.position > viewHolder.getAdapterPosition()){ // click before month
                if(segmentSelectListener.onInterceptSelect(ssDay, startSelectedRecord.day)) return;
                endSelectedRecord.position = startSelectedRecord.position;
                endSelectedRecord.day = startSelectedRecord.day;
                startSelectedRecord.position = viewHolder.getAdapterPosition();
                startSelectedRecord.day = ssDay;
                segmentMonthSelected();
            }else{ // click the same month
                SSMonth ssMonth = startSelectedRecord.day.getSsMonth();
                if(startSelectedRecord.day.getDay() != ssDay.getDay()){
                    if(startSelectedRecord.day.getDay() < ssDay.getDay()){
                        if(segmentSelectListener.onInterceptSelect(startSelectedRecord.day, ssDay)) return;
                        for (int day = startSelectedRecord.day.getDay(); day <= ssDay.getDay(); day++){
                            ssMonth.addSelectedDay(new SSDay(DateUtils.isToday(ssMonth.getYear(),
                                    ssMonth.getMonth(), day) ?SSDay.TODAY:SSDay.CURRENT_MONTH_DAY, ssMonth, day));
                        }
                        endSelectedRecord.position = viewHolder.getAdapterPosition();
                        endSelectedRecord.day = ssDay;
                    }else if(startSelectedRecord.day.getDay() > ssDay.getDay()){
                        if(segmentSelectListener.onInterceptSelect(ssDay, startSelectedRecord.day)) return;
                        for (int day = ssDay.getDay(); day <= startSelectedRecord.day.getDay(); day++){
                            ssMonth.addSelectedDay(new SSDay(DateUtils.isToday(ssMonth.getYear(),
                                    ssMonth.getMonth(), day)?SSDay.TODAY:SSDay.CURRENT_MONTH_DAY, ssMonth, day));
                        }
                        endSelectedRecord.position = viewHolder.getAdapterPosition();
                        endSelectedRecord.day = startSelectedRecord.day;
                        startSelectedRecord.day = ssDay;
                    }
                    RecyclerView.ViewHolder startViewHolder = recyclerView.findViewHolderForAdapterPosition(startSelectedRecord.position);
                    invalidate(startViewHolder);
                    segmentSelectListener.onSegmentSelect(startSelectedRecord.day, endSelectedRecord.day);
                }else{
                    ssMonth.getSelectedDays().clear();
                    RecyclerView.ViewHolder startViewHolder = recyclerView.findViewHolderForAdapterPosition(startSelectedRecord.position);
                    invalidate(startViewHolder);
                    startSelectedRecord.reset();
                    endSelectedRecord.reset();
                }
            }

        }else if(startSelectedRecord.isRecord() && endSelectedRecord.isRecord()){ // start day and end day is ok
            startSelectedRecord.day.getSsMonth().getSelectedDays().clear();
            RecyclerView.ViewHolder startViewHolder = recyclerView.findViewHolderForAdapterPosition(startSelectedRecord.position);
            invalidate(startViewHolder);

            endSelectedRecord.day.getSsMonth().getSelectedDays().clear();
            RecyclerView.ViewHolder endViewHolder = recyclerView.findViewHolderForAdapterPosition(endSelectedRecord.position);
            invalidate(endViewHolder);

            int startSelectedPosition = startSelectedRecord.position;
            int endSelectedPosition = endSelectedRecord.position;

            if(endSelectedPosition - startSelectedPosition > 1){
                do {
                    startSelectedPosition++;
                    dataList.get(startSelectedPosition).getSelectedDays().clear();
                    RecyclerView.ViewHolder segmentViewHolder = recyclerView.findViewHolderForAdapterPosition(startSelectedPosition);
                    invalidate(segmentViewHolder);
                }while (startSelectedPosition < endSelectedPosition);
            }

            startSelectedRecord.position = viewHolder.getAdapterPosition();
            startSelectedRecord.day = ssDay;
            startSelectedRecord.day.getSsMonth().addSelectedDay(new SSDay(SSDay.CURRENT_MONTH_DAY,
                    startSelectedRecord.day.getSsMonth(), ssDay.getDay()));
            invalidate(viewHolder);

            endSelectedRecord.reset();
        }
    }

    public void invalidate(RecyclerView.ViewHolder viewHolder){
        if(viewHolder != null) {
            ViewGroup vg = (ViewGroup) viewHolder.itemView;
            for (int i = 0; i < vg.getChildCount(); i++) {
                View view = vg.getChildAt(i);
                if(view instanceof SSMonthView) view.invalidate();
            }
        }
    }

    private void segmentMonthSelected() {

        SSMonth startMonth = startSelectedRecord.day.getSsMonth();
        int startSelectedMonthDayCount = DateUtils.getDayCountOfMonth(startMonth.getYear(), startMonth.getMonth());
        for (int day = startSelectedRecord.day.getDay(); day <= startSelectedMonthDayCount; day++){
            startMonth.addSelectedDay(new SSDay(DateUtils.isToday(startMonth.getYear(), startMonth.getMonth(), day)?SSDay.TODAY:SSDay.CURRENT_MONTH_DAY, startMonth, day));
        }
        RecyclerView.ViewHolder startViewHolder = recyclerView.findViewHolderForAdapterPosition(startSelectedRecord.position);
        if(startViewHolder != null) invalidate(startViewHolder);

        int startSelectedPosition = startSelectedRecord.position;
        int endSelectedPosition = endSelectedRecord.position;

        while (endSelectedPosition - startSelectedPosition > 1){
            startSelectedPosition++;
            SSMonth segmentMonth = dataList.get(startSelectedPosition);
            int segmentSelectedMonthDayCount = DateUtils.getDayCountOfMonth(segmentMonth.getYear(), segmentMonth.getMonth());
            for (int day = 1; day <= segmentSelectedMonthDayCount; day++) {
                segmentMonth.addSelectedDay(new SSDay(DateUtils.isToday(segmentMonth.getYear(), segmentMonth.getMonth(), day)?SSDay.TODAY:SSDay.CURRENT_MONTH_DAY, segmentMonth, day));
            }

            RecyclerView.ViewHolder segmentViewHolder = recyclerView.findViewHolderForAdapterPosition(startSelectedPosition);
            invalidate(segmentViewHolder);
        }

        SSMonth endMonth = endSelectedRecord.day.getSsMonth();
        for (int day = 1; day <= endSelectedRecord.day.getDay(); day++){
            endMonth.addSelectedDay(new SSDay(DateUtils.isToday(endMonth.getYear(), endMonth.getMonth(), day)?SSDay.TODAY:SSDay.CURRENT_MONTH_DAY, endMonth, day));
        }
        RecyclerView.ViewHolder endViewHolder = recyclerView.findViewHolderForAdapterPosition(endSelectedRecord.position);
        if(endViewHolder != null) invalidate(endViewHolder);

        segmentSelectListener.onSegmentSelect(startSelectedRecord.day, endSelectedRecord.day);
    }

    private void intervalSelect(SSMonthView ssMonthView, SSDay ssDay) {
        if(intervalSelectListener.onInterceptSelect(sDays, ssDay)) return;
        List<SSDay> selectedDays = ssDay.getSsMonth().getSelectedDays();
        if(selectedDays.contains(ssDay)) {
            selectedDays.remove(ssDay);
            sDays.remove(ssDay);
        } else {
            selectedDays.add(ssDay);
            sDays.add(ssDay);
        }
        intervalSelectListener.onIntervalSelect(sDays);
        ssMonthView.invalidate();
    }

    public SSDay getStartDay(){
        return startSelectedRecord.day;
    }

    public SSDay getEndDay(){
        return endSelectedRecord.day;
    }

    public static class SelectedRecord{
        public int position = -1;
        public SSDay day;

        public boolean isRecord(){
            return position >= 0 && day != null;
        }

        public void reset(){
            position = -1;
            day = null;
        }
    }

    public enum Mode{
        INTERVAL,
        SEGMENT
    }

}
