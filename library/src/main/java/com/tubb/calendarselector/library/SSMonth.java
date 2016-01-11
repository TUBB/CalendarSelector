package com.tubb.calendarselector.library;

import java.util.Set;
import java.util.LinkedHashSet;
/**
 * Created by tubingbing on 16/1/19.
 */
public class SSMonth {

    protected int year;
    protected int month;
    protected Set<SSDay> selectedDays = new LinkedHashSet<>();

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

    public Set<SSDay> getSelectedDays() {
        return selectedDays;
    }

    public void addSelectedDay(SSDay ssDay){
        getSelectedDays().add(ssDay);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SSMonth ssMonth = (SSMonth) o;

        if (year != ssMonth.year) return false;
        return month == ssMonth.month;

    }

    @Override
    public int hashCode() {
        int result = year;
        result = 31 * result + month;
        return result;
    }
}
