package com.tubb.calendarselector.library;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tubingbing on 16/3/1.
 */
public class FullDay implements Parcelable {

    private int year;
    private int month;
    private int day;

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

    @Override
    public String toString() {
        return "FullDay{" +
                "year=" + year +
                ", month=" + month +
                ", day=" + day +
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
    }

    public FullDay() {
    }

    protected FullDay(Parcel in) {
        this.year = in.readInt();
        this.month = in.readInt();
        this.day = in.readInt();
    }

    public static final Parcelable.Creator<FullDay> CREATOR = new Parcelable.Creator<FullDay>() {
        public FullDay createFromParcel(Parcel source) {
            return new FullDay(source);
        }

        public FullDay[] newArray(int size) {
            return new FullDay[size];
        }
    };
}
