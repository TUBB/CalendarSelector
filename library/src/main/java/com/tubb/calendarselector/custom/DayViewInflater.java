package com.tubb.calendarselector.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tubb.calendarselector.library.FullDay;

/**
 * Created by tubingbing on 16/4/13.
 */
public abstract class DayViewInflater {

    protected Context mContext;
    protected LayoutInflater mLayoutInflater;

    public DayViewInflater(Context context){
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    /**
     * inflate day view
     * @param container MonthView
     * @return day view
     */
    public abstract DayViewHolder inflateDayView(ViewGroup container);

    public Decor inflateHorizontalDecor(ViewGroup container, int row, int totalRow){
        return null;
    }

    public Decor inflateVerticalDecor(ViewGroup container, int col, int totalCol){
        return null;
    }

    protected int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static class Decor{

        private boolean showDecor = false;
        private View decorView;

        public Decor(View decorView){
            this(decorView, false);
        }

        public Decor(View decorView, boolean showDecor){
            this.decorView = decorView;
            this.showDecor = showDecor;
        }

        public View getDecorView() {
            return decorView;
        }

        public boolean isShowDecor() {
            return showDecor;
        }
    }
}
