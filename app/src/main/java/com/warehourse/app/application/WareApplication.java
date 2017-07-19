package com.warehourse.app.application;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.biz.application.BaseApplication;
import com.biz.http.HttpConfig;
import com.biz.http.ParaAndroidConfig;
import com.biz.http.dispatcher.DispatcherUtil;
import com.biz.image.upload.OssTokenEntity;
import com.biz.push.PushManager;
import com.biz.util.FrescoManager;
import com.warehourse.app.BuildConfig;
import com.warehourse.app.model.db.DatabaseLoader;
import com.warehourse.app.ui.upgrade.UpgradeManager;

/**
 * Created by johnzheng on 3/8/16.
 */
public class WareApplication extends BaseApplication {

    static WareApplication application;

    private OssTokenEntity entity;
    private boolean isShowAdv=false;
    private long showAdvTime=0;

    public static WareApplication getApplication() {
        return application;
    }

    public void setShowAdv(boolean showAdv) {
        isShowAdv = showAdv;
        if(isShowAdv){
            showAdvTime=System.currentTimeMillis();
        }
    }

    public boolean isShowAdv() {
        if(isShowAdv){
           if(showAdvTime<1000*60*20){
                return false;
           }
        }
        return isShowAdv;
    }

    @Override
    public void onCreate() {
        application = this;
        super.onCreate();
        ParaAndroidConfig.getInstance().init(this);
        FrescoManager.init(this);
        PushManager.getInstance().register(this);
        DatabaseLoader.init(this);
        DispatcherUtil.init(this);
        UpgradeManager.register(this);
        HttpConfig.setLog(BuildConfig.DEBUG);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    public boolean isOssTokenEffective() {
        return entity != null && entity.isEffective(this);
    }

    public void setOssTokenEntity(OssTokenEntity entity) {
        this.entity = entity;
    }

    public OssTokenEntity getOssTokenEntity() {
        return entity;
    }
}
