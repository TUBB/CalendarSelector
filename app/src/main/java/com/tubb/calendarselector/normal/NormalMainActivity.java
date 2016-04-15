package com.tubb.calendarselector.normal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.tubb.calendarselector.R;

public class NormalMainActivity extends AppCompatActivity {

    private static final String TAG = "mv";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_main);
    }

    public void viewClick(View view){
        switch (view.getId()){
            case R.id.bt_normal:
                startActivity(new Intent(this, NormalActivity.class));
                break;
            case R.id.bt_single:
                startActivity(new Intent(this, SingleMonthSelectorActivity.class));
                break;
            case R.id.bt_calendar:
                startActivity(new Intent(this, CalendarSelectorActivity.class));
                break;
            case R.id.bt_savedstate:
                startActivity(new Intent(this, StateSavedActivity.class));
                break;
            case R.id.bt_vp:
                startActivity(new Intent(this, ViewPagerActivity.class));
                break;
            case R.id.bt_state:
                startActivity(new Intent(this, StateSavedActivity.class));
                break;
        }
    }
}
