package com.tubb.calendarselector.library;

import android.content.Context;

/**
 * Created by tubingbing on 16/1/29.
 */
public class CalendarSelectorConfiguration {

    private SSDayDrawer ssDayDrawer;

    private CalendarSelectorConfiguration(SSDayDrawer ssDayDrawer){
        this.ssDayDrawer = ssDayDrawer;
    }

    public SSDayDrawer getSsDayDrawer() {
        return ssDayDrawer;
    }

    public static class Builder{

        private Context context;
        private SSDayDrawer ssDayDrawer;

        public Builder(Context context){
            this.context = context.getApplicationContext();
        }

        public Builder ssDayDrawer(SSDayDrawer ssDayDrawer){
            this.ssDayDrawer = ssDayDrawer;
            return this;
        }

        public CalendarSelectorConfiguration build(){
            if(this.ssDayDrawer == null)
                this.ssDayDrawer = new DefaultSSDayDrawer(context);
            return new CalendarSelectorConfiguration(this.ssDayDrawer);
        }

    }

}
