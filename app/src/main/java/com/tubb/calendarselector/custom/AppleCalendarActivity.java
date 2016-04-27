package com.tubb.calendarselector.custom;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationSet;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tubb.calendarselector.R;
import com.tubb.calendarselector.library.CalendarSelector;
import com.tubb.calendarselector.library.FullDay;
import com.tubb.calendarselector.library.MonthView;
import com.tubb.calendarselector.library.SCDateUtils;
import com.tubb.calendarselector.library.SCMonth;
import com.tubb.calendarselector.library.SegmentSelectListener;
import com.tubb.calendarselector.library.SingleMonthSelector;

import java.util.List;
import java.util.Locale;

/**
 * Created by tubingbing on 16/4/25.
 */
public class AppleCalendarActivity extends AppCompatActivity{

    private RecyclerView rvCalendar;
    private CalendarSelector selector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apple_calendar);
        setTitle("2016");
        rvCalendar = (RecyclerView) findViewById(R.id.rvCalendar);
        rvCalendar.setLayoutManager(new LinearLayoutManager(this));
        ((SimpleItemAnimator) rvCalendar.getItemAnimator()).setSupportsChangeAnimations(false);
        List<SCMonth> months = SCDateUtils.generateMonths(2016, 2016);
        rvCalendar.setAdapter(new CalendarAdpater(months));
        selector = new CalendarSelector(months, SingleMonthSelector.Mode.SEGMENT);
        selector.setSegmentSelectListener(new SegmentSelectListener() {
            @Override
            public void onSegmentSelect(FullDay startDay, FullDay endDay) {
                // TODO
            }
        });
    }

    class CalendarAdpater extends RecyclerView.Adapter<CalendarViewHolder>{

        List<SCMonth> months;
        DayViewInflater appleCalendarDayViewInflater;

        public CalendarAdpater(List<SCMonth> months){
            this.months = months;
            appleCalendarDayViewInflater = new AppleCalendarDayViewInflater(AppleCalendarActivity.this);
        }

        @Override
        public CalendarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CalendarViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_apple_calendar, parent, false));
        }

        @Override
        public void onBindViewHolder(final CalendarViewHolder holder, int position) {
            SCMonth scMonth = months.get(position);
            holder.tvMonthTitle.setText(String.format(Locale.getDefault(), "%d月", scMonth.getMonth()));
            holder.monthView.setSCMonth(scMonth, appleCalendarDayViewInflater);
            final int firstdayOfWeekPosInMonth = scMonth.getFirstdayOfWeekPosInMonth();
            // wait for MonthView measure finish
            holder.monthView.post(new Runnable() {
                @Override
                public void run() {
                    ValueAnimator lineAnimator = ValueAnimator.ofInt(0, -(firstdayOfWeekPosInMonth - 1) * holder.monthView.getDayWidth());
                    lineAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            int scrollX = (int) animation.getAnimatedValue();
                            holder.flScrollLine.scrollTo(scrollX , 0);
                        }
                    });

                    ValueAnimator monthAnimator = ValueAnimator.ofInt(0, -(firstdayOfWeekPosInMonth - 1) * holder.monthView.getDayWidth()
                            - (holder.monthView.getDayWidth() / 2 - holder.tvMonthTitle.getWidth() / 2));
                    monthAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            int scrollX = (int) animation.getAnimatedValue();
                            holder.flScrollMonth.scrollTo(scrollX , 0);
                        }
                    });
                    AnimatorSet animationSet = new AnimatorSet();
                    animationSet.play(monthAnimator).with(lineAnimator);
                    animationSet.setInterpolator(new AccelerateDecelerateInterpolator());
                    animationSet.setDuration(500);
                    animationSet.start();

                    int dayCount = holder.monthView.getCurrentMonthLastRowDayCount();
                    View decorView = holder.monthView.getLastHorizontalDecor();
                    if(decorView != null)
                        decorView.scrollTo((7 - dayCount)*holder.monthView.getDayWidth(), 0);

                }
            });
            selector.bind(rvCalendar, holder.monthView, position);
        }

        @Override
        public int getItemCount() {
            return months.size();
        }
    }

    class CalendarViewHolder extends RecyclerView.ViewHolder{

        View flScrollMonth;
        View flScrollLine;
        TextView tvMonthTitle;
        MonthView monthView;

        public CalendarViewHolder(View itemView) {
            super(itemView);
            tvMonthTitle = (TextView) itemView.findViewById(R.id.tvMonthTitle);
            monthView = (MonthView) itemView.findViewById(R.id.ssMv);
            flScrollMonth = itemView.findViewById(R.id.flScrollMonth);
            flScrollLine = itemView.findViewById(R.id.flScrollLine);
        }
    }

}
