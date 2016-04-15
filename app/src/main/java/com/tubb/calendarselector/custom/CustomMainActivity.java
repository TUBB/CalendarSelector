package com.tubb.calendarselector.custom;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.tubb.calendarselector.R;
import com.tubb.calendarselector.normal.CalendarSelectorActivity;
import com.tubb.calendarselector.normal.NormalActivity;
import com.tubb.calendarselector.normal.SingleMonthSelectorActivity;
import com.tubb.calendarselector.normal.StateSavedActivity;
import com.tubb.calendarselector.normal.ViewPagerActivity;

public class CustomMainActivity extends AppCompatActivity {

    private static final String TAG = "mv";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_main);
    }

    public void viewClick(View view){
        switch (view.getId()){
            case R.id.bt_normal:
                startActivity(new Intent(this, CustomDayViewActivity.class));
                break;
            case R.id.bt_anim:
                startActivity(new Intent(this, AnimDayViewActivity.class));
                break;
            case R.id.bt_anim_calendar:
                startActivity(new Intent(this, AnimCalendarSelectorActivity.class));
                break;
            case R.id.bt_decor:
                startActivity(new Intent(this, DecorDayViewActivity.class));
                break;
        }
    }
}
