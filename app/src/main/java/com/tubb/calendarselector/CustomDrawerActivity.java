package com.tubb.calendarselector;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.tubb.calendarselector.library.FullDay;
import com.tubb.calendarselector.library.SSMonth;
import com.tubb.calendarselector.library.SSMonthView;

public class CustomDrawerActivity extends AppCompatActivity {

    private static final String TAG = "mv";
    SSMonth ssMonth;
    private SSMonthView ssMonthView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal);
        ssMonthView = (SSMonthView) findViewById(R.id.ssMv);

        TextView tvMonthTitle = (TextView) findViewById(R.id.tvMonthTitle);
        if(savedInstanceState != null){
            ssMonth = savedInstanceState.getParcelable("month");
        }
        if(ssMonth == null)
            ssMonth = new SSMonth(2016, 1);
        tvMonthTitle.setText(ssMonth.toString());
        ssMonthView.setSsMonth(ssMonth, new CustomDrawer(this.getApplicationContext()));
        ssMonthView.setMonthDayClickListener(new SSMonthView.OnMonthDayClickListener() {
            @Override
            public void onMonthDayClick(FullDay day) {
                ssMonthView.getSsMonth().getSelectedDays().clear();
                ssMonthView.getSsMonth().getSelectedDays().add(day);
                ssMonthView.invalidate();
            }
        });
    }

}
