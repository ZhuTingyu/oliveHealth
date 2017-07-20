package com.olive.app;

import com.biz.application.BaseApplication;
import com.biz.http.HttpConfig;
import com.biz.http.ParaAndroidConfig;
import com.biz.http.dispatcher.DispatcherUtil;
import com.biz.image.upload.OssTokenEntity;
import com.biz.push.PushManager;
import com.biz.util.FrescoManager;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.olive.BuildConfig;


/**
 * Title: OliveApplication
 * Description:
 * Copyright:Copyright(c)2016
 * Company:博智维讯信息技术有限公司
 * CreateTime:19/07/2017  14:42
 *
 * @author johnzheng
 * @version 1.0
 */

public class OliveApplication extends BaseApplication {
    static OliveApplication application;


    public static OliveApplication getApplication() {
        return application;
    }

    @Override
    public void onCreate() {
        application = this;
        super.onCreate();
        ParaAndroidConfig.getInstance().init(this);
        FrescoManager.init(this);
        PushManager.getInstance().register(this);
        DispatcherUtil.init(this);
        HttpConfig.setLog(BuildConfig.DEBUG);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

}
