package com.olive.util;

import com.biz.application.BaseApplication;
import com.biz.http.Request;
import com.biz.http.RestMethodEnum;
import com.olive.model.UserModel;

/**
 * Title: HttpRequest
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:2017/5/15  15:22
 *
 * @author wangwei
 * @version 1.0
 */
public class HttpRequest<T> extends Request<T> {
    public static <T> Request<T> builder() {
        HttpRequest<T> request = new HttpRequest<T>();
        request.restMethod(RestMethodEnum.REST_POST);
        request.userId(UserModel.getInstance().getUserId());
        request.token(UserModel.getInstance().getToken());
        request.https(false);
        request.setDefaultConnectTime();
        return request;
    }
}
