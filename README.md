Calendar Selector
=================

A `calendar selector` for select dates, support select a `continuous` period of time and some `discontinuous` dates.

Supported functionality:
 
 * select `continuous` or `discontinuous` dates
 * single month or multi months selection
 * intercept select event
 * state saved
 * UI custom
 * indicate the start day of a week (`SUNDAY`、`SATURDAY`、`MONDAY`)
 * editor mode support, a good feeling of developing
 * API 8+
 
Preview
=======

![Preview](https://github.com/TUBB/CalendarSelector/blob/master/art/preview.gif)

Usage
-----

Add to dependencies

```groovy
compile 'com.tubb.calendarselector.library:calendar-selector:1.4'
```

Just use [MonthView][1], [MonthView][1] is a custom view for display month's days

```xml
<com.tubb.calendarselector.library.MonthView
    android:id="@+id/ssMv"
    android:layout_width="match_parent"
    android:layout_height="300dp"
    sc:sc_firstday_week="sunday"
    sc:sc_draw_monthday="false"
    sc:sc_month="2016-3"/>
```

![Month](https://github.com/TUBB/CalendarSelector/blob/master/art/1.png)

We provide two `calendar selector` to select dates, one ( [SingleMonthSelector][2] ) is used for single month, 
another ( [CalendarSelector][3] ) is used for multi months

[SingleMonthSelector][2] usage

```java
singleMonthSelector.bind(monthView);
```

[CalendarSelector][3] usage ( support all `ViewGroup`'s subclasses, except `ListView` )

```java
calendarSelector.bind(containerViewGroup, monthView, itemPosition);
```

We support intercept select event, so you do something you like, such as define the limit dates

segment mode

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

interval mode

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

[SingleMonthSelector][2] and [CalendarSelector][3] support two selector mode ( `SEGMENT` and `INTERVAL` ) and state saved, restore selector state

More usage detail please see [SingleMonthSelectorActivity][4] and [CalendarSelectorActivity][5]

We provide month's day drawer [DayDrawer][6] to custom month view display, we implements a default drawer [DefaultDayDrawer][8], you can extend it for you, please look at [CustomDrawerActivity][7]

We include so many attrs for [MonthView][1], just like indicate the start day of a week...

```xml
<declare-styleable name="MonthView">
    <!-- only draw the month day, or not, default is false -->
    <attr name="sc_draw_monthday" format="boolean"/>
    <!-- the monday day text color -->
    <attr name="sc_normalday_color" format="color"/>
    <!-- last month day text color -->
    <attr name="sc_prevmonthday_color" format="color"/>
    <!-- today text color -->
    <attr name="sc_today_color" format="color"/>
    <!-- next month day text color -->
    <attr name="sc_nextmonthday_color" format="color"/>
    <!-- selected day text color -->
    <attr name="sc_selectedday_color" format="color"/>
    <!-- selected day background color -->
    <attr name="sc_selectedday_bgcolor" format="color"/>
    <!-- the day text size -->
    <attr name="sc_day_textsize" format="dimension"/>
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
```

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
 [4]: https://github.com/TUBB/CalendarSelector/blob/master/app/src/main/java/com/tubb/calendarselector/SingleMonthSelectorActivity.java
 [5]: https://github.com/TUBB/CalendarSelector/blob/master/app/src/main/java/com/tubb/calendarselector/CalendarSelectorActivity.java
 [6]: https://github.com/TUBB/CalendarSelector/blob/master/library/src/main/java/com/tubb/calendarselector/library/DayDrawer.java
 [7]: https://github.com/TUBB/CalendarSelector/blob/master/app/src/main/java/com/tubb/calendarselector/CustomDrawerActivity.java
 [8]: https://github.com/TUBB/CalendarSelector/blob/master/library/src/main/java/com/tubb/calendarselector/library/DefaultDayDrawer.java