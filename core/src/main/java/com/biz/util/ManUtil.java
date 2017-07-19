package com.biz.util;

import android.app.Application;

import com.alibaba.sdk.android.man.MANService;
import com.alibaba.sdk.android.man.MANServiceProvider;
import com.biz.http.R;

/**
 * Title: ManUtil
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:2017/1/3  16:38
 *
 * @author wangwei
 * @version 1.0
 */
public class ManUtil {
    public static void init(Application application)
    {
        /**阿里云数据分析init**/
        // 获取MAN服务
        MANService manService = MANServiceProvider.getService();
        if (application.getResources().getBoolean(R.bool.isDebug))
            manService.getMANAnalytics().turnOnDebug();
        manService.getMANAnalytics().init(application, application.getApplicationContext());

    }
}
