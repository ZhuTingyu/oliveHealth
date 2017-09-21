package com.olive.event;

import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayResp;

/**
 * Created by TingYu Zhu on 2017/8/24.
 */

public class WeiPayResultEvent {

    public static final int SUCCESS = 0;
    public static final int ERROR = -1;
    public static final int CANCEL = -2;

    public PayResp req;

    public WeiPayResultEvent(PayResp req){
        this.req = req;
    }

}



