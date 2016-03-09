package com.tubb.calendarselector;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tubb.calendarselector.library.FullDay;
import com.tubb.calendarselector.library.MonthView;
import com.tubb.calendarselector.library.SCDateUtils;
import com.tubb.calendarselector.library.SCMonth;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerActivity extends AppCompatActivity implements Protocol {

    private static final String TAG = "mv";

    TextView tvMonthTitle;
    ViewPager vpMonth;
    private List<SCMonth> months;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vp);

        tvMonthTitle = (TextView) findViewById(R.id.tvMonthTitle);
        vpMonth = (ViewPager) findViewById(R.id.vpMonth);

        months = SCDateUtils.generateMonths(2016, 3, 2016, 12);
        tvMonthTitle.setText(months.get(0).toString());

        List<Fragment> fragments = new ArrayList<>(months.size());
        for (SCMonth month:months){
            fragments.add(MonthFragment.newInstance(month));
        }
        vpMonth.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                SCMonth month = months.get(position);
                tvMonthTitle.setText(month.toString());
            }
        });
        vpMonth.setAdapter(new MonthFragmentAdapter(getSupportFragmentManager(), fragments));
    }

    @Override
    public void clickNextMonthDay(SCMonth currentMonth) {
        int currentIndex = months.indexOf(currentMonth);
        if(currentIndex+1 < months.size())
            vpMonth.setCurrentItem(currentIndex+1, true);
        else Toast.makeText(this, "the end", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void clickPrevMonthDay(SCMonth currentMonth) {
        int currentIndex = months.indexOf(currentMonth);
        if(currentIndex-1 >= 0)
            vpMonth.setCurrentItem(currentIndex-1, true);
        else Toast.makeText(this, "the start", Toast.LENGTH_SHORT).show();
    }

    class MonthFragmentAdapter extends FragmentPagerAdapter{

        List<Fragment> fragments;

        public MonthFragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

    public static final class MonthFragment extends Fragment{

        public static Fragment newInstance(SCMonth month){
            Fragment fragment = new MonthFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("month", month);
            fragment.setArguments(bundle);
            return fragment;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.item_vp, container, false);
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            final MonthView monthView = (MonthView) view.findViewById(R.id.scMv);
            final SCMonth month = getArguments().getParcelable("month");
            monthView.setSCMonth(month);
            monthView.setMonthDayClickListener(new MonthView.OnMonthDayClickListener() {
                @Override
                public void onMonthDayClick(FullDay day) {
                    if(SCDateUtils.isPrevMonthDay(month.getYear(), month.getMonth(),
                            day.getYear(), day.getMonth())){
                        clickPrevMonthDay(month);
                    }else if(SCDateUtils.isNextMonthDay(month.getYear(), month.getMonth(),
                            day.getYear(), day.getMonth())){
                        clickNextMonthDay(month);
                    }else{
                        monthView.getSCMonth().getSelectedDays().clear();
                        monthView.getSCMonth().getSelectedDays().add(day);
                        monthView.invalidate();
                    }
                }
            });
        }

        private void clickNextMonthDay(SCMonth month) {
            Activity activity = getActivity();
            if(activity instanceof Protocol){
                Protocol protocol = (Protocol)activity;
                protocol.clickNextMonthDay(month);
            }
        }

        private void clickPrevMonthDay(SCMonth month) {
            Activity activity = getActivity();
            if(activity instanceof Protocol){
                Protocol protocol = (Protocol)activity;
                protocol.clickPrevMonthDay(month);
            }
        }

    }


}
