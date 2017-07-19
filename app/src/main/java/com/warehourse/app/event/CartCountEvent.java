package com.warehourse.app.event;

/**
 * Title: CartCountEvent
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:2017/5/26  14:53
 *
 * @author wangwei
 * @version 1.0
 */
public class CartCountEvent {
    public int count=0;
    public CartCountEvent(int count){
        this.count=count;
    }
}
