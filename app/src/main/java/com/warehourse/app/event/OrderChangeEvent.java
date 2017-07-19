package com.warehourse.app.event;

/**
 * Created by wangwei on 2016/3/26.
 */
public class OrderChangeEvent {
    public long orderId;
    public OrderChangeEvent(long orderId){
        this.orderId=orderId;
    }
}
