package com.warehourse.app.model;

import com.google.gson.reflect.TypeToken;

import com.biz.http.ResponseJson;
import com.biz.http.RestMethodEnum;
import com.biz.image.upload.TextErrorException;
import com.warehourse.app.R;
import com.warehourse.app.application.WareApplication;
import com.warehourse.app.model.db.AvgDaoHelper;
import com.warehourse.app.util.HttpRequest;
import com.warehouse.dao.AvgBean;

import android.text.TextUtils;

import java.util.List;

import rx.Observable;

/**
 * Created by wangwei on 2016/5/26.
 */
public class AvgModel {
    public static Observable<Boolean> initAvg() {
        return HttpRequest.<ResponseJson<List<AvgBean>>>builder()
                .restMethod(RestMethodEnum.REST_POST)
                .url(R.string.api_init_avg)
                .setToJsonType(new TypeToken<ResponseJson<List<AvgBean>>>() {
                }.getType())
                .requestPI().map(advInfoResponseJson -> {
                    if(advInfoResponseJson.isOk()) {
                        new AvgDaoHelper().add(advInfoResponseJson.data);
                    }
                    return true;
                });
    }
    public static Observable<Boolean> isShowAvg() {
        return Observable.create(subscriber -> {
            AvgDaoHelper avgDaoHelper = new AvgDaoHelper();
            AvgBean avgBean = avgDaoHelper.queryAvg();
            if (avgBean == null)
                subscriber.onNext(false);
            else subscriber.onNext(true);
            subscriber.onCompleted();
        });
    }
    public static Observable<AvgBean> getShowAvg() {
        return Observable.create(subscriber -> {
            AvgDaoHelper avgDaoHelper = new AvgDaoHelper();
            AvgBean avgBean = avgDaoHelper.queryAvg();
            if (avgBean == null)
                subscriber.onError(new TextErrorException(WareApplication.getAppContext().getString(R.string.error_not_avg)));
            else {
                subscriber.onNext(avgBean);
                subscriber.onCompleted();
            }
        });
    }

}
