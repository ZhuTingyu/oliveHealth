package com.warehourse.app.event;

/**
 * Created by wangwei on 2016/3/23.
 */
public class HotKeyClickEvent {
    public String key;
    public boolean isPlaceHolder=false;
    public HotKeyClickEvent(String key)
    {
        this.key=key;
    }
    public HotKeyClickEvent(String key,boolean isPlaceHolder)
    {
        this.key=key;
        this.isPlaceHolder=isPlaceHolder;
    }
}
