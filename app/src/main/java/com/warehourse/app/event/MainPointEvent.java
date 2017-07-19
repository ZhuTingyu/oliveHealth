package com.warehourse.app.event;

/**
 * Created by wangwei on 2016/6/30.
 */
public class MainPointEvent {
    public boolean showPaymentButton;
    public int luckMoneyCount;
    public boolean showActivityRedPoint;
    public int messageCount;
    public boolean isShowPoint()
    {
        return showActivityRedPoint||messageCount>0;
    }


    public boolean isPoint(){
       return messageCount>0;
    }
}
