package com.warehourse.b2b.wxapi;

import com.biz.base.BaseActivity;
import com.biz.share.weixin.SendWX;
import com.biz.share.weixin.WeiXinEvent;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.warehourse.app.R;

import android.content.Intent;
import android.os.Bundle;

import de.greenrobot.event.EventBus;

public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {
    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, SendWX.getAppId(), false);
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
    public void onReq(BaseReq arg0) {
        finish();
    }

    @Override
    public void onResp(BaseResp resp) {
        try {
            if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
                WeiXinEvent event = new WeiXinEvent();
                event.isOK=true;
                event.isError=true;
                event.json="";
                EventBus.getDefault().post(event);
            }
        } catch (Exception e) {
        }
        finish();
    }

    @Override
    public void finish() {
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
        super.finish();
    }
}
