package com.warehourse.app.event;

/**
 * Title: UserEvent
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:2017/5/15  17:58
 *
 * @author wangwei
 * @version 1.0
 */
public class UserEvent {
    public static final int TYPE_CHANGE_MOBILE = 7;
    public static final int TYPE_CHANGE_AVATAR=8;
    public static final int TYPE_CHANGE_REGISTER=9;
    public static final int TYPE_CHANGE_ADDRESS=11;
    public static final int TYPE_CHANGE_DELIVERY_NAME = 12;
    public static final int TYPE_MESSAGE=10;
    public static final int TYPE_LOGIN=5;
    public static final int TYPE_REGISTER=5;
    public int type;

    public UserEvent(int type) {
        this.type = type;
    }
}
