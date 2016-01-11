package com.tubb.calendarselector.library;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * Created by tubingbing on 16/1/11.
 */
public class CSMonthView extends ViewGroup{

    public CSMonthView(Context context) {
        super(context);
    }

    public CSMonthView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CSMonthView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CSMonthView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}
