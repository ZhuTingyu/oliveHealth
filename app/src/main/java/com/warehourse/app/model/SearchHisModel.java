package com.warehourse.app.model;


import com.biz.util.Lists;
import com.biz.util.MD5;
import com.warehourse.app.model.db.ConfigDaoHelper;
import com.warehouse.dao.ConfigBean;

import android.text.TextUtils;

import java.util.List;

import rx.Observable;

/**
 * Created by wangwei on 2016/3/21.
 */
public class SearchHisModel{
    public static final int SAVE_COUNT = 5;

    public static Observable<List<String>> getSearchHisData() {
        return Observable.<List<String>>create(subscriber -> {
            List<ConfigBean> list = ConfigDaoHelper.getInstance().queryListByTypeOrderDescTs(ConfigDaoHelper.TYPE_SEARCH_KEY);
            List<String> listArray = Lists.newArrayList();
            if (list != null && list.size() > 0) {
                for (ConfigBean configBean : list) {
                    if (!TextUtils.isEmpty(configBean.getKey()))
                        listArray.add(configBean.getKey());
                }
            }
            subscriber.onNext(listArray);
            subscriber.onCompleted();
        });
    }

    public static Observable<Boolean> addSearchHis(String key) {
        return Observable.create(subscriber -> {
            List<ConfigBean> list = ConfigDaoHelper.getInstance().queryListByTypeOrderDescTs(ConfigDaoHelper.TYPE_SEARCH_KEY);
            if (list != null && list.size() >= SAVE_COUNT) {
                for (int i = SAVE_COUNT - 1; i < list.size(); i++) {
                    ConfigBean configBean = list.get(i);
                    ConfigDaoHelper.getInstance().delete(configBean);
                }
            }
            if (!TextUtils.isEmpty(key)) {
                ConfigBean configBean = ConfigDaoHelper.getInstance().getDataById(ConfigDaoHelper.SEARCH_ID + MD5.toMD5(key));
                if (configBean == null)
                    configBean = new ConfigBean();
                configBean.setId(ConfigDaoHelper.SEARCH_ID + MD5.toMD5(key));
                configBean.setUserId(UserModel.getInstance().getUserId());
                configBean.setType(ConfigDaoHelper.TYPE_SEARCH_KEY);
                configBean.setKey(key);
                configBean.setTs(System.currentTimeMillis());
                ConfigDaoHelper.getInstance().addData(configBean);
            }
            subscriber.onNext(true);
            subscriber.onCompleted();
        });
    }

    public static Observable<Boolean> deleteAllSearchHis() {
        return Observable.create(subscriber -> {
            ConfigDaoHelper.getInstance().deleteAll(ConfigDaoHelper.TYPE_SEARCH_KEY);
            subscriber.onNext(true);
            subscriber.onCompleted();
        });
    }

}
