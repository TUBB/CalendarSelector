package com.tubb.calendarselector;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.tubb.calendarselector.library.FullDay;
import com.tubb.calendarselector.library.SCMonth;
import com.tubb.calendarselector.library.MonthView;

public class NormalActivity extends AppCompatActivity {

    private static final String TAG = "mv";
    SCMonth SCMonth;
    private MonthView monthView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal);
        monthView = (MonthView) findViewById(R.id.ssMv);

        TextView tvMonthTitle = (TextView) findViewById(R.id.tvMonthTitle);
        if(savedInstanceState != null){
            SCMonth = savedInstanceState.getParcelable("month");
        }
        if(SCMonth == null)
            SCMonth = new SCMonth(2016, 1);
        tvMonthTitle.setText(SCMonth.toString());
        monthView.setSCMonth(SCMonth);
        monthView.setMonthDayClickListener(new MonthView.OnMonthDayClickListener() {
            @Override
            public void onMonthDayClick(FullDay day) {
                monthView.getSCMonth().getSelectedDays().clear();
                monthView.getSCMonth().getSelectedDays().add(day);
                monthView.invalidate();
            }
        });
    }

}
