package com.biz.util;

import android.app.Application;

import com.biz.widget.CustomDraweeView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Title: FrescoManager
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:2017/1/3  16:34
 *
 * @author wangwei
 * @version 1.0
 */
public class FrescoManager {
    public static void init(Application application){
        FrescoImageUtil.initFresco(application);
        CustomDraweeView.initialize(Fresco.getDraweeControllerBuilderSupplier());
        SimpleDraweeView.initialize(Fresco.getDraweeControllerBuilderSupplier());
    }
}
