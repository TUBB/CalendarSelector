package com.tubb.calendarselector.library;

/**
 * Created by tubingbing on 16/1/19.
 */
public class SSMonth {

    private int year;
    private int month;

    public SSMonth(int year, int month){
        this.year = year;
        this.month = month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getMonth() {
        return month;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getYear() {
        return year;
    }


}
