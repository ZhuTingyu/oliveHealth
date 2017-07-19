package com.warehourse.app.model.entity;

/**
 * Title: OrderStatusEntity
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:2017/5/31  14:29
 *
 * @author wangwei
 * @version 1.0
 */
public class OrderStatusEntity {
    public String orderId;
    public String orderCode;
    public String businessName;
    public int payStatus;
    public String payStatusName;
    public int paymentType;
    public String paymentTypeName;
    public long payAmount;
    public long paidTime;
    public boolean paid;
}
