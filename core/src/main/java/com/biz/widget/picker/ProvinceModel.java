package com.biz.widget.picker;

import com.google.gson.reflect.TypeToken;

import com.biz.application.BaseApplication;
import com.biz.util.GsonUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import rx.Observable;

/**
 * Title: ProvinceModel
 * Description:
 * Copyright:Copyright(c)2016
 * Company:博智维讯信息技术有限公司
 * CreateTime:8/12/16  2:39 PM
 *
 * @author johnzheng
 * @version 1.0
 */

public class ProvinceModel {
    private static Observable<String> getAreaJson() {
        return Observable.create(subscriber -> {
                    try {
                        StringBuffer sb = new StringBuffer();
                        InputStream is = BaseApplication.getAppContext().getAssets().open("simple-geo.json");
                        int len = -1;
                        byte[] buf = new byte[is.available()];
                        while ((len = is.read(buf)) != -1) {
                            sb.append(new String(buf, 0, len, "utf-8"));
                        }
                        is.close();
                        subscriber.onNext(sb.toString());
                        subscriber.onCompleted();
                    } catch (IOException e) {
                        subscriber.onError(e);
                    }
                }
        );
    }

    public static Observable<List<ProvinceEntity>> getProvince() {
        return getAreaJson().map(s -> {
            List<ProvinceEntity> list =
                    GsonUtil.fromJson(s, new TypeToken<List<ProvinceEntity>>() {
                    }.getType());
            return list;
        });
    }
}
