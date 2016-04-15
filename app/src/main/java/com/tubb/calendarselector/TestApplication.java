package com.tubb.calendarselector;

import android.app.Application;

import com.github.moduth.blockcanary.BlockCanary;
import com.github.moduth.blockcanary.BlockCanaryContext;

/**
 * Created by tubingbing on 16/4/15.
 */
public class TestApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        // Do it on main process
        BlockCanary.install(this, new AppBlockCanaryContext()).start();
    }

    class AppBlockCanaryContext extends BlockCanaryContext {
        // override to provide context like app qualifier, uid, network type, block threshold, log save path

        // this is default block threshold, you can set it by phone's performance
        @Override
        public int getConfigBlockThreshold() {
            return 500;
        }

        // if set true, notification will be shown, else only write log file
        @Override
        public boolean isNeedDisplay() {
            return BuildConfig.DEBUG;
        }

        // path to save log file
        @Override
        public String getLogPath() {
            return "/blockcanary/performance";
        }
    }
}
