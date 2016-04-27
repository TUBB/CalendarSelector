package com.tubb.calendarselector.library;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;

import com.tubb.calendarselector.custom.DayViewHolder;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by tubingbing on 16/3/1.
 */
public class FullDay implements Parcelable {

    public static final int WEEK_1 = 1;
    public static final int WEEK_2 = 2;
    public static final int WEEK_3 = 3;
    public static final int WEEK_4 = 4;
    public static final int WEEK_5 = 5;
    public static final int WEEK_6 = 6;
    public static final int WEEK_7 = 7;

    protected int year;
    protected int month;
    protected int day;
    protected int weekOf;

    public FullDay(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setWeekOf(int weekOf) {
        this.weekOf = weekOf;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FullDay fullDay = (FullDay) o;

        if (year != fullDay.year) return false;
        if (month != fullDay.month) return false;
        return day == fullDay.day;

    }

    @Override
    public int hashCode() {
        int result = year;
        result = 31 * result + month;
        result = 31 * result + day;
        return result;
    }

    /**
     * get the position of week ([1-7])
     */
    public int getWeekOf() {
        return weekOf;
    }

    @Override
    public String toString() {
        return "FullDay{" +
                "year=" + year +
                ", month=" + month +
                ", day=" + day +
                ", weekOf=" + weekOf +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.year);
        dest.writeInt(this.month);
        dest.writeInt(this.day);
        dest.writeInt(this.weekOf);
    }

    protected FullDay(Parcel in) {
        this.year = in.readInt();
        this.month = in.readInt();
        this.day = in.readInt();
        this.weekOf = in.readInt();
    }

    public static final Parcelable.Creator<FullDay> CREATOR = new Parcelable.Creator<FullDay>() {
        @Override
        public FullDay createFromParcel(Parcel source) {
            return new FullDay(source);
        }

        @Override
        public FullDay[] newArray(int size) {
            return new FullDay[size];
        }
    };
}
