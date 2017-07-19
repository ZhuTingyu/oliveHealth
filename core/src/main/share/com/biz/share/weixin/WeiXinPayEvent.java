package com.biz.share.weixin;

public class WeiXinPayEvent {
    //0=成功 -1失败 -2取消
    public int code=-9;
    public String orderId="";
    public WeiXinPayEvent(int code,String orderId)
    {
        this.code=code;
        this.orderId=orderId;
    }

}