package com.tubb.calendarselector.custom;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.tubb.calendarselector.R;
import com.tubb.calendarselector.library.FullDay;
import com.tubb.calendarselector.library.IntervalSelectListener;
import com.tubb.calendarselector.library.MonthView;
import com.tubb.calendarselector.library.SCDateUtils;
import com.tubb.calendarselector.library.SCMonth;
import com.tubb.calendarselector.library.SegmentSelectListener;
import com.tubb.calendarselector.library.SingleMonthSelector;

import java.util.List;

public class CustomDayViewActivity extends AppCompatActivity {

    private static final String TAG = "mv";
    SCMonth scMonth;
    private SingleMonthSelector selector;
    private MonthView monthView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_month_selector);
        monthView = (MonthView) findViewById(R.id.ssMv);
        TextView tvMonthTitle = (TextView) findViewById(R.id.tvMonthTitle);
        if(savedInstanceState != null){
            scMonth = savedInstanceState.getParcelable("month");
        }
        if(scMonth == null)
            scMonth = new SCMonth(2016, 2, SCMonth.SUNDAY_OF_WEEK);
        tvMonthTitle.setText(scMonth.toString());
        segmentMode();
    }

    private void segmentMode(){
        scMonth = new SCMonth(2016, 2, SCMonth.SUNDAY_OF_WEEK);
        monthView.setSCMonth(scMonth, new CustomDayViewInflater(this));
        selector = new SingleMonthSelector(SingleMonthSelector.Mode.SEGMENT);
        selector.setSegmentSelectListener(new SegmentSelectListener() {
            @Override
            public void onSegmentSelect(FullDay startDay, FullDay endDay) {
                Log.d(TAG, "segment select " + startDay.toString() + " : " + endDay.toString());
            }

            @Override
            public boolean onInterceptSelect(FullDay selectingDay) {
                if(SCDateUtils.isToday(selectingDay.getYear(), selectingDay.getMonth(), selectingDay.getDay())){
                    Toast.makeText(CustomDayViewActivity.this, "Today can't be selected", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return super.onInterceptSelect(selectingDay);
            }

            @Override
            public boolean onInterceptSelect(FullDay startDay, FullDay endDay) {
                int differDays = SCDateUtils.countDays(startDay.getYear(), startDay.getMonth(), startDay.getDay(),
                        endDay.getYear(), endDay.getMonth(), endDay.getDay());
                Log.d(TAG, "differDays " + differDays);
                if(differDays > 5) {
                    Toast.makeText(CustomDayViewActivity.this, "Selected days can't more than 5", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return super.onInterceptSelect(startDay, endDay);
            }

        });
        selector.bind(monthView);
    }

    private void intervalMode(){
        scMonth = new SCMonth(2016, 2, SCMonth.SUNDAY_OF_WEEK);
        monthView.setSCMonth(scMonth, new CustomDayViewInflater(this));
        selector = new SingleMonthSelector(SingleMonthSelector.Mode.INTERVAL);
        selector.setIntervalSelectListener(new IntervalSelectListener() {
            @Override
            public void onIntervalSelect(List<FullDay> selectedDays) {
                Log.d(TAG, "interval selected days " + selectedDays.toString());
            }

            @Override
            public boolean onInterceptSelect(List<FullDay> selectedDays, FullDay selectingDay) {
                if(selectedDays.size() >= 5) {
                    Toast.makeText(CustomDayViewActivity.this, "Selected days can't more than 5", Toast.LENGTH_LONG).show();
                    return true;
                }
                return super.onInterceptSelect(selectedDays, selectingDay);
            }
        });
        selector.bind(monthView);
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

}
