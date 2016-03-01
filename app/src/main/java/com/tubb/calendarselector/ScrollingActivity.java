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
import android.widget.Toast;

import com.tubb.calendarselector.library.DateUtils;
import com.tubb.calendarselector.library.LunarDayDrawer;
import com.tubb.calendarselector.library.SSDay;
import com.tubb.calendarselector.library.SSMonth;
import com.tubb.calendarselector.library.SSMonthDayProcessor;
import com.tubb.calendarselector.library.SSMonthView;
import com.tubb.calendarselector.library.SegmentSelectListener;

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

//        processor = new SSMonthDayProcessor(rvCalendar, data, new IntervalSelectListener() {
//            @Override
//            public void onIntervalSelect(List<SSDay> selectedDays) {
//                Log.d(TAG, "interval selected days " + selectedDays.toString());
//            }
//
//            @Override
//            public boolean onInterceptSelect(List<SSDay> selectedDays, SSDay selectingDay) {
//                if(selectedDays.size() >= 5) {
//                    Toast.makeText(ScrollingActivity.this, "Selected days can't more than 5", Toast.LENGTH_LONG).show();
//                    return true;
//                }
//                return super.onInterceptSelect(selectedDays, selectingDay);
//            }
//        });

        processor = new SSMonthDayProcessor(rvCalendar, data, new SegmentSelectListener() {
            @Override
            public void onSegmentSelect(SSDay startDay, SSDay endDay) {
                Log.d(TAG, "segment select " + startDay.toString() + " : " + endDay.toString());
            }

            @Override
            public boolean onInterceptSelect(SSDay selectingDay) {
                if(DateUtils.isToday(selectingDay.getSsMonth().getYear(), selectingDay.getSsMonth().getMonth(), selectingDay.getDay())){
                    Toast.makeText(ScrollingActivity.this, "Today can't be selected", Toast.LENGTH_LONG).show();
                    return true;
                }
                return super.onInterceptSelect(selectingDay);
            }

            @Override
            public boolean onInterceptSelect(SSDay startDay, SSDay endDay) {
                int differDays = DateUtils.countDays(startDay.getSsMonth().getYear(), startDay.getSsMonth().getMonth(), startDay.getDay(),
                        endDay.getSsMonth().getYear(), endDay.getSsMonth().getMonth(), endDay.getDay());
                Log.d(TAG, "differDays " + differDays);
                if(differDays > 5) {
                    Toast.makeText(ScrollingActivity.this, "Selected days can't more than 5", Toast.LENGTH_LONG).show();
                    return true;
                }
                return super.onInterceptSelect(startDay, endDay);
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
            holder.ssMonthView.setDayDrawer(new LunarDayDrawer(ScrollingActivity.this));
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
