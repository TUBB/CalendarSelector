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

import com.tubb.calendarselector.library.CalendarSelector;
import com.tubb.calendarselector.library.SCDateUtils;
import com.tubb.calendarselector.library.FullDay;
import com.tubb.calendarselector.library.SCMonth;
import com.tubb.calendarselector.library.MonthView;
import com.tubb.calendarselector.library.SegmentSelectListener;

import java.util.ArrayList;
import java.util.List;

public class StateSavedActivity extends AppCompatActivity {

    private static final String TAG = "mv";

    CalendarSelector processor;
    RecyclerView rvCalendar;
    List<SCMonth> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        if(savedInstanceState != null){
            processor = savedInstanceState.getParcelable("selector");
        }
        if(processor != null){ // restore data
            data = processor.getDataList();
        }else{ // generate data
            data = getData();
            processor = new CalendarSelector(data, CalendarSelector.Mode.SEGMENT);
        }

        rvCalendar = (RecyclerView) findViewById(R.id.rvCalendar);
        rvCalendar.setLayoutManager(new LinearLayoutManager(this));

        processor.setSegmentSelectListener(new SegmentSelectListener() {
            @Override
            public void onSegmentSelect(FullDay startDay, FullDay endDay) {
                Log.d(TAG, "segment select " + startDay.toString() + " : " + endDay.toString());
            }

            @Override
            public boolean onInterceptSelect(FullDay selectingDay) {
                if(SCDateUtils.isToday(selectingDay.getYear(), selectingDay.getMonth(), selectingDay.getDay())){
                    Toast.makeText(StateSavedActivity.this, "Today can't be selected", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return super.onInterceptSelect(selectingDay);
            }

            @Override
            public boolean onInterceptSelect(FullDay startDay, FullDay endDay) {
                int differDays = SCDateUtils.countDays(startDay.getYear(), startDay.getMonth(), startDay.getDay(),
                        endDay.getYear(), endDay.getMonth(), endDay.getDay());
                Log.d(TAG, "segment select " + startDay.toString() + " : " + endDay.toString());
                Log.d(TAG, "differDays " + differDays);
                if(differDays > 5) {
                    Toast.makeText(StateSavedActivity.this, "Selected days can't more than 5", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return super.onInterceptSelect(startDay, endDay);
            }
        });
        rvCalendar.setAdapter(new CalendarAdpater(data));
    }

    public List<SCMonth> getData() {
        return SCDateUtils.generateMonths(2016, 2017);
    }

    class CalendarAdpater extends RecyclerView.Adapter<CalendarViewHolder>{

        List<SCMonth> months;

        public CalendarAdpater(List<SCMonth> months){
            this.months = months;
        }

        @Override
        public CalendarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CalendarViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_calendar, parent, false));
        }

        @Override
        public void onBindViewHolder(CalendarViewHolder holder, int position) {
            SCMonth SCMonth = months.get(position);
            holder.tvMonthTitle.setText(String.format("%d-%d", SCMonth.getYear(), SCMonth.getMonth()));
            holder.monthView.setSCMonth(SCMonth);
            processor.bind(rvCalendar, holder.monthView, position);
        }

        @Override
        public int getItemCount() {
            return months.size();
        }
    }

    class CalendarViewHolder extends RecyclerView.ViewHolder{

        TextView tvMonthTitle;
        MonthView monthView;

        public CalendarViewHolder(View itemView) {
            super(itemView);
            tvMonthTitle = (TextView) itemView.findViewById(R.id.tvMonthTitle);
            monthView = (MonthView) itemView.findViewById(R.id.ssMv);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("selector", processor);
        super.onSaveInstanceState(outState);
    }

}
