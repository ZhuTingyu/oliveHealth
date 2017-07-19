package com.biz.util;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.alibaba.sdk.android.man.MANHitBuilders;
import com.alibaba.sdk.android.man.MANPageHitBuilder;
import com.alibaba.sdk.android.man.MANService;
import com.alibaba.sdk.android.man.MANServiceProvider;
import com.biz.application.BaseApplication;

import java.util.Map;

/**
 * Title: AppAnalyticsUtil
 * Description:应用统计分析工具类
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:16/9/23  下午2:31
 *
 * @author dengqinsheng
 * @version 1.0
 */

public class AppAnalyticsUtil {
    /**
     * 是否开启打点
     */
    public static boolean isNeedHit = true;

    /**
     * 注册用户埋点
     *
     * @param usernick 这里传用户手机作nickname
     */
    public static void register(String usernick) {
        if (!isNeedHit) return;
        MANService manService = MANServiceProvider.getService();
        manService.getMANAnalytics().userRegister(usernick);
    }

    /**
     * 用户登录埋点
     *
     * @param usernick 这里传用户手机作nickname
     * @param userId
     */
    public static void login(String usernick, String userId) {
        if (!isNeedHit) return;
        MANService manService = MANServiceProvider.getService();
        manService.getMANAnalytics().updateUserAccount(usernick, userId);
    }

    /**
     * 用户注销埋点
     */
    public static void logout() {
        if (!isNeedHit) return;
        MANService manService = MANServiceProvider.getService();
        manService.getMANAnalytics().updateUserAccount("", "");
    }

    /**
     * 给Activity页面增加属性统计
     * 这句话要在一个页面的onPause 之前任何位置调用都可以
     *
     * @param properties 属性map
     */
    public static void updatePageProperties(Map<String, String> properties) {
        if (!isNeedHit) return;
        MANService manService = MANServiceProvider.getService();
        manService.getMANPageHitHelper().updatePageProperties(properties);
    }

    /**
     * 页面基础埋点,非Activity页面，如Fragment等的埋点
     *
     * @param pageName      传入参数为页面名称
     * @param referPageName 设置来源页面名称
     * @param duration      设置页面停留时间
     * @param properties    设置页面属性
     */
    public static void pageHit(String pageName, String referPageName, long duration, Map<String, String> properties) {
        if (!isNeedHit) return;
        MANPageHitBuilder pageHitBuilder = new MANPageHitBuilder(pageName);
        pageHitBuilder.setReferPage(referPageName);
        pageHitBuilder.setDurationOnPage(duration);
        pageHitBuilder.setProperties(properties);
        pageHitBuilder.build();
    }


    /**
     * 自定义事件埋点
     *
     * @param eventName     事件名称
     * @param eventDuration 可设置时长
     * @param pageName      可设置关联页面名称
     * @param properties    设置属性
     */
    public static void customerHit(@NonNull String eventName, long eventDuration, String pageName, Map<String, String> properties) {
        if (!isNeedHit) return;
        MANHitBuilders.MANCustomHitBuilder hitBuilder = new MANHitBuilders.MANCustomHitBuilder(eventName);
        if (eventDuration > 0)
            hitBuilder.setDurationOnEvent(eventDuration);

        if (!TextUtils.isEmpty(pageName))
            hitBuilder.setEventPage(pageName);

        if (properties != null && !properties.isEmpty())
            hitBuilder.setProperties(properties);

        MANService manService = MANServiceProvider.getService();
        manService.getMANAnalytics().getDefaultTracker().send(hitBuilder.build());
    }

    /**
     * 自定义事件埋点
     *
     * @param eventResId 事件名称resId
     * @param curClass   当前页面class
     * @param properties 设置属性
     */
    public static void customerHit(int eventResId, Class curClass, Map<String, String> properties) {
        if (!isNeedHit) return;
        MANHitBuilders.MANCustomHitBuilder hitBuilder = new MANHitBuilders.MANCustomHitBuilder(
                BaseApplication.getAppContext().getString(eventResId)
        );

        if (curClass != null)
            hitBuilder.setEventPage(curClass.getSimpleName());

        if (properties != null && !properties.isEmpty())
            hitBuilder.setProperties(properties);

        MANService manService = MANServiceProvider.getService();
        manService.getMANAnalytics().getDefaultTracker().send(hitBuilder.build());
    }

//    /**
//     * 打点记录网络请求开始
//     *
//     * @param host      接口地址
//     * @param method    方法类型:传 GET 或者 POST
//     * @param extraInfo 附带额外需要上报信息 可为空
//     */
//    public static void hitRequestStart(String host, String method, HashMap<String, String> extraInfo) {
//        MANNetworkPerformanceHitBuilder networkPerfBuilder = new MANNetworkPerformanceHitBuilder(host, method);
//        if (extraInfo != null && !extraInfo.isEmpty()) {
//            for (Map.Entry<String, String> entry : extraInfo.entrySet()) {
//                LogUtil.print("hitRequestStart key= " + entry.getKey() + " and value= " + entry.getValue());
//                networkPerfBuilder.withExtraInfo(entry.getKey(), entry.getValue());
//            }
//        }
//        networkPerfBuilder.hitRequestStart();
//    }


}
