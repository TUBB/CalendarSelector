package com.tubb.calendarselector.library;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by tubingbing on 16/1/19.
 */
public class SCMonth implements Parcelable {

    public static final int SUNDAY_OF_WEEK = 1;
    public static final int MONDAY_OF_WEEK = 2;
    public static final int SATURDAY_OF_WEEK = 7;
    protected int year;
    protected int month;
    protected List<FullDay> selectedDays = new ArrayList<>(5);

    public SCMonth(int year, int month){
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

    public void setSelectedDays(List<FullDay> selectedDays) {
        this.selectedDays = selectedDays;
    }

    public List<FullDay> getSelectedDays() {
        return selectedDays;
    }

    public void addSelectedDay(FullDay fullDay){
        getSelectedDays().add(fullDay);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SCMonth SCMonth = (SCMonth) o;
        if (year != SCMonth.year) return false;
        return month == SCMonth.month;

    }

    @Override
    public int hashCode() {
        int result = year;
        result = 31 * result + month;
        return result;
    }

    @Override
    public String toString() {
        return year + "-" + month;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.year);
        dest.writeInt(this.month);
        dest.writeTypedList(selectedDays);
    }

    protected SCMonth(Parcel in) {
        this.year = in.readInt();
        this.month = in.readInt();
        this.selectedDays = in.createTypedArrayList(FullDay.CREATOR);
    }

    public static final Parcelable.Creator<SCMonth> CREATOR = new Parcelable.Creator<SCMonth>() {
        public SCMonth createFromParcel(Parcel source) {
            return new SCMonth(source);
        }

        public SCMonth[] newArray(int size) {
            return new SCMonth[size];
        }
    };
}
