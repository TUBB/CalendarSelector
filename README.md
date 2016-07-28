Calendar Selector
=================

[中文文档][9]

A `calendar selector` for select dates, support select a `continuous` period of time and some `discontinuous` dates.

Supported functionality:
 
 * select `continuous` or `discontinuous` dates
 * single month or multi months selection
 * intercept select event
 * save state
 * UI custom
 * indicate the start day of a week (`SUNDAY`、`SATURDAY`、`MONDAY`)
 * editor mode support, a good feeling of develop
 * API 8+
 
Preview
=======

![Preview](https://github.com/TUBB/CalendarSelector/blob/master/art/preview.gif)

Usage
-----

Add to dependencies

```groovy
compile 'com.tubb.calendarselector.library:calendar-selector:0.1.8'
```

Just use [MonthView][1], [MonthView][1] is a custom view for display month's days

```xml
<com.tubb.calendarselector.library.MonthView
    android:id="@+id/ssMv"
    android:layout_width="match_parent"
    android:layout_height="300dp"
    sc:sc_firstday_week="sunday"
    sc:sc_draw_monthday_only="false"
    sc:sc_month="2016-3"/>
```

![Month](https://github.com/TUBB/CalendarSelector/blob/master/art/1.png)

We provide two `calendar selector` to select dates, one ( [SingleMonthSelector][2] ) is used for single month, 
another ( [CalendarSelector][3] ) is used for multi months, and the two `calendar selector` support `save state`, [StateSavedActivity][6] shows how to use them

[SingleMonthSelector][2] usage

```java
singleMonthSelector.bind(monthView);
```

[CalendarSelector][3] usage ( support all `ViewGroup`'s subclasses, but except `ListView` )

```java
calendarSelector.bind(containerViewGroup, monthView, itemPosition);
```

We support intercept select event, so you can do something you like, such as define the limit select dates

We include two select mode, one is for `continuous` dates (`SEGMENT` MODE), another is for `discontinuous` dates (`INTERVAL` MODE)

SEGMENT mode

```java
selector = new CalendarSelector(data, CalendarSelector.Mode.SEGMENT);
selector.setSegmentSelectListener(new SegmentSelectListener() {
    @Override
    public void onSegmentSelect(FullDay startDay, FullDay endDay) {
        Log.d(TAG, "segment select " + startDay.toString() + " : " + endDay.toString());
    }

    @Override
    public boolean onInterceptSelect(FullDay selectingDay) { // one day intercept
        if(SCDateUtils.isToday(selectingDay.getYear(), selectingDay.getMonth(), selectingDay.getDay())){
            Toast.makeText(CalendarSelectorActivity.this, "Today can't be selected", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onInterceptSelect(selectingDay);
    }

    @Override
    public boolean onInterceptSelect(FullDay startDay, FullDay endDay) { // segment days intercept
        int differDays = SCDateUtils.countDays(startDay.getYear(), startDay.getMonth(), startDay.getDay(),
                endDay.getYear(), endDay.getMonth(), endDay.getDay());
        Log.d(TAG, "differDays " + differDays);
        if(differDays > 10) {
            Toast.makeText(CalendarSelectorActivity.this, "Selected days can't more than 10", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onInterceptSelect(startDay, endDay);
    }

    @Override
    public void selectedSameDay(FullDay sameDay) { // selected the same day
        super.selectedSameDay(sameDay);
    }
});
```

INTERVAL mode

```java
selector = new SingleMonthSelector(CalendarSelector.Mode.INTERVAL);
selector.setIntervalSelectListener(new IntervalSelectListener() {
    @Override
    public void onIntervalSelect(List<FullDay> selectedDays) {
        Log.d(TAG, "interval selected days " + selectedDays.toString());
    }

    @Override
    public boolean onInterceptSelect(List<FullDay> selectedDays, FullDay selectingDay) {
        if(selectedDays.size() >= 5) {
            Toast.makeText(SingleMonthSelectorActivity.this, "Selected days can't more than 5", Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onInterceptSelect(selectedDays, selectingDay);
    }
});
```

More details please see [SingleMonthSelectorActivity][4] and [CalendarSelectorActivity][5]

Custom
======

`CalendarSelector` easy to custom, we abstract out the month's day ui to custom, 
so you can control the ui of month's day, just like a view, layout in xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    xmlns:tools="http://schemas.android.com/tools">

    <TextView
        android:id="@+id/tvDay"
        android:layout_width="30dp"
        android:layout_height="30dp"
        android:textSize="@dimen/t_16"
        tools:text="0"
        android:layout_gravity="center"
        android:gravity="center"
        android:textColor="@color/color_dayview_text_selector"
        android:background="@drawable/drawable_custom_dayview_text_bg"
        />

</FrameLayout>
```

then implement your `DayViewInflater`, used by MonthView (`MonthView.setSCMonth(scMonth, new CustomDayViewInflater(context))`)

```java
public class CustomDayViewInflater extends DayViewInflater{

    public CustomDayViewInflater(Context context) {
        super(context);
    }

    @Override
    public DayViewHolder inflateDayView(ViewGroup container) {
        View dayView = mLayoutInflater.inflate(R.layout.layout_dayview_custom, container, false);
        return new CustomDayViewHolder(dayView);
    }

    public static class CustomDayViewHolder extends DayViewHolder{

        protected TextView tvDay;
        private int mPrevMonthDayTextColor;
        private int mNextMonthDayTextColor;

        public CustomDayViewHolder(View dayView) {
            super(dayView);
            tvDay = (TextView) dayView.findViewById(com.tubb.calendarselector.library.R.id.tvDay);
            mPrevMonthDayTextColor = ContextCompat.getColor(mContext, com.tubb.calendarselector.library.R.color.c_999999);
            mNextMonthDayTextColor = ContextCompat.getColor(mContext, com.tubb.calendarselector.library.R.color.c_999999);
        }

        @Override
        public void setCurrentMonthDayText(FullDay day, boolean isSelected) {
            tvDay.setText(String.valueOf(day.getDay()));
            tvDay.setSelected(isSelected);
        }

        @Override
        public void setPrevMonthDayText(FullDay day) {
            tvDay.setTextColor(mPrevMonthDayTextColor);
            tvDay.setText(String.valueOf(day.getDay()));
        }

        @Override
        public void setNextMonthDayText(FullDay day) {
            tvDay.setTextColor(mNextMonthDayTextColor);
            tvDay.setText(String.valueOf(day.getDay()));
        }

    }
}
```

When day has selected, the DayViewHolder.setCurrentMonthDayText(FullDay day, boolean isSelected) method will be excute, 
at this moment, you can do something interesting (add animator for day view), please see [AnimDayViewInflater][8]

```java
@Override
public void setCurrentMonthDayText(FullDay day, boolean isSelected) {
    boolean oldSelected = tvDay.isSelected();
    tvDay.setText(String.valueOf(day.getDay()));
    tvDay.setSelected(isSelected);
    // view selected animation
    if(!oldSelected && isSelected){
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(AnimationUtils.loadInterpolator(mContext, android.R.anim.bounce_interpolator));
        animatorSet.play(ObjectAnimator.ofFloat(tvDay, "scaleX", 0.5f, 1.0f))
                .with(ObjectAnimator.ofFloat(tvDay, "scaleY", 0.5f, 1.0f));
        animatorSet.setDuration(500)
                .start();
    }
}
```

We also provide the ability for decorate day view, please see [DecorDayViewInflater][7]
```java
@Override
public Decor inflateHorizontalDecor(ViewGroup container, int row, int totalRow) {
    return new Decor(mLayoutInflater.inflate(R.layout.view_horizontal_decor, container, false), true);
}

@Override
public Decor inflateVerticalDecor(ViewGroup container, int col, int totalCol) {
    return new Decor(mLayoutInflater.inflate(R.layout.view_vertical_decor, container, false), true);
}
```

We include several attrs for [MonthView][1]

```xml
<resources>
    <declare-styleable name="MonthView">
        <!-- only draw the month day, or not, default is false -->
        <attr name="sc_draw_monthday_only" format="boolean"/>
        <!-- start day of a week, we support (sunday、monday and saturday) -->
        <attr name="sc_firstday_week" format="enum">
            <enum name="sunday" value="1"/>
            <enum name="monday" value="2"/>
            <enum name="saturday" value="7"/>
        </attr>

        <!-- editor mode only -->
        <!-- test selected days (format:1,2,3,4) -->
        <attr name="sc_selected_days" format="string"/>
        <!-- test month (format:2016-3) -->
        <attr name="sc_month" format="string"/>
    </declare-styleable>
</resources>
```

Note
====

If you have any question, just commit some issues


License
-------

    Copyright 2016 TUBB

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.



 [1]: https://github.com/TUBB/CalendarSelector/blob/master/library/src/main/java/com/tubb/calendarselector/library/MonthView.java
 [2]: https://github.com/TUBB/CalendarSelector/blob/master/library/src/main/java/com/tubb/calendarselector/library/SingleMonthSelector.java
 [3]: https://github.com/TUBB/CalendarSelector/blob/master/library/src/main/java/com/tubb/calendarselector/library/CalendarSelector.java
 [4]: https://github.com/TUBB/CalendarSelector/blob/master/app/src/main/java/com/tubb/calendarselector/normal/SingleMonthSelectorActivity.java
 [5]: https://github.com/TUBB/CalendarSelector/blob/master/app/src/main/java/com/tubb/calendarselector/normal/CalendarSelectorActivity.java
 [6]: https://github.com/TUBB/CalendarSelector/blob/master/app/src/main/java/com/tubb/calendarselector/normal/StateSavedActivity.java
 [7]: https://github.com/TUBB/CalendarSelector/blob/master/app/src/main/java/com/tubb/calendarselector/custom/DecorDayViewInflater.java
 [8]: https://github.com/TUBB/CalendarSelector/blob/master/app/src/main/java/com/tubb/calendarselector/custom/AnimDayViewInflater.java
 [9]: http://tubb.github.io/2016/04/17/Android%E6%97%A5%E6%9C%9F%E6%98%BE%E7%A4%BA%E5%92%8C%E9%80%89%E6%8B%A9%E5%BA%93/
