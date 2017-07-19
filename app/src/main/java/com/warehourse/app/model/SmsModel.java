package com.warehourse.app.model;

import com.biz.http.ResponseJson;
import com.biz.http.RestMethodEnum;
import com.google.gson.reflect.TypeToken;
import com.warehourse.app.R;
import com.warehourse.app.util.HttpRequest;

import rx.Observable;

/**
 * Title: SmsModel
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:2017/5/16  16:51
 *
 * @author wangwei
 * @version 1.0
 */
public class SmsModel {
    public static final String ACTION_REGISTER="REGISTER";
    public static final String ACTION_FORGOT_PASSWORD="FORGOT_PASSWORD";
    public static final String ACTION_CHANGE_MOBILE="CHANGE_MOBILE";
    public static Observable<ResponseJson<Boolean>> registerSmsValidate(String mobile,String smsCode) {
        return HttpRequest.<ResponseJson<Boolean>>builder()
                .addBody("mobile",mobile)
                .addBody("smsCode",smsCode)
                .addBody("action",ACTION_REGISTER)
                .restMethod(RestMethodEnum.REST_POST)
                .url(R.string.api_sms_validate)
                .setToJsonType(new TypeToken<ResponseJson<Boolean>>() {}.getType())
                .requestPI();
    }

    public static Observable<ResponseJson<Boolean>> forgotPasswordSmsValidate(String mobile,String smsCode) {
        return HttpRequest.<ResponseJson<Boolean>>builder()
                .addBody("mobile",mobile)
                .addBody("smsCode",smsCode)
                .addBody("action",ACTION_FORGOT_PASSWORD)
                .restMethod(RestMethodEnum.REST_POST)
                .url(R.string.api_sms_validate)
                .setToJsonType(new TypeToken<ResponseJson<Boolean>>() {
                }.getType())
                .requestPI();
    }


    public static Observable<ResponseJson<Boolean>> changeMobileSmsValidate(String mobile,String smsCode) {
        return HttpRequest.<ResponseJson<Boolean>>builder()
                .addBody("mobile",mobile)
                .addBody("smsCode",smsCode)
                .addBody("action",ACTION_CHANGE_MOBILE)
                .restMethod(RestMethodEnum.REST_POST)
                .url(R.string.api_sms_validate)
                .setToJsonType(new TypeToken<ResponseJson<Boolean>>() {
                }.getType())
                .requestPI();
    }
    public static Observable<ResponseJson<Boolean>> registerSendSms(String mobile) {
        return HttpRequest.<ResponseJson<Boolean>>builder()
                .addBody("mobile",mobile)
                .addBody("action",ACTION_REGISTER)
                .restMethod(RestMethodEnum.REST_POST)
                .url(R.string.api_sms_send)
                .setToJsonType(new TypeToken<ResponseJson<Boolean>>() {
                }.getType())
                .requestPI();
    }
    public static Observable<ResponseJson<Boolean>> forgotPasswordSendSms(String mobile) {
        return HttpRequest.<ResponseJson<Boolean>>builder()
                .addBody("mobile",mobile)
                .addBody("action",ACTION_FORGOT_PASSWORD)
                .restMethod(RestMethodEnum.REST_POST)
                .url(R.string.api_sms_send)
                .setToJsonType(new TypeToken<ResponseJson<Boolean>>() {
                }.getType())
                .requestPI();
    }

    public static Observable<ResponseJson<Boolean>> changeMobileSendSms(String mobile) {
        return HttpRequest.<ResponseJson<Boolean>>builder()
                .addBody("mobile",mobile)
                .addBody("action",ACTION_CHANGE_MOBILE)
                .restMethod(RestMethodEnum.REST_POST)
                .url(R.string.api_sms_send)
                .setToJsonType(new TypeToken<ResponseJson<Boolean>>() {
                }.getType())
                .requestPI();
    }



}
