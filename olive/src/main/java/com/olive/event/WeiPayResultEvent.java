package com.olive.event;

/**
 * Created by TingYu Zhu on 2017/8/24.
 */

public class WeiPayResultEvent {

    public static final int SUCCESS = 0;
    public static final int ERROR = -1;
    public static final int CANCEL = -2;

    public int code;

    public WeiPayResultEvent(int code){
        this.code = code;
    }

}



