package com.warehourse.app.model;

import android.text.TextUtils;

import com.biz.http.ResponseJson;
import com.biz.http.RestMethodEnum;
import com.biz.util.GsonUtil;
import com.biz.util.Lists;
import com.google.gson.reflect.TypeToken;
import com.warehourse.app.R;
import com.warehourse.app.model.db.ConfigDaoHelper;
import com.warehourse.app.model.entity.HomeEntity;
import com.warehourse.app.util.HttpRequest;
import com.warehouse.dao.ConfigBean;

import java.util.List;

import rx.Observable;

/**
 * Title: HomeModel
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:2017/5/22  11:06
 *
 * @author wangwei
 * @version 1.0
 */
public class HomeModel {
    public static Observable<ResponseJson<List<HomeEntity>>> home() {
        return HttpRequest.<ResponseJson<List<HomeEntity>>>builder()
                .restMethod(RestMethodEnum.REST_POST)
                .url(R.string.api_home)
                .userId(UserModel.getInstance().getUserId())
                .setToJsonType(new TypeToken<ResponseJson<List<HomeEntity>>>() {
                }.getType())
                .requestPI().map(r -> {
                    if (r.isOk()) {
                        saveHisHome(r.data);
                    }
                    return r;
                });
    }

    public static Observable<List<HomeEntity>> getHisHomeData() {
        return Observable.create(subscriber -> {
            subscriber.onNext(getHisHome());
            subscriber.onCompleted();
        });
    }

    private static List<HomeEntity> getHisHome() {
        String json = "";
        ConfigBean configBean = ConfigDaoHelper.getInstance().queryByType(ConfigDaoHelper.HOME_ID, ConfigDaoHelper.TYPE_HOME);
        if (configBean != null) {
            json = configBean.getCache();
        }
        List<HomeEntity> list = null;
        try {
            list = GsonUtil.fromJson(json, new TypeToken<List<HomeEntity>>() {
            }.getType());
        } catch (Exception e) {
        }
        if (list == null) list = Lists.newArrayList();
        return list;
    }

    private static void saveHisHome(List<HomeEntity> list) {
        ConfigBean configBean = ConfigDaoHelper.getInstance().queryByType(ConfigDaoHelper.HOME_ID, ConfigDaoHelper.TYPE_HOME);
        if (configBean == null) {
            configBean = new ConfigBean();
        }
        configBean.setId(ConfigDaoHelper.HOME_ID);
        configBean.setType(ConfigDaoHelper.TYPE_HOME);
        configBean.setCache(GsonUtil.toJson(list));
        ConfigDaoHelper.getInstance().addData(configBean);
    }

}
