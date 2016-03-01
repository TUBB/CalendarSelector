package com.tubb.calendarselector.library;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tubingbing on 16/1/19.
 */
public class SSDay implements Parcelable {

    public static final int PRE_MONTH_DAY = 0;
    public static final int TODAY = 1;
    public static final int CURRENT_MONTH_DAY = 2;
    public static final int NEXT_MONTH_DAY = 3;

    protected int dayType;
    protected SSMonth ssMonth;
    protected int day;

    public SSDay(int dayType, SSMonth ssMonth, int day) {
        this.dayType = dayType;
        this.ssMonth = ssMonth;
        this.day = day;
    }

    public int getDayType() {
        return dayType;
    }

    public void setDayType(int dayType) {
        this.dayType = dayType;
    }

    public SSMonth getSsMonth() {
        return ssMonth;
    }

    public void setSsMonth(SSMonth ssMonth) {
        this.ssMonth = ssMonth;
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

        SSDay ssDay = (SSDay) o;

        if (dayType != ssDay.dayType) return false;
        if (day != ssDay.day) return false;
        return ssMonth.equals(ssDay.ssMonth);

    }

    @Override
    public int hashCode() {
        int result = dayType;
        result = 31 * result + ssMonth.hashCode();
        result = 31 * result + day;
        return result;
    }

    @Override
    public String toString() {
        return ssMonth.getYear()+"-"+ssMonth.getMonth()+"-"+day;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.dayType);
        dest.writeParcelable(this.ssMonth, flags);
        dest.writeInt(this.day);
    }

    protected SSDay(Parcel in) {
        this.dayType = in.readInt();
        this.ssMonth = in.readParcelable(SSMonth.class.getClassLoader());
        this.day = in.readInt();
    }

    public static final Parcelable.Creator<SSDay> CREATOR = new Parcelable.Creator<SSDay>() {
        public SSDay createFromParcel(Parcel source) {
            return new SSDay(source);
        }

        public SSDay[] newArray(int size) {
            return new SSDay[size];
        }
    };
}
