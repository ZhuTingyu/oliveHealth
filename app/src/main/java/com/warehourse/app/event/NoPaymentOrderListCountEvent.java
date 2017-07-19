package com.warehourse.app.event;

/**
 * Created by wangwei on 2016/3/27.
 */
public class NoPaymentOrderListCountEvent {
    public int count=0;
    public NoPaymentOrderListCountEvent(int count){
        this.count=count;
    }
}
