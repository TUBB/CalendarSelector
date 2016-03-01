package com.tubb.calendarselector;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.tubb.calendarselector.library.SSDay;
import com.tubb.calendarselector.library.SSMonth;
import com.tubb.calendarselector.library.SSMonthView;

public class NormalActivity extends AppCompatActivity {

    private static final String TAG = "mv";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal);
        SSMonthView ssMonthView = (SSMonthView) findViewById(R.id.ssMv);
        ssMonthView.setSsMonth(new SSMonth(2016, 3));
        ssMonthView.setMonthDayClickListener(new SSMonthView.OnMonthDayClickListener() {
            @Override
            public void onMonthDayClick(SSDay ssDay) {
                Log.d(TAG, ssDay.toString());
            }
        });
    }

}
