package com.tubb.calendarselector;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.tubb.calendarselector.custom.CustomMainActivity;
import com.tubb.calendarselector.normal.CalendarSelectorActivity;
import com.tubb.calendarselector.normal.NormalActivity;
import com.tubb.calendarselector.normal.NormalMainActivity;
import com.tubb.calendarselector.normal.SingleMonthSelectorActivity;
import com.tubb.calendarselector.normal.StateSavedActivity;
import com.tubb.calendarselector.normal.ViewPagerActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "mv";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void viewClick(View view){
        switch (view.getId()){
            case R.id.bt_default:
                startActivity(new Intent(this, NormalMainActivity.class));
                break;
            case R.id.bt_custom:
                startActivity(new Intent(this, CustomMainActivity.class));
                break;
        }
    }
}
