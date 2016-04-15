package com.tubb.calendarselector.normal;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.tubb.calendarselector.R;
import com.tubb.calendarselector.library.FullDay;
import com.tubb.calendarselector.library.SCMonth;
import com.tubb.calendarselector.library.MonthView;

public class NormalActivity extends AppCompatActivity {

    private static final String TAG = "mv";
    SCMonth scMonth;
    private MonthView monthView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal);
        monthView = (MonthView) findViewById(R.id.ssMv);

        TextView tvMonthTitle = (TextView) findViewById(R.id.tvMonthTitle);
        if(savedInstanceState != null){
            scMonth = savedInstanceState.getParcelable("month");
        }
        if(scMonth == null)
            scMonth = new SCMonth(2016, 5);
        tvMonthTitle.setText(scMonth.toString());
        monthView.setSCMonth(scMonth);
        monthView.setMonthDayClickListener(new MonthView.OnMonthDayClickListener() {
            @Override
            public void onMonthDayClick(FullDay day) {
                monthView.clearSelectedDays();
                monthView.addSelectedDay(day);
            }
        });
    }

}
