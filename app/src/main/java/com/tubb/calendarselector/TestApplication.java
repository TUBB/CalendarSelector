package com.tubb.calendarselector;

import android.app.Application;

import com.tubb.calendarselector.library.CalendarSelector;
import com.tubb.calendarselector.library.CalendarSelectorConfiguration;
import com.tubb.calendarselector.library.DefaultSSDayDrawer;

/**
 * Created by tubingbing on 16/2/2.
 */
public class TestApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        CalendarSelector.getInstance().init(new CalendarSelectorConfiguration.Builder(this).ssDayDrawer(new DefaultSSDayDrawer(this)).build());
    }
}
