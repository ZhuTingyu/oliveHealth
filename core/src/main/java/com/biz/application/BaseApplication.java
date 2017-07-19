package com.biz.application;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.alibaba.sdk.android.man.MANService;
import com.alibaba.sdk.android.man.MANServiceProvider;
import com.biz.http.ParaAndroidConfig;
import com.biz.http.R;
import com.biz.http.dispatcher.DispatcherUtil;
import com.biz.widget.CustomDraweeView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by wangwei on 2016/4/14.
 */
public abstract class BaseApplication extends Application {
    private static BaseApplication httpApplication;

    public static BaseApplication getAppContext() {
        return httpApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.httpApplication = this;
    }

    /**
     * 获得当前进程的名字
     *
     * @param context
     * @return
     */
    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {

                return appProcess.processName;
            }
        }
        return null;
    }
}
