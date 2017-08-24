package com.olive.event;

/**
 * Created by TingYu Zhu on 2017/8/23.
 */

public class OrderListUpdateEvent {
    public int typeCode;
    public OrderListUpdateEvent(int typeCode){
        this.typeCode = typeCode;
    }
}

