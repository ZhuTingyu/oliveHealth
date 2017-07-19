package com.warehourse.app.model;

import android.text.TextUtils;

import com.biz.http.Request;
import com.biz.http.ResponseJson;
import com.biz.http.RestMethodEnum;
import com.biz.image.upload.OssTokenEntity;
import com.biz.util.GsonUtil;
import com.google.gson.reflect.TypeToken;

import com.biz.util.IdsUtil;
import com.biz.util.Lists;
import com.warehourse.app.R;
import com.warehourse.app.application.WareApplication;
import com.warehourse.app.model.db.ConfigDaoHelper;
import com.warehourse.app.model.entity.CategoryEntity;
import com.warehourse.app.model.entity.InitEntity;
import com.warehourse.app.model.entity.NoticeEntity;
import com.warehourse.app.model.entity.OssEntity;
import com.warehourse.app.model.entity.ShareEntity;
import com.warehourse.app.model.entity.UpgradeEntity;
import com.warehourse.app.util.HttpRequest;
import com.warehourse.app.util.Version;
import com.warehouse.dao.ConfigBean;

import java.util.List;

import rx.Observable;

/**
 * Title: SystemModel
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:2017/5/15  15:21
 *
 * @author wangwei
 * @version 1.0
 */
public class SystemModel {
    private static SystemModel systemModel;
    private InitEntity initEntity;

    public static SystemModel getInstance() {
        if (systemModel == null) {
            synchronized (SystemModel.class) {
                systemModel = new SystemModel();
            }
        }
        return systemModel;
    }

    public SystemModel() {
        initDB();
    }

    public List<CategoryEntity> getCategories() {
        return initEntity==null? Lists.newArrayList(): initEntity.getCategories();
    }

    public List<String> getHotEntity() {
        return initEntity==null?Lists.newArrayList(): IdsUtil.getList(initEntity.hotKeywords," ",false);
    }

    public NoticeEntity getNoticeEntity(){
        return initEntity==null?null:initEntity.notice;
    }

    public String getPlaceHolder(){
        return initEntity==null|| initEntity.searchPlaceHolder==null?
                WareApplication.getAppContext().getString(R.string.text_hint_search)
                :initEntity.searchPlaceHolder;
    }

    public String getPlaceHolderWithNone(){
        return initEntity==null|| initEntity.searchPlaceHolder==null?
                null :initEntity.searchPlaceHolder;
    }

    public void setInitEntity(InitEntity initEntity) {
        this.initEntity = initEntity;
    }

    public List<OssEntity> getOss() {
        if (this.initEntity == null) {
            initDB();
        }
        return initEntity == null ? null : initEntity.oss;
    }
    private String  getOssName(String type) {
        if (this.initEntity == null) {
            initDB();
        }
        if(TextUtils.isEmpty(type)) return "";
        if(initEntity==null||initEntity.oss==null){
            return "";
        }
        for(OssEntity entity:initEntity.oss){
            if(type.equals(entity.type)){
                return entity.name;
            }
        }
        return "";
    }
    public String getOssPrivateBucketName(){
        String configName=WareApplication.getAppContext().getString(R.string.oss_private_name);
        return getOssName(configName);
    }
    public String getOssPublicBucketName(){
        String configName=WareApplication.getAppContext().getString(R.string.oss_public_name);
        return getOssName(configName);
    }
    public String getTell400() {
        if (initEntity == null || TextUtils.isEmpty(initEntity.tel400)) {
            return WareApplication.getAppContext().getString(R.string.default_tel400);
        }
        return initEntity.tel400;
    }

    public ShareEntity getShare()
    {
        if (this.initEntity == null) {
            initDB();
        }
        return initEntity == null ? null : initEntity.share;
    }



    private synchronized void initDB() {
        try {
            ConfigBean configBean = ConfigDaoHelper.getInstance().queryByType(ConfigDaoHelper.INIT_ID, ConfigDaoHelper.TYPE_INIT);
            if (configBean != null && !TextUtils.isEmpty(configBean.getCache())) {
                InitEntity initEntity = GsonUtil.fromJson(configBean.getCache(), new TypeToken<InitEntity>() {
                }.getType());
                setInitEntity(initEntity);
            }
        } catch (Exception e) {
        }
    }


    public static Observable<ResponseJson<InitEntity>> init() {
        return HttpRequest.<ResponseJson<InitEntity>>builder()
                .restMethod(RestMethodEnum.REST_POST)
                .url(R.string.api_init)
                .setToJsonType(new TypeToken<ResponseJson<InitEntity>>() {
                }.getType())
                .requestPI()
                .map(initEntityResponseJson -> {
                    if (initEntityResponseJson.isOk()) {
                        ConfigBean bean = new ConfigBean();
                        bean.setCache(GsonUtil.toJson(initEntityResponseJson.data));
                        bean.setId(ConfigDaoHelper.INIT_ID);
                        bean.setKey(ConfigDaoHelper.TYPE_INIT);
                        bean.setTs(System.currentTimeMillis());
                        bean.setType(ConfigDaoHelper.TYPE_INIT);
                        bean.setUserId(UserModel.getInstance().getUserId());
                        ConfigDaoHelper.getInstance().addData(bean);
                        SystemModel.getInstance().setInitEntity(initEntityResponseJson.data);
                    }
                    return initEntityResponseJson;
                });
    }

    public static Observable<Boolean> upgrade() {
        return HttpRequest.<ResponseJson<UpgradeEntity>>builder()
                .restMethod(RestMethodEnum.REST_POST)
                .url(R.string.api_init_upgrade)
                .setToJsonType(new TypeToken<ResponseJson<UpgradeEntity>>() {
                }.getType())
                .requestPI().map(r -> {
                    ConfigBean configBean = new ConfigBean();
                    configBean.setId(ConfigDaoHelper.UPGRADE_ID);
                    configBean.setType(ConfigDaoHelper.TYPE_UPGRADE);
                    configBean.setCache(GsonUtil.toJson(r.data));
                    ConfigDaoHelper.getInstance().addData(configBean);
                    return true;
                });
    }


    public static Observable<ResponseJson<OssTokenEntity>> getOssToken() {
        return HttpRequest.<ResponseJson<OssTokenEntity>>builder()
                .restMethod(RestMethodEnum.REST_POST)
                .url(R.string.api_upload_token)
                .setToJsonType(new TypeToken<ResponseJson<OssTokenEntity>>() {
                }.getType())
                .requestPI();
    }

    public static Observable<UpgradeEntity> getUpgrade() {
        return Observable.create(subscriber -> {
            ConfigBean configBea = ConfigDaoHelper.getInstance().queryByType(ConfigDaoHelper.UPGRADE_ID, ConfigDaoHelper.TYPE_UPGRADE);
            UpgradeEntity upgradeInfo = null;
            if (configBea != null) {
                try {
                    upgradeInfo = GsonUtil.fromJson(configBea.getCache(), new TypeToken<UpgradeEntity>() {
                    }.getType());
                } catch (Exception e) {
                }
            }
            try {
                Version version = new Version(getHisUpgradeVersion());
                if (version.compareTo(new Version(upgradeInfo.version)) >= 0) {
                    upgradeInfo = new UpgradeEntity();
                }
            } catch (Exception e) {
            }
            subscriber.onNext(upgradeInfo);
            subscriber.onCompleted();
        });
    }

    private static String getHisUpgradeVersion() {
        String version = "";
        ConfigBean configBean = ConfigDaoHelper.getInstance().queryByType(ConfigDaoHelper.UPGRADE_VERSION_ID, ConfigDaoHelper.TYPE_UPGRADE_VERSION);
        if (configBean != null) {
            version = configBean.getCache();
        }
        if (TextUtils.isEmpty(version)) {
            version = "0.0.0";
        }
        return version;
    }
    private static void saveHisUpgradeVersion(String version) {
        ConfigBean configBean = ConfigDaoHelper.getInstance().queryByType(ConfigDaoHelper.UPGRADE_VERSION_ID, ConfigDaoHelper.TYPE_UPGRADE_VERSION);
        if (configBean == null) {
            configBean = new ConfigBean();
        }
        configBean.setId(ConfigDaoHelper.UPGRADE_VERSION_ID);
        configBean.setType(ConfigDaoHelper.TYPE_UPGRADE_VERSION);
        configBean.setCache(version);
        ConfigDaoHelper.getInstance().addData(configBean);
    }
    public static Observable<Boolean> cancelUpgrade() {
        return Observable.create(subscriber -> {
            ConfigBean configBea = ConfigDaoHelper.getInstance().queryByType(ConfigDaoHelper.UPGRADE_ID, ConfigDaoHelper.TYPE_UPGRADE);
            if (configBea != null) {
                UpgradeEntity upgradeInfo = null;
                if (configBea != null) {
                    try {
                        upgradeInfo = GsonUtil.fromJson(configBea.getCache(), new TypeToken<UpgradeEntity>() {
                        }.getType());
                        saveHisUpgradeVersion(upgradeInfo.version);
                    } catch (Exception e) {
                    }
                }
                ConfigDaoHelper.getInstance().delete(configBea);
            }
            subscriber.onNext(true);
            subscriber.onCompleted();
        });
    }

    public static Observable<Boolean> isShowNotice()
    {
        return Observable.create(subscriber -> {
            NoticeEntity notice=getInstance().getNoticeEntity();
            if(notice==null)
                subscriber.onNext(false);
            else {
                subscriber.onNext(!TextUtils.isEmpty(notice.pictureUrl));
            }
            subscriber.onCompleted();
        });
    }
}
