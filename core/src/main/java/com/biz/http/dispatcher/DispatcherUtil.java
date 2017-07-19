package com.biz.http.dispatcher;

import com.biz.http.ParaAndroidConfig;
import com.google.gson.reflect.TypeToken;

import com.biz.http.R;
import com.biz.http.Request;
import com.biz.http.ResponseJson;
import com.biz.http.RestMethodEnum;
import com.biz.application.BaseApplication;
import com.biz.http.cache.HttpUrlCache;
import com.biz.util.Lists;
import com.biz.util.Maps;

import android.app.Application;
import android.text.TextUtils;

import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by wangwei on 2016/3/15.
 */
public class DispatcherUtil {
    private static DispatcherUtil dispatcherUtil;
    private boolean isDispatcherIng;
    private Map<String, DispatcherInfo> mapTime;
    private List<String> headList;
    public final CompositeSubscription subscription = new CompositeSubscription();

    public static void init(Application application)
    {
        if (application.getResources().getBoolean(R.bool.isDispatcher)) {
            DispatcherUtil.getInstance().dispatcher(ParaAndroidConfig.getInstance().routerMac);
        }
    }

    public static DispatcherUtil getInstance() {
        if (dispatcherUtil == null) {
            synchronized (DispatcherUtil.class) {
                dispatcherUtil = new DispatcherUtil();
            }
        }
        return dispatcherUtil;
    }

    public void dispatcher(String key) {
        if (!BaseApplication.getAppContext().getResources().getBoolean(R.bool.isDispatcher)) return;
        if (isDispatcherIng) {
            return;
        }
        isDispatcherIng = true;
        //判断当前地址是否进行过测速
        if (!HttpUrlCache.getInstance().isUpdateCache(key)) {
            return;
        }
        //获取测速地址
        subscription.clear();
        subscription.add(Request.<ResponseJson<DispatcherInfo>>builder()
                .url(R.string.api_init_master)
                .headUrl(Lists.newArrayList(HttpUrlCache.getInstance().getMasterHeadUrl()))
                .restMethod(RestMethodEnum.DISPATCHER)
                .setToJsonType(new TypeToken<ResponseJson<DispatcherInfo>>() {
                }.getType())
                .requestPI()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(b -> {
                    if (b.isOk() && b.data != null) {
                        start(b.data.getList(), key);
                    } else {
                        isDispatcherIng = false;
                    }
                }, throwable -> {
                    isDispatcherIng = false;
                }));
    }

    /**
     * 测速
     *
     * @param headList
     * @param key
     */
    private void start(List<String> headList, String key) {
        this.headList = headList;
        mapTime = Maps.newHashMap();
        for (String headUrl : headList) {
            subscription.add(Request.<ResponseJson<DispatcherInfo>>builder()
                    .url(R.string.api_init_dispatcher)
                    .headUrl(Lists.newArrayList(headUrl))
                    .restMethod(RestMethodEnum.DISPATCHER)
                    .setToJsonType(new TypeToken<ResponseJson<DispatcherInfo>>() {
                    }.getType())
                    .requestPI()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(b -> {
                        DispatcherInfo d = null;
                        if (b.data == null) {
                            d = new DispatcherInfo();
                            d.execTime = -1;
                        } else {
                            d = b.data;
                            d.execTime = b.execTime;
                        }
                        addInitTime(headUrl, d, key);
                    }, throwable -> {
                        DispatcherInfo d = new DispatcherInfo();
                        d.execTime = -1;
                        addInitTime(headUrl, d, key);
                    }));
        }
    }

    private void addInitTime(String headUrl, DispatcherInfo entity, String ssidKey) {
        mapTime.put(headUrl, entity);
        if (mapTime.size() == headList.size()) {
            subscription.clear();
            //判断时间用的少的
            long t = 0;
            String key = "";
            for (Map.Entry<String, DispatcherInfo> e : mapTime.entrySet()) {
                if (e.getValue() != null && e.getValue().execTime > 0) {
                    if (t <= 0) {
                        key = e.getKey();
                        t = e.getValue().execTime;
                    } else if (t > e.getValue().execTime) {
                        key = e.getKey();
                        t = e.getValue().execTime;
                    }
                }
            }
            if (!TextUtils.isEmpty(key)) {
                DispatcherInfo e = mapTime.get(key);
                HttpUrlCache.getInstance().saveUrlList(e.getList(), ssidKey);
            }
            isDispatcherIng = false;
        }
    }

}
