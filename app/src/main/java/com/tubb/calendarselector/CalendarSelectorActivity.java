package com.tubb.calendarselector;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tubb.calendarselector.library.SCDateUtils;
import com.tubb.calendarselector.library.FullDay;
import com.tubb.calendarselector.library.IntervalSelectListener;
import com.tubb.calendarselector.library.SCMonth;
import com.tubb.calendarselector.library.CalendarSelector;
import com.tubb.calendarselector.library.MonthView;
import com.tubb.calendarselector.library.SegmentSelectListener;

import java.util.List;

public class CalendarSelectorActivity extends AppCompatActivity {

    private static final String TAG = "mv";

    CalendarSelector selector;
    RecyclerView rvCalendar;
    List<SCMonth> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        if(savedInstanceState != null)
            selector = savedInstanceState.getParcelable("selector");
        rvCalendar = (RecyclerView) findViewById(R.id.rvCalendar);
        rvCalendar.setLayoutManager(new LinearLayoutManager(this));
        data = getData();
        segmentMode();
    }

    /**
     * segment mode
     */
    private void segmentMode(){

        for (SCMonth month:data)
            month.getSelectedDays().clear();

        selector = new CalendarSelector(data, CalendarSelector.Mode.SEGMENT);
        selector.setSegmentSelectListener(new SegmentSelectListener() {
            @Override
            public void onSegmentSelect(FullDay startDay, FullDay endDay) {
                Log.d(TAG, "segment select " + startDay.toString() + " : " + endDay.toString());
            }

            @Override
            public boolean onInterceptSelect(FullDay selectingDay) { // one day intercept
                if(SCDateUtils.isToday(selectingDay.getYear(), selectingDay.getMonth(), selectingDay.getDay())){
                    Toast.makeText(CalendarSelectorActivity.this, "Today can't be selected", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return super.onInterceptSelect(selectingDay);
            }

            @Override
            public boolean onInterceptSelect(FullDay startDay, FullDay endDay) { // segment days intercept
                int differDays = SCDateUtils.countDays(startDay.getYear(), startDay.getMonth(), startDay.getDay(),
                        endDay.getYear(), endDay.getMonth(), endDay.getDay());
                Log.d(TAG, "differDays " + differDays);
                if(differDays > 10) {
                    Toast.makeText(CalendarSelectorActivity.this, "Selected days can't more than 10", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return super.onInterceptSelect(startDay, endDay);
            }

            @Override
            public void selectedSameDay(FullDay sameDay) { // selected the same day
                super.selectedSameDay(sameDay);
            }
        });
        rvCalendar.setAdapter(new CalendarAdpater(data));
    }

    /**
     * interval mode
     */
    private void intervalMode(){
        for (SCMonth month:data)
            month.getSelectedDays().clear();
        selector = new CalendarSelector(data, CalendarSelector.Mode.INTERVAL);
        selector.setIntervalSelectListener(new IntervalSelectListener() {
            @Override
            public void onIntervalSelect(List<FullDay> selectedDays) {
                Log.d(TAG, "interval selected days " + selectedDays.toString());
            }

            @Override
            public boolean onInterceptSelect(List<FullDay> selectedDays, FullDay selectingDay) {
                if(selectedDays.size() >= 5) {
                    Toast.makeText(CalendarSelectorActivity.this, "Selected days can't more than 5", Toast.LENGTH_LONG).show();
                    return true;
                }
                return super.onInterceptSelect(selectedDays, selectingDay);
            }
        });
        rvCalendar.setAdapter(new CalendarAdpater(data));
    }

    public List<SCMonth> getData() {
        return SCDateUtils.generateMonths(2016, 3, 2017, 6);
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
            selector.bind(rvCalendar, holder.monthView, position);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mode, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_segment) {
            segmentMode();
            return true;
        }
        else if (id == R.id.action_interval) {
            intervalMode();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("selector", selector);
        super.onSaveInstanceState(outState);
    }

}
