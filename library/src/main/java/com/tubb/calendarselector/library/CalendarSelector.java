package com.tubb.calendarselector.library;

/**
 * Created by tubingbing on 16/1/29.
 */
public class CalendarSelector {

    private static CalendarSelector calendarSelector;
    private CalendarSelectorConfiguration configuration;

    private CalendarSelector(){}

    public static synchronized CalendarSelector getInstance(){
        if(calendarSelector == null){
            calendarSelector = new CalendarSelector();
        }
        return calendarSelector;
    }

    public void init(CalendarSelectorConfiguration configuration){
        setConfiguration(configuration);
    }

    private void setConfiguration(CalendarSelectorConfiguration configuration) {
        this.configuration = configuration;
    }

    public CalendarSelectorConfiguration getConfiguration() {
        if(this.configuration == null) throw new RuntimeException("Your must call init(CalendarSelectorConfiguration configuration) method first.");
        return configuration;
    }
}
