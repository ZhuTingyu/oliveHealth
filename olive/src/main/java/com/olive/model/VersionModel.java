package com.olive.model;

import android.text.TextUtils;

import com.biz.http.HttpErrorException;
import com.biz.http.ResponseJson;
import com.biz.util.GsonUtil;
import com.google.gson.reflect.TypeToken;
import com.olive.R;
import com.olive.model.db.ConfigDaoHelper;
import com.olive.model.entity.ConfigBean;
import com.olive.model.entity.VersionEntity;
import com.olive.util.HttpRequest;


import rx.Observable;

/**
 * Created by TingYu Zhu on 2017/9/11.
 */

public class VersionModel {

    private static VersionModel versionModel;
    public static final int ANDROID = 1;

    private VersionEntity versionEntity;

    public static VersionModel getInstance(){
        if(versionModel == null){
            synchronized (VersionModel.class){
                versionModel = new VersionModel();
            }
        }
        return versionModel;
    }

    public VersionModel(){
        initDB();
    }

    private void initDB() {
        try {
            ConfigBean configBean = ConfigDaoHelper.getInstance().queryByType(ConfigDaoHelper.UPGRADE_VERSION_ID, ConfigDaoHelper.TYPE_UPGRADE_VERSION);
            if (configBean != null && !TextUtils.isEmpty(configBean.getCache())) {
                VersionEntity versionEntity = GsonUtil.fromJson(configBean.getCache(), new TypeToken<VersionEntity>() {
                }.getType());
                setVersionEntity(versionEntity);
            }
        } catch (Exception e) {
        }
    }

    public static Observable<VersionEntity> versionInfo() {
        return HttpRequest.<ResponseJson<VersionEntity>>builder()
                .setToJsonType(new TypeToken<ResponseJson<VersionEntity>>() {
                }.getType())
                .addBody("platform", ANDROID)
                .url(R.string.api_last_version)
                .requestPI().map(r -> {
                    if (r.isOk()){
                        VersionModel.getInstance().setVersionEntity(r.data);
                        return r.data;
                    }else throw new HttpErrorException(r);

                });
    }

    public static int getHisUpgradeVersion() {
        int version = 0;
        ConfigBean configBean = ConfigDaoHelper.getInstance().queryByType(ConfigDaoHelper.UPGRADE_VERSION_ID, ConfigDaoHelper.TYPE_UPGRADE_VERSION);
        if (configBean != null) {
            version = Integer.parseInt(configBean.getCache());
        }
        if (version  == 0) {
            version = 1;
        }
        return version;
    }

    public void saveHisUpgradeVersion() {
        ConfigBean configBean = ConfigDaoHelper.getInstance().queryByType(ConfigDaoHelper.UPGRADE_VERSION_ID, ConfigDaoHelper.TYPE_UPGRADE_VERSION);
        if (configBean == null) {
            configBean = new ConfigBean();
        }
        configBean.setId(ConfigDaoHelper.UPGRADE_VERSION_ID);
        configBean.setType(ConfigDaoHelper.TYPE_UPGRADE_VERSION);
        configBean.setCache(String.valueOf(versionEntity.version));
        ConfigDaoHelper.getInstance().addData(configBean);
    }

    public void setVersionEntity(VersionEntity versionEntity) {
        this.versionEntity = versionEntity;
    }

    public String getVarsionDes(){
        if(versionEntity == null || !versionEntity.varsionDes.isEmpty()) return "";
        return versionEntity.varsionDes;
    }

    public String getVersiondese(){
        if(versionEntity == null || !versionEntity.desc.isEmpty()) return "";
        return versionEntity.desc;
    }

    public String getAppDownloadUrl(){
        if(versionEntity == null || !versionEntity.url.isEmpty()) return "";
        return versionEntity.url;
    }

    public int getAppVersion(){
        if(versionEntity == null || versionEntity.version == 0) return 0;
        return versionEntity.version;
    }

    public VersionEntity getVersionEntity() {
        return versionEntity;
    }
}
