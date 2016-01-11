package com.tubb.calendarselector;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tubb.calendarselector.library.SSDay;
import com.tubb.calendarselector.library.SSMonth;
import com.tubb.calendarselector.library.SSMonthDayProcessor;
import com.tubb.calendarselector.library.SSMonthView;

import java.util.ArrayList;
import java.util.List;

public class ScrollingActivity extends AppCompatActivity {

    private static final String TAG = "mv";

    SSMonthDayProcessor processor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        RecyclerView rvCalendar = (RecyclerView) findViewById(R.id.rvCalendar);
        rvCalendar.setLayoutManager(new LinearLayoutManager(this));
        final List<SSMonth> data = getData();
//        processor = new SSMonthDayProcessor(rvCalendar, data, new SSMonthDayProcessor.IntervalSelectListener() {
//            @Override
//            public void onIntervalSelect(SSDay day) {
//                Log.d(TAG, "interval select day " + day.toString());
//            }
//
//            @Override
//            public void onIntervalUnSelect(SSDay day) {
//                Log.d(TAG, "interval unselect day " + day.toString());
//            }
//        });

        processor = new SSMonthDayProcessor(rvCalendar, data, new SSMonthDayProcessor.SegmentSelectListener() {
            @Override
            public void onSegmentSelect(SSDay startDay, SSDay endDay) {
                Log.d(TAG, "segment select " + startDay.toString() + " : " + endDay.toString());
            }
        });
        rvCalendar.setAdapter(new CalendarAdpater(data));
    }

    public List<SSMonth> getData() {
        List<SSMonth> data = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            data.add(new SSMonth(2016, i));
        }
        for (int i = 1; i <= 12; i++) {
            data.add(new SSMonth(2017, i));
        }
        return data;
    }

    class CalendarAdpater extends RecyclerView.Adapter<CalendarViewHolder>{

        List<SSMonth> months;

        public CalendarAdpater(List<SSMonth> months){
            this.months = months;
        }

        @Override
        public CalendarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CalendarViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_calendar, parent, false));
        }

        @Override
        public void onBindViewHolder(CalendarViewHolder holder, int position) {
            SSMonth ssMonth = months.get(position);
            holder.tvMonthTitle.setText(String.format("%d-%d", ssMonth.getYear(), ssMonth.getMonth()));
            holder.ssMonthView.setSsMonth(ssMonth);
            processor.bind(holder.ssMonthView, holder);
        }

        @Override
        public int getItemCount() {
            return months.size();
        }
    }

    class CalendarViewHolder extends RecyclerView.ViewHolder{

        TextView tvMonthTitle;
        SSMonthView ssMonthView;

        public CalendarViewHolder(View itemView) {
            super(itemView);
            tvMonthTitle = (TextView) itemView.findViewById(R.id.tvMonthTitle);
            ssMonthView = (SSMonthView) itemView.findViewById(R.id.ssMv);
        }
    }
}
