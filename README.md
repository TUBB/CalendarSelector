Calendar Selector
=================

A `calendar selector` for select dates, support select a `continuous` period of time and some `discontinuous` dates.

Supported functionality:
 
 * select `continuous` or `discontinuous` dates
 * single month or multi months selection
 * intercept select event
 * state saved
 * UI custom
 * editor mode support, a good feeling of developing
 * API 8+
 
Preview
=======

![Preview](https://github.com/TUBB/CalendarSelector/blob/master/art/preview.gif)

Usage
-----

Add to dependencies

```groovy
compile 'com.tubb.calendarselector.library:calendar-selector:1.0'
```

Just use [SSMonthView][1], [SSMonthView][1] is a custom view for display month's days

```xml
<com.tubb.calendarselector.library.SSMonthView
    android:id="@+id/ssMv"
    android:layout_width="match_parent"
    android:layout_height="300dp"
    ss:firstDayOfWeek="sunday"
    ss:draw_monthday="false"
    ss:month="2016-3"/>
```

![Month](https://github.com/TUBB/CalendarSelector/blob/master/art/1.png)

We provide two `calendar selector` to select dates, one ( [SingleMonthSelector][2] ) is use for single month, 
another ( [CalendarSelector][3] ) is use for multi months

[SingleMonthSelector][2] usage

```java
singleMonthSelector.bind(monthView);
```

[CalendarSelector][3] usage ( support all `ViewGroup`'s subclasses )

```java
calendarSelector.bind(containerViewGroup, monthView, itemPosition);
```

[SingleMonthSelector][2] and [CalendarSelector][3] support two selector mode ( `SEGMENT` and `INTERVAL` ) and state saved, restore seletor state

More usage detail please see [SingleMonthSelectorActivity][4] and [CalendarSelectorActivity][5]

We provide month's day drawer [SSDayDrawer][6] to custom month view display, you can draw anything, please look at [CustomDrawerActivity][7]

We include so many attrs for [SSMonthView][1], like indicate the start day of a week...

```xml
<declare-styleable name="SSMonthView">
    <!-- only draw the month day, or not, default is false -->
    <attr name="draw_monthday" format="boolean"/>
    <!-- the monday day text color -->
    <attr name="normalday_color" format="color"/>
    <!-- last month day text color -->
    <attr name="prevmonthday_color" format="color"/>
    <!-- today text color -->
    <attr name="today_color" format="color"/>
    <!-- next month day text color -->
    <attr name="nextmonthday_color" format="color"/>
    <!-- selected day text color -->
    <attr name="selectedday_color" format="color"/>
    <!-- selected day background color -->
    <attr name="selectedday_circle_color" format="color"/>
    <!-- the day text size -->
    <attr name="day_size" format="dimension"/>
    <!-- start day of a week, we support (sunday、monday and saturday) -->
    <attr name="firstDayOfWeek" format="enum">
        <enum name="sunday" value="1"/>
        <enum name="monday" value="2"/>
        <enum name="saturday" value="7"/>
    </attr>

    <!-- editor mode only -->
    <!-- test selected days (format:1,2,3,4) -->
    <attr name="selected_days" format="string"/>
    <!-- test month (format:2016-3) -->
    <attr name="month" format="string"/>
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



 [1]: https://github.com/TUBB/CalendarSelector/blob/master/library/src/main/java/com/tubb/calendarselector/library/SSMonthView.java
 [2]: https://github.com/TUBB/CalendarSelector/blob/master/library/src/main/java/com/tubb/calendarselector/library/SingleMonthSelector.java
 [3]: https://github.com/TUBB/CalendarSelector/blob/master/library/src/main/java/com/tubb/calendarselector/library/CalendarSelector.java
 [4]: https://github.com/TUBB/CalendarSelector/blob/master/app/src/main/java/com/tubb/calendarselector/SingleMonthSelectorActivity.java
 [5]: https://github.com/TUBB/CalendarSelector/blob/master/app/src/main/java/com/tubb/calendarselector/CalendarSelectorActivity.java
 [6]: https://github.com/TUBB/CalendarSelector/blob/master/library/src/main/java/com/tubb/calendarselector/library/SSDayDrawer.java
 [7]: https://github.com/TUBB/CalendarSelector/blob/master/app/src/main/java/com/tubb/calendarselector/CustomDrawerActivity.java