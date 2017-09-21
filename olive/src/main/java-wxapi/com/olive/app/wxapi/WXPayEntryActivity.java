package com.olive.app.wxapi;

/**
 * Created by TingYu Zhu on 2017/8/18.
 */
import com.biz.share.weixin.SendWX;
import com.biz.util.GsonUtil;
import com.biz.util.LogUtil;
import com.olive.R;
import com.olive.event.WeiPayResultEvent;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import de.greenrobot.event.EventBus;


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, SendWX.getAppId());
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
        finish();
    }

    @Override
    public void onReq(BaseReq req) {
        finish();
    }

    @Override
    public void onResp(BaseResp resp) {
        LogUtil.print("onResp" + this+":"+ GsonUtil.toJson(resp));
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            EventBus.getDefault().post(new WeiPayResultEvent((PayResp) resp));
        }
        finish();
    }


    @Override
    public void finish() {
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
        super.finish();
    }
}
