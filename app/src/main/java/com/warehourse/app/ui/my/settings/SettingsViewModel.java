package com.warehourse.app.ui.my.settings;


import com.biz.base.BaseViewModel;
import com.biz.util.FrescoImageUtil;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;

/**
 * Created by wangwei on 2016/3/30.
 */
public class SettingsViewModel extends BaseViewModel {
    public SettingsViewModel(Object activity) {
        super(activity);
    }

    public void getCacheSize(Action1<String> onNext) {
        submitRequest(Observable.create(subscriber -> {
            long cacheSize = FrescoImageUtil.getCacheSize();
            long size = cacheSize / 1024l;
            String s = "";
            if (size > 1024l) {
                s = (Math.round((cacheSize / 1024d / 1024d) * 10 + 0.5) / 10.0) + "M";
            } else {
                s = (Math.round((cacheSize / 1024d) * 10 + 0.5) / 10.0) + "K";
                if (s.equals("0.1K"))s = "0K";
            }
            subscriber.onNext(s);
            subscriber.onCompleted();
        }), onNext, throwable -> {
        });

    }

    public void clear(Action0 action0) {
        submitRequest(Observable.create(subscriber -> {
            FrescoImageUtil.clearCache();
            subscriber.onNext(true);
            subscriber.onCompleted();
        }), r -> {
        }, throwable -> {
        }, action0);

    }
}
