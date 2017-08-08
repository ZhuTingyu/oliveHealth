package com.olive.model;

import android.text.TextUtils;

import com.anye.greendao.gen.ConfigBeanDao;
import com.biz.http.ParaAndroidConfig;
import com.biz.http.Request;
import com.biz.http.ResponseJson;
import com.biz.http.RestMethodEnum;
import com.biz.util.AES;
import com.biz.util.GsonUtil;
import com.biz.util.Lists;
import com.biz.util.LogUtil;
import com.biz.util.MD5;
import com.biz.util.Utils;
import com.google.gson.reflect.TypeToken;
import com.olive.R;
import com.olive.model.db.DatabaseLoader;
import com.olive.model.entity.ConfigBean;
import com.olive.model.entity.DatabaseType;
import com.olive.model.entity.UserEntity;
import com.olive.util.HttpRequest;


import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Title: UserModel
 * Description:
 * Copyright:Copyright(c)2016
 * Company:博智维讯信息技术有限公司
 * CreateTime:20/07/2017  10:31
 *
 * @author johnzheng
 * @version 1.0
 */

public class UserModel {

    private static UserModel userModel;
    private UserEntity userInfo;
    private final CompositeSubscription subscription = new CompositeSubscription();
    private String hisUserId;

    public UserEntity getUserInfo() {
        if (userInfo == null) {
            userInfo = new UserEntity();
        }
        return userInfo;
    }

    public static UserModel getInstance() {
        if (userModel == null) {
            synchronized (UserModel.class) {
                userModel = new UserModel();
            }
        }
        return userModel;
    }

    public UserModel() {
        List<ConfigBean> list = DatabaseLoader.getDaoSession()
                .getConfigBeanDao().queryBuilder()
                .where(ConfigBeanDao.Properties.Type.eq(DatabaseType.TYPE_USER)).build().list();
        if (list != null && list.size() > 0) {
            for (ConfigBean configBean : list) {
                if (configBean.getTs() != null && configBean.getTs() > 0) {
                    try {
                        this.userInfo = GsonUtil.fromJson(configBean.getCache(), new TypeToken<UserEntity>() {
                        }.getType());
                    } catch (Exception e) {
                    }
                    break;
                }
            }
        }
        hisUserId = getUserId();
    }

    public boolean isLogin() {
        return this.userInfo != null && userInfo.userId.length() > 0;
    }


    public static Observable<String> loginMobile() {
        return Observable.create(subscriber -> {
            List<ConfigBean> configBeans = DatabaseLoader.getDaoSession()
                    .getConfigBeanDao()
                    .queryBuilder()
                    .where(ConfigBeanDao.Properties.Type.eq(DatabaseType.TYPE_LOGIN_NAME))
                    .build().list();
            ConfigBean configBean = null;
            if (configBeans != null && (!configBeans.isEmpty())) {
                configBean = configBeans.get(0);
            }
            if (configBean != null) {
                subscriber.onNext(configBean.getCache() == null ? "" : configBean.getCache());
            } else {
                subscriber.onNext("");
            }
            subscriber.onCompleted();
        });
    }

    public static Observable<Boolean> saveLoginMobile(String mobile) {
        return Observable.create(subscriber -> {
            ConfigBean configBean = new ConfigBean();
            configBean.setCache(mobile);
            configBean.setId("0");
            configBean.setTs(System.currentTimeMillis());
            configBean.setType(DatabaseType.TYPE_LOGIN_NAME);
            DatabaseLoader.getDaoSession().getConfigBeanDao().insertOrReplace(configBean);
            subscriber.onNext(true);
            subscriber.onCompleted();
        });
    }


    public String getUserId() {
        if (userInfo == null || userInfo.userId.length() < 0) return "";
        return userInfo.userId;
    }

    public String getToken() {
        if (userInfo == null || userInfo.userId.length() < 0) return "";
        return userInfo.token;
    }

    public String getNickName() {
        if (userInfo == null || userInfo.userId.length() < 0) return "";
        return userInfo.nickname;
    }

    public String getMobile() {
        if (userInfo == null || userInfo.userId.length() < 0) return "";
        return userInfo.mobile;
    }

    public String getShopId() {
        if (userInfo == null) return "";
        return userInfo.account;
    }

    public synchronized void setUserInfo(UserEntity userInfo) {
        LogUtil.print(";");
        List<ConfigBean> list = DatabaseLoader.
                getDaoSession()
                .getConfigBeanDao()
                .queryBuilder()
                .where(ConfigBeanDao.Properties.Type.eq(DatabaseType.TYPE_USER))
                .orderAsc(ConfigBeanDao.Properties.Id).list();
        boolean isExists = false;
        if (list != null && list.size() > 0) {
            for (ConfigBean configBean : list) {
                if (userInfo != null && userInfo.userId == configBean.getUserId()) {
                    configBean.setCache(GsonUtil.toJson(userInfo));
                    configBean.setKey(userInfo.mobile);
                    configBean.setTs(System.currentTimeMillis());
                    configBean.setType(DatabaseType.TYPE_USER);
                    isExists = true;
                } else {
                    configBean.setTs(-1l);
                }
            }
        } else if (list == null) {
            list = Lists.newArrayList();
        }
        if (!isExists && userInfo != null) {
            ConfigBean configBean = new ConfigBean();
            configBean.setCache(GsonUtil.toJson(userInfo));
            configBean.setKey(userInfo.mobile);
            configBean.setTs(System.currentTimeMillis());
            configBean.setType(DatabaseType.TYPE_USER);
            configBean.setId(DatabaseType.USER_ID + userInfo.userId);
            configBean.setUserId(userInfo.userId);
            list.add(configBean);
        }
        DatabaseLoader.getDaoSession().getConfigBeanDao().insertOrReplaceInTx(list, true);
        this.userInfo = userInfo;
        hisUserId = getUserId();
    }

    public synchronized void loginOut() {
        loginOutUser().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(b -> {
        }, throwable -> {
        });
        List<ConfigBean> list = DatabaseLoader.
                getDaoSession().getConfigBeanDao().queryBuilder().where(ConfigBeanDao.Properties.Type.eq(DatabaseType.TYPE_USER)).list();
        if (list != null && list.size() > 0) {
            for (ConfigBean configBean : list) {
                configBean.setTs(-1l);
            }
        } else if (list == null) {
            list = Lists.newArrayList();
        }
        DatabaseLoader.
                getDaoSession().getConfigBeanDao().insertOrReplaceInTx(list);
        hisUserId = getUserId();
        this.userInfo = null;
    }

    public static Observable<ResponseJson<UserEntity>> login(String mobile, String password) {
        return HttpRequest.<ResponseJson<UserEntity>>builder()
                .url(R.string.api_user_login)
                .addBody("mobile", mobile)
                .addBody("password", MD5.toMD5(password).toUpperCase())
                .setToJsonType(new TypeToken<ResponseJson<UserEntity>>() {}.getType())
                .requestPI().map(r -> {
                    if (r.isOk()) {
                        UserModel.getInstance().setUserInfo(r.data);
                    }
                    return r;
                });
    }

    public static Observable<ResponseJson<Object>> loginOutUser() {
        return Request.<ResponseJson<Object>>builder()

                .requestPI();
    }

}
