package com.warehourse.app.model.entity;

import com.biz.util.GsonUtil;

import java.util.List;

/**
 * Title: OrderApplyReturnEntity
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:2017/5/31  14:37
 *
 * @author wangwei
 * @version 1.0
 */
public class OrderApplyReturnEntity {
    public static final int TYPE_RETURN = 1;
    public static final int TYPE_CHANGE = 2;
    //1临期；2破损；3质量问题
    public static final int CAUSE_ADVENT = 1;
    public static final int CAUSE_DAMAGED = 2;
    public static final int CAUSE_QUESTION = 3;
    public String orderId;
    public int type;
    public int cause;
    public String description;
    public List<String> images;

    public String toJson() {
        return GsonUtil.toJson(this);
    }
}
