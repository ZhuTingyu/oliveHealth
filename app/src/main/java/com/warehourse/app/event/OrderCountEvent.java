package com.warehourse.app.event;

import com.warehourse.app.model.entity.OrderCountEntity;

/**
 * Title: OrderCountEvent
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:2017/6/6  14:36
 *
 * @author wangwei
 * @version 1.0
 */
public class OrderCountEvent {
    private OrderCountEntity mOrderCountEntity;

    public OrderCountEvent(OrderCountEntity orderCountEntity) {
        this.mOrderCountEntity = orderCountEntity;
    }

    public String getWaitingPay() {
        return mOrderCountEntity == null ? "0" : mOrderCountEntity.waitingPay+"";
    }

    public String getWaitingDelivery() {
        return mOrderCountEntity == null ? "0" : mOrderCountEntity.waitingDelivery+"";
    }

    public String getWaitingReceived() {
        return mOrderCountEntity == null ? "0" : mOrderCountEntity.waitingReceived+"";
    }

    public String getFinished() {
        return mOrderCountEntity == null ? "0" : mOrderCountEntity.finished+"";
    }
}
