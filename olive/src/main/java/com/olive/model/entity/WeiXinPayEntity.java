package com.olive.model.entity;

import com.google.gson.annotations.SerializedName;
import com.tencent.mm.sdk.modelpay.PayReq;

/**
 * Created by TingYu Zhu on 2017/8/18.
 */

public class WeiXinPayEntity {
    public String orderNo;
    public String outTradeNo;
    public String appid;
    public String partnerid;
    public String prepayid;
    public String noncestr;
    public String timestamp;
    public String sign;
    @SerializedName("package")
    public String packValue;

    public PayReq getPayReq(){
        PayReq payReq = new PayReq();
        payReq.appId = appid;
        payReq.partnerId = partnerid;
        payReq.prepayId = prepayid;
        payReq.packageValue = packValue;
        payReq.nonceStr = noncestr;
        payReq.timeStamp = timestamp;
        payReq.sign = sign;
        return payReq;
    }

}
