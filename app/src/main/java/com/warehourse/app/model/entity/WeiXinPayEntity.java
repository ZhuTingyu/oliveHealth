package com.warehourse.app.model.entity;

import com.google.gson.annotations.SerializedName;

import com.tencent.mm.sdk.modelpay.PayReq;

/**
 * Created by wangwei on 2016/3/26.
 */
public class WeiXinPayEntity {
    public String appid;
    public String partnerid;
    public String prepayid;
    @SerializedName("package")
    public String packageValue;
    public String noncestr;
    public String timestamp;
    public String sign;
    public String extData;
    public String orderId;

    public PayReq getPayReq()
    {
        PayReq payReq=new PayReq();
        payReq.appId=appid;
        payReq.partnerId=partnerid;
        payReq.prepayId=prepayid;
        payReq.packageValue=packageValue;
        payReq.nonceStr=noncestr;
        payReq.timeStamp=timestamp;
        payReq.sign=sign;
        payReq.extData=extData;
        return payReq;
    }
}
