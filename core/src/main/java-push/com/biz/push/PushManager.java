package com.biz.push;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.util.Log;

import com.biz.application.BaseApplication;
import com.biz.http.HttpConfig;
import com.biz.http.ParaAndroidConfig;
import com.biz.http.R;
import com.biz.push.jpush.ExampleUtil;
import com.biz.util.LogUtil;
import com.biz.util.MD5;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.List;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Title: PushManager
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:2017/1/3  16:05
 *
 * @author wangwei
 * @version 1.0
 */
public class PushManager {

    private static final int MSG_SET_ALIAS = 1001;
    private static final int MSG_SET_TAGS = 1002;
    private static final long REGISTER_TIME = 1 * 60 * 1000;
    private long registerXiaoMiTime = 0;
    private static PushManager pushManager;
    public boolean isRegister;
    private boolean isInit;

    public static PushManager getInstance() {
        if (pushManager == null) {
            synchronized (PushManager.class) {
                pushManager = new PushManager();
            }
        }
        return pushManager;
    }
    private void init(Application application)
    {
        if(isInit) return;
        isInit=true;
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                if (isRegister) {
                    return;
                }
                if (registerXiaoMiTime + REGISTER_TIME < System.currentTimeMillis()) {
                    register(application);
                }
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    public String getPushTag() {
        return MD5.toMD5(ParaAndroidConfig.getInstance().deviceId);
    }

    public String getXiaoMiAppID() {
        return BaseApplication.getAppContext().getString(R.string.xiaomi_appid);
    }

    public String getXiaoMiAppKEY() {
        return BaseApplication.getAppContext().getString(R.string.xiaomi_appkey);
    }

    public void register(Application application) {
        init(application);
        if (isXiaoMi()) {
            if (shouldInit()) {
                LogUtil.print("----xiaomi registerPush----");
                MiPushClient.registerPush(application, getXiaoMiAppID(), getXiaoMiAppKEY());
            } else {
                LogUtil.print("----xiaomi not registerPush----");
            }
            if (registerXiaoMiTime + REGISTER_TIME < System.currentTimeMillis()) {
                String tag = getPushTag();
                registerXiaoMiTime = System.currentTimeMillis();
                List<String> list = MiPushClient.getAllAlias(application);
                if (list != null && list.size() > 0) {
                    for (String s : list)
                        if (!tag.equals(s))
                            MiPushClient.unsetAlias(application, s, null);
                }
                MiPushClient.setAlias(application, tag, null);
            }

        } else {
            JPushInterface.setDebugMode(HttpConfig.isLog());
            JPushInterface.init(application);
            if (registerXiaoMiTime + REGISTER_TIME < System.currentTimeMillis()) {
                registerXiaoMiTime = System.currentTimeMillis();
                mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, getPushTag()));
            }
        }
    }

    public boolean isXiaoMi() {
        return MIUIUtils.isMIUI();
    }

    public boolean shouldInit() {
        ActivityManager am = ((ActivityManager) BaseApplication.getAppContext().getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = BaseApplication.getAppContext().getPackageName();
        int myPid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    private final TagAliasCallback mTagsCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    LogUtil.print(logs);
                    isRegister = true;
                    break;

                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    LogUtil.print(logs);
                    if (ExampleUtil.isConnected(BaseApplication.getAppContext().getApplicationContext())) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_TAGS, tags), 1000 * 60);
                    } else {
                        LogUtil.print("No network");
                    }
                    break;

                default:
                    logs = "Failed with errorCode = " + code;
                    LogUtil.print(logs);
            }
        }
    };
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    LogUtil.print("Set alias in handler.");
                    JPushInterface.setAliasAndTags(BaseApplication.getAppContext(), (String) msg.obj, null, mAliasCallback);
                    break;

                case MSG_SET_TAGS:
                    LogUtil.print("Set tags in handler.");
                    JPushInterface.setAliasAndTags(BaseApplication.getAppContext(), null, (Set<String>) msg.obj, mTagsCallback);
                    break;

                default:
                    LogUtil.print("Unhandled msg - " + msg.what);
            }
        }
    };
    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    LogUtil.print(logs);
                    break;

                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    LogUtil.print(logs);
                    if (ExampleUtil.isConnected(BaseApplication.getAppContext())) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    } else {
                        LogUtil.print("No network");
                    }
                    break;

                default:
                    logs = "Failed with errorCode = " + code;
                    LogUtil.print(logs);
            }
        }

    };
}
