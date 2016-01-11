package com.tubb.calendarselector.library;

import android.graphics.Canvas;

/**
 * Created by tubingbing on 16/1/28.
 */
public interface SSDayDrawer {

    void draw(SSDay ssDay, Canvas canvas, int row, int col, int dayViewWidth, int dayViewHeight);

    void onDayClick(SSDay ssDay, SSMonthView ssMonthView);
}
