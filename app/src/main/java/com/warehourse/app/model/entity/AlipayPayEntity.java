package com.warehourse.app.model.entity;

/**
 * Title: OrderPayAlipayEntity
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:2017/5/31  11:20
 *
 * @author wangwei
 * @version 1.0
 */
public class AlipayPayEntity {
    public String params;
    public String orderId;
    public String orderCode;
    public String getAlipayPayString()
    {
        return params;
    }
}
